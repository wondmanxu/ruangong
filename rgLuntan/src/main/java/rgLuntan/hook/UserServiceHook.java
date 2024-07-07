package rgLuntan.hook;

import org.aspectj.lang.annotation.Pointcut;

 
public class UserServiceHook {

    @Pointcut("execution(public * rgLuntan.service.IUserService.selectByUsername(..))")
    public void selectByUsername() {
    }

    @Pointcut("execution(public * rgLuntan.service.IUserService.selectByToken(..))")
    public void selectByToken() {
    }

    @Pointcut("execution(public * rgLuntan.service.IUserService.selectById(..))")
    public void selectById() {
    }

    @Pointcut("execution(public * rgLuntan.service.IUserService.delRedisUser(..))")
    public void delRedisUser() {
    }

}
