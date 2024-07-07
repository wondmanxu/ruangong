package rgLuntan.hook;

import org.aspectj.lang.annotation.Pointcut;

 
public class IndexedServiceHook {

    @Pointcut("execution(public * rgLuntan.service.IIndexedService.indexAllTopic(..))")
    public void indexAllTopic() {
    }

    @Pointcut("execution(public * rgLuntan.service.IIndexedService.indexTopic(..))")
    public void indexTopic() {
    }

    @Pointcut("execution(public * rgLuntan.service.IIndexedService.deleteTopicIndex(..))")
    public void deleteTopicIndex() {
    }

    @Pointcut("execution(public * rgLuntan.service.IIndexedService.batchDeleteIndex(..))")
    public void batchDeleteIndex() {
    }

}
