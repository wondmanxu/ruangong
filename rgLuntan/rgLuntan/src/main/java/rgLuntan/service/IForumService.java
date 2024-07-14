package rgLuntan.service;

import rgLuntan.model.Forum;
import rgLuntan.util.MyPage;

import java.util.List;
import java.util.Map;


public interface IForumService {

    List<String> getAllForums();
    Integer getForumIdByName(String forumsName);
}
