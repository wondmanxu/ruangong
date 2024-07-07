package rgLuntan.hook;

import org.aspectj.lang.annotation.Pointcut;

 
public class TopicServiceHook {

    @Pointcut("execution(public * rgLuntan.service.ITopicService.search(..))")
    public void search() {
    }

    @Pointcut("execution(public * rgLuntan.service.ITopicService.selectById(..))")
    public void selectById() {
    }

    @Pointcut("execution(public * rgLuntan.service.ITopicService.update(..))")
    public void update() {
    }

    @Pointcut("execution(public * rgLuntan.service.ITopicService.vote(..))")
    public void vote() {
    }

    @Pointcut("execution(public * rgLuntan.service.ITopicService.updateViewCount(..))")
    public void updateViewCount() {
    }

}
