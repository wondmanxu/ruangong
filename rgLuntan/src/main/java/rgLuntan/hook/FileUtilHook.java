package rgLuntan.hook;

import org.aspectj.lang.annotation.Pointcut;

public class FileUtilHook {

    @Pointcut("execution(public * rgLuntan.util.FileUtil.upload(..))")
    public void upload() {
    }

}
