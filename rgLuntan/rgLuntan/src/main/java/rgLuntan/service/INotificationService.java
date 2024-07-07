package rgLuntan.service;

import java.util.List;
import java.util.Map;

 
public interface INotificationService {
    // 查询消息
    List<Map<String, Object>> selectByUserId(Integer userId, Boolean read, Integer limit);

    void markRead(Integer userId);

    // 查询未读消息数量
    long countNotRead(Integer userId);

    void deleteByTopicId(Integer topicId);

    void deleteByUserId(Integer userId);

    void insert(Integer userId, Integer targetUserId, Integer topicId, String action, String content);
}
