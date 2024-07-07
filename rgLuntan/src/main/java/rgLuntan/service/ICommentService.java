package rgLuntan.service;

import rgLuntan.model.Comment;
import rgLuntan.model.Topic;
import rgLuntan.model.User;
import rgLuntan.model.vo.CommentsByTopic;
import rgLuntan.util.MyPage;

import java.util.List;
import java.util.Map;

 
public interface ICommentService {
    // 根据话题id查询评论
    List<CommentsByTopic> selectByTopicId(Integer topicId);

    // 删除话题时删除相关的评论
    void deleteByTopicId(Integer topicId);

    // 根据用户id删除评论记录
    void deleteByUserId(Integer userId);

    // 保存评论
    Comment insert(Comment comment, Topic topic, User user);

    Comment selectById(Integer id);

    Comment selectByTgMessageId(Integer messageId);

    // 更新评论
    void update(Comment comment);

    // 对评论点赞
    int vote(Comment comment, User user);

    // 删除评论
    void delete(Comment comment);

    // 查询用户的评论
    MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize);

    MyPage<Map<String, Object>> selectAllForAdmin(Integer pageNo, String startDate, String endDate, String username);

    // 查询今天新增的话题数
    int countToday();
}
