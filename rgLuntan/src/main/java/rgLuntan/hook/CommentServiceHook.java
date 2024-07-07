package rgLuntan.hook;

import org.aspectj.lang.annotation.Pointcut;

 
public class CommentServiceHook {

    @Pointcut("execution(public * rgLuntan.service.ICommentService.selectByTopicId(..))")
    public void selectByTopicId() {
    }

    @Pointcut("execution(public * rgLuntan.service.ICommentService.insert(..))")
    public void insert() {
    }

    @Pointcut("execution(public * rgLuntan.service.ICommentService.update(..))")
    public void update() {
    }

    @Pointcut("execution(public * rgLuntan.service.ICommentService.vote(..))")
    public void vote() {
    }

    @Pointcut("execution(public * rgLuntan.service.ICommentService.delete(..))")
    public void delete() {
    }

}
