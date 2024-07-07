package rgLuntan.interceptor;

import rgLuntan.model.User;
import rgLuntan.service.ISystemConfigService;
import rgLuntan.service.IUserService;
import rgLuntan.util.CookieUtil;
import rgLuntan.util.HttpUtil;
import rgLuntan.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

 
@Component
public class CommonInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(CommonInterceptor.class);
    @Resource
    private IUserService userService;
    @Resource
    private CookieUtil cookieUtil;
    @Resource
    private ISystemConfigService systemConfigService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long start = System.currentTimeMillis();
        request.setAttribute("_start", start);

        // 判断session里有用户信息，有直接通过
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("_user");
        if (user == null) {
            // 获取cookie里的token，查询用户的信息并放入session里
            String token = cookieUtil.getCookie(systemConfigService.selectAllConfig().get("cookie_name").toString());
            if (!StringUtils.isEmpty(token)) {
                // 根据token查询用户是否存在
                user = userService.selectByToken(token);
                if (user != null) {
                    // 用户存在写session，cookie然后给予通过
                    session.setAttribute("_user", user);
                    cookieUtil.setCookie(systemConfigService.selectAllConfig().get("cookie_name").toString(), user.getToken());
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (!HttpUtil.isApiRequest(request) && modelAndView != null) {
            // TODO 这地方有安全隐患，通过这个设置，就可以在页面上获取到system_config表里的所有数据了，如果有人恶意往页面里加入一些代码，就可以拿到一些不可告人的东西。。
            // 后面啥时候想起来了，再收拾它
            // 2023/3/14 过滤掉了systemConfig里type为password的数据，应该能提升一丢丢的安全性吧
            modelAndView.addObject("site", systemConfigService.selectAllConfigWithoutPassword());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long start = (long) request.getAttribute("_start");
        String actionName = request.getRequestURI();
        String clientIp = IpUtil.getIpAddr(request);
        StringBuilder logString = new StringBuilder();
        logString.append(clientIp).append("|").append(actionName).append("|");
        Map<String, String[]> params = request.getParameterMap();
        params.forEach((key, value) -> {
            logString.append(key);
            logString.append("=");
            for (String paramString : value) {
                logString.append(paramString);
            }
            logString.append("|");
        });
        long executionTime = System.currentTimeMillis() - start;
        logString.append("excitation=").append(executionTime).append("ms");
        log.info(logString.toString());
    }
}
