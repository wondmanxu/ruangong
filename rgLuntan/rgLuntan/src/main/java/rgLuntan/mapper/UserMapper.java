package rgLuntan.mapper;

import rgLuntan.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

 
@Mapper
public interface UserMapper extends BaseMapper<User> {
    int countToday();
}
