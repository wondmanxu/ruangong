package rgLuntan.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import rgLuntan.model.Forum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import rgLuntan.util.MyPage;

import java.util.Map;
import java.util.List;

public interface ForumMapper extends BaseMapper<Forum> {

    @Select("SELECT forumsName FROM forum")
    List<String> getAllForums();

    @Select("SELECT id FROM forum WHERE forumsName = #{forumsName}")
    Integer getForumIdByName(@Param("forumsName") String forumsName);

}
