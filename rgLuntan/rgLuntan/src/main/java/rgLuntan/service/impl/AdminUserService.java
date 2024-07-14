package rgLuntan.service.impl;

import rgLuntan.mapper.AdminUserMapper;
import rgLuntan.model.AdminUser;
import rgLuntan.model.User;
import rgLuntan.service.IAdminUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rgLuntan.service.IUserService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

 
@Service
@Transactional
public class AdminUserService implements IAdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private IUserService userService;

    // 根据用户名查询用户
    @Override
    public AdminUser selectByUsername(String username) {
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AdminUser::getUsername, username);
        return adminUserMapper.selectOne(wrapper);
    }

    // 查询所有的后台用户
    @Override
    public List<Map<String, Object>> selectAll() {
        return adminUserMapper.selectAll();
    }

    @Override
    public void update(AdminUser adminUser) {
        User user = userService.selectByUsername(adminUser.getUsername());
        if (user != null){
            adminUser.setUserId(user.getId());
        }
        adminUserMapper.updateById(adminUser);
    }

    @Override
    public void insert(AdminUser adminUser) {
        User user = userService.selectByUsername(adminUser.getUsername());
        if (user != null){
            adminUser.setUserId(user.getId());
        }

        adminUserMapper.insert(adminUser);
    }

    @Override
    public void delete(Integer id) {
        adminUserMapper.deleteById(id);
    }

    @Override
    public AdminUser selectById(Integer id) {
        return adminUserMapper.selectById(id);
    }

    // 根据角色id查询后台关联的用户
    @Override
    public List<AdminUser> selectByRoleId(Integer roleId) {
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AdminUser::getRoleId, roleId);
        return adminUserMapper.selectList(wrapper);
    }
}
