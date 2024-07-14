package rgLuntan.mapper;

import rgLuntan.model.Topic;
import rgLuntan.util.MyPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

 
public interface TopicMapper extends BaseMapper<Topic> {

    MyPage<Map<String, Object>> selectAll(MyPage<Map<String, Object>> iPage, @Param("tab") String tab);

    MyPage<Map<String, Object>> selectByTag(MyPage<Map<String, Object>> iPage, @Param("tag") String tag);

    MyPage<Map<String, Object>> selectByForumId(MyPage<Map<String, Object>> iPage, @Param("forumId") Integer forumId);

    MyPage<Map<String, Object>> selectByUserId(MyPage<Map<String, Object>> iPage, @Param("userId") Integer userId);

    MyPage<Map<String, Object>> selectAllForAdmin(MyPage<Map<String, Object>> iPage, @Param("startDate") String
            startDate, @Param("endDate") String endDate, @Param("username") String username);

    int countToday();

    MyPage<Map<String, Object>> searchBar(MyPage<Map<String, Object>> iPage, @Param("tab") String tab, @Param("forumsName") String forumsName, @Param("keyword") String keyword);

    MyPage<Map<String, Object>> search(MyPage<Map<String, Object>> iPage, @Param("keyword") String keyword);
}
