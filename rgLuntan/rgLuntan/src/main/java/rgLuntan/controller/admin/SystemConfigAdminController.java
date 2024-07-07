package rgLuntan.controller.admin;

import rgLuntan.config.service.TelegramBotService;
import rgLuntan.service.ISystemConfigService;
import rgLuntan.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 
@Controller
@RequestMapping("/admin/system")
public class SystemConfigAdminController extends BaseAdminController {

    private Logger log = LoggerFactory.getLogger(SystemConfigAdminController.class);

    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private TelegramBotService telegramBotService;

    @RequiresPermissions("system:edit")
    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("systems", systemConfigService.selectAll());
        return "admin/system/edit";
    }

    @RequiresPermissions("system:edit")
    @PostMapping("/edit")
    @ResponseBody
    public Result edit(@RequestBody List<Map<String, String>> list) {
        Map<String, String> flattenedMap = new HashMap<>();
        list.forEach(map -> flattenedMap.put(map.get("name"), map.get("value")));
        new Thread(() -> telegramBotService.init().setWebHook(flattenedMap)).start();

        systemConfigService.update(list);
        return success();
    }
}
