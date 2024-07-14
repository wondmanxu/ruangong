package rgLuntan.service.impl;

import rgLuntan.mapper.ForumMapper;
import rgLuntan.mapper.TopicMapper;
import rgLuntan.model.Tag;
import rgLuntan.model.Topic;
import rgLuntan.model.User;
import rgLuntan.service.*;
import rgLuntan.service.*;
import rgLuntan.util.MyPage;
import rgLuntan.util.SensitiveWordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@Transactional
public class ForumService implements IForumService  {

    @Resource
    private ForumMapper forumMapper;
    @Resource
    @Lazy
    private IForumService forumService;

    @Override
    public List<String> getAllForums(){
        return forumMapper.getAllForums();
    }

    @Override
    public Integer getForumIdByName(String forumsName) {
        return forumMapper.getForumIdByName(forumsName);
    }


}
