package rgLuntan.service.impl;

import rgLuntan.config.service.EmailService;
import rgLuntan.config.service.TelegramBotService;
import rgLuntan.config.websocket.MyWebSocket;
import rgLuntan.mapper.CommentMapper;
import rgLuntan.model.Comment;
import rgLuntan.model.Topic;
import rgLuntan.model.User;
import rgLuntan.model.vo.CommentsByTopic;
import rgLuntan.service.*;
import rgLuntan.service.*;
import rgLuntan.util.Message;
import rgLuntan.util.MyPage;
import rgLuntan.util.SensitiveWordUtil;
import rgLuntan.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

 
@Service
@Transactional
public class CommentService implements ICommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    @Resource
    private CommentMapper commentMapper;
    @Resource
    @Lazy
    private ITopicService topicService;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    @Lazy
    private IUserService userService;
    @Resource
    private INotificationService notificationService;
    @Resource
    private EmailService emailService;
    @Resource
    private TelegramBotService telegramBotService;

    // 根据话题id查询评论
    @Override
    public List<CommentsByTopic> selectByTopicId(Integer topicId) {
        List<CommentsByTopic> commentsByTopics = commentMapper.selectByTopicId(topicId);
        // 对评论内容进行过滤，然后再写入redis
        for (CommentsByTopic commentsByTopic : commentsByTopics) {
            commentsByTopic.setContent(SensitiveWordUtil.replaceSensitiveWord(commentsByTopic.getContent(), "*",
                    SensitiveWordUtil.MinMatchType));
        }
        return commentsByTopics;
    }

    // 删除话题时删除相关的评论
    @Override
    public void deleteByTopicId(Integer topicId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getTopicId, topicId);
        commentMapper.delete(wrapper);
    }

    // 根据用户id删除评论记录
    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getUserId, userId);
        commentMapper.delete(wrapper);
    }

    // 保存评论
    @Override
    public Comment insert(Comment comment, Topic topic, User user) {
        if (systemConfigService.selectAllConfig().get("comment_need_examine").equals("1")) {
            comment.setStatus(false);// 审核中
        } else {
            comment.setStatus(true);// 无需审核
        }
        commentMapper.insert(comment);

        // 话题的评论数+1
        topic.setCommentCount(topic.getCommentCount() + 1);
        topicService.update(topic, null);

        // 增加用户积分
        user.setScore(user.getScore() + Integer.parseInt(systemConfigService.selectAllConfig().get
                ("create_comment_score")));
        userService.update(user);

        // 通知
        // 给评论的作者发通知
        if (comment.getCommentId() != null) {
            Comment targetComment = this.selectById(comment.getCommentId());
            if (!user.getId().equals(targetComment.getUserId())) {
                notificationService.insert(user.getId(), targetComment.getUserId(), topic.getId(), "REPLY", comment
                        .getContent());

                String emailTitle = "你在话题 %s 下的评论被 %s 回复了，快去看看吧！";
                // 如果开启了websocket，就发网页通知
                if (systemConfigService.selectAllConfig().get("websocket").equals("1")) {
                    MyWebSocket.emit(targetComment.getUserId(), new Message("notifications", String.format(emailTitle, topic
                            .getTitle(), user.getUsername())));
                    MyWebSocket.emit(targetComment.getUserId(), new Message("notification_notread", 1));
                }
                // 发送邮件通知
                User targetUser = userService.selectById(targetComment.getUserId());
                if (!StringUtils.isEmpty(targetUser.getEmail()) && targetUser.getEmailNotification()) {
                    String emailContent = "回复内容: %s <br><a href='%s/topic/%s' target='_blank'>传送门</a>";
                    new Thread(() -> emailService.sendEmail(targetUser.getEmail(), String.format(emailTitle, topic.getTitle(),
                            user.getUsername()), String.format(emailContent, comment.getContent(), systemConfigService
                            .selectAllConfig().get("base_url"), topic.getId()))).start();
                }
            }
        }
        // 给话题作者发通知
        if (!user.getId().equals(topic.getUserId())) {
            notificationService.insert(user.getId(), topic.getUserId(), topic.getId(), "COMMENT", comment.getContent());
            // 发送邮件通知
            String emailTitle = "%s 评论你的话题 %s 快去看看吧！";
            // 如果开启了websocket，就发网页通知
            if (systemConfigService.selectAllConfig().get("websocket").equals("1")) {
                MyWebSocket.emit(topic.getUserId(), new Message("notifications", String.format(emailTitle, user.getUsername()
                        , topic.getTitle())));
                MyWebSocket.emit(topic.getUserId(), new Message("notification_notread", 1));
            }
            User targetUser = userService.selectById(topic.getUserId());
            if (!StringUtils.isEmpty(targetUser.getEmail()) && targetUser.getEmailNotification()) {
                String emailContent = "评论内容: %s <br><a href='%s/topic/%s' target='_blank'>传送门</a>";
                new Thread(() -> emailService.sendEmail(targetUser.getEmail(), String.format(emailTitle, user.getUsername(),
                        topic.getTitle()), String.format(emailContent, comment.getContent(), systemConfigService.selectAllConfig
                        ().get("base_url"), topic.getId()))).start();
            }
        }

        // 日志 TODO

        // 发送TG通知
        new Thread(() -> {
            String formatMessage;
            String domain = systemConfigService.selectAllConfig().get("base_url");
            if (systemConfigService.selectAllConfig().get("content_style").equals("MD")) {
                formatMessage = String.format("%s 评论了话题 [%s](%s) 内容： %s", user.getUsername(), topic.getTitle(), domain + "/topic/" + topic.getId(), StringUtil.removeSpecialChar(comment.getContent()));
            } else {
                formatMessage = String.format("%s 评论了话题 <a href=\"%s\">%s</a> 内容： %s", user.getUsername(), domain + "/topic/" + topic.getId(), topic.getTitle(), StringUtil.removeSpecialChar(comment.getContent()));
            }
            Integer message_id = telegramBotService.init().sendMessage(formatMessage, true, null);
            Comment newComment = new Comment();
            newComment.setId(comment.getId());
            newComment.setTgMessageId(message_id);
            commentMapper.updateById(newComment);

        }).start();

        return comment;
    }

    @Override
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    @Override
    public Comment selectByTgMessageId(Integer messageId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getTgMessageId, messageId);
        List<Comment> comments = commentMapper.selectList(wrapper);
        return comments.size() > 0 ? comments.get(0) : null;
    }

    // 更新评论
    @Override
    public void update(Comment comment) {
        commentMapper.updateById(comment);
    }

    // 对评论点赞
    @Override
    public int vote(Comment comment, User user) {
        String upIds = comment.getUpIds();
        // 将点赞用户id的字符串转成集合
        Set<String> strings = StringUtils.commaDelimitedListToSet(upIds);
        // 把新的点赞用户id添加进集合，这里用set，正好可以去重，如果集合里已经有用户的id了，那么这次动作被视为取消点赞
        Integer userScore = user.getScore();
        if (strings.contains(String.valueOf(user.getId()))) { // 取消点赞行为
            strings.remove(String.valueOf(user.getId()));
            userScore -= Integer.parseInt(systemConfigService.selectAllConfig().get("up_comment_score"));
        } else { // 点赞行为
            strings.add(String.valueOf(user.getId()));
            userScore += Integer.parseInt(systemConfigService.selectAllConfig().get("up_comment_score"));
        }
        // 再把这些id按逗号隔开组成字符串
        comment.setUpIds(StringUtils.collectionToCommaDelimitedString(strings));
        // 更新评论
        this.update(comment);
        // 增加用户积分
        user.setScore(userScore);
        userService.update(user);
        return strings.size();
    }

    // 删除评论
    @Override
    public void delete(Comment comment) {
        if (comment != null) {
            // 话题评论数-1
            Topic topic = topicService.selectById(comment.getTopicId());
            topic.setCommentCount(topic.getCommentCount() - 1);
            topicService.update(topic, null);
            // 减去用户积分
            User user = userService.selectById(comment.getUserId());
            user.setScore(user.getScore() - Integer.parseInt(systemConfigService.selectAllConfig().get
                    ("delete_comment_score").toString()));
            userService.update(user);
            // 删除评论
            commentMapper.deleteById(comment.getId());
        }
    }

    // 查询用户的评论
    @Override
    public MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize) {
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, pageSize == null ? Integer.parseInt(systemConfigService
                .selectAllConfig().get("page_size").toString()) : pageSize);
        MyPage<Map<String, Object>> page = commentMapper.selectByUserId(iPage, userId);
        for (Map<String, Object> map : page.getRecords()) {
            Object content = map.get("content");
            map.put("content", StringUtils.isEmpty(content) ? null : SensitiveWordUtil.replaceSensitiveWord(content
                    .toString(), "*", SensitiveWordUtil.MinMatchType));
        }
        return page;
    }

    // ---------------------------- admin ----------------------------
    @Override
    public MyPage<Map<String, Object>> selectForAdmin(Integer pageNo, Integer admUserId, String startDate, String endDate, String username){
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, Integer.parseInt((String) systemConfigService.selectAllConfig().get("page_size")));
        return commentMapper.selectForAdmin(iPage,admUserId, startDate, endDate, username);
    }

    @Override
    public MyPage<Map<String, Object>> selectAllForAdmin(Integer pageNo, String startDate, String endDate, String username) {
        MyPage<Map<String, Object>> iPage = new MyPage<>(pageNo, Integer.parseInt((String) systemConfigService.selectAllConfig().get("page_size")));
        return commentMapper.selectAllForAdmin(iPage, startDate, endDate, username);
    }

    // 查询今天新增的话题数
    @Override
    public int countToday() {
        return commentMapper.countToday();
    }
}
