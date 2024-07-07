package rgLuntan.mapper;

import rgLuntan.model.Collect;
import rgLuntan.util.MyPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

 
public interface CollectMapper extends BaseMapper<Collect> {

    MyPage<Map<String, Object>> selectByUserId(MyPage<Map<String, Object>> iPage, @Param("userId") Integer userId);
}
