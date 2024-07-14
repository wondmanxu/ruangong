package rgLuntan.controller.front;

import rgLuntan.model.User;
import rgLuntan.service.IForumService;
import rgLuntan.service.ISystemConfigService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.List;

 
public class BaseController {

    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private IForumService forumService;

    protected String redirect(String path) {
        return "redirect:" + path;
    }

    protected User getUser() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        HttpSession session = request.getSession();
        return (User) session.getAttribute("_user");
    }

    // 只针对前台页面的模板路径渲染，后台不变
    protected String render(String path) {
        return String.format("theme/%s/%s", systemConfigService.selectAllConfig().get("theme").toString(), path);
    }


    @ModelAttribute("forumsNames")
    public List<String> populateForumsNames() {
        return forumService.getAllForums();
    }

    @ModelAttribute
    public void addAttributes(@RequestParam(required = false) String forumsName,
                              @RequestParam(required = false) String keyword,
                              Model model) {
        model.addAttribute("forumsName", forumsName);
        model.addAttribute("keyword", keyword);
    }

}
