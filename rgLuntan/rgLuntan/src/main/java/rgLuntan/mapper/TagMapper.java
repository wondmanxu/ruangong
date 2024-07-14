package rgLuntan.mapper;

import rgLuntan.model.Tag;
import rgLuntan.util.MyPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

 
public interface TagMapper extends BaseMapper<Tag> {

    //MyPage<Map<String, Object>> selectTopicByTagId(MyPage<Map<String, Object>> iPage, @Param("tagId") Integer tagId);

    MyPage<Map<String, Object>> selectTopicByTagId(MyPage<Map<String, Object>> iPage, @Param("tagId") Integer tagId, @Param("forumsName") String forumsName, @Param("keyword") String keyword);

    int countToday();
}
