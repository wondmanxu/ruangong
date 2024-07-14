package rgLuntan.directive;

import rgLuntan.service.ISystemConfigService;
import rgLuntan.service.ITopicService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

 
@Component
public class SearchDirective implements TemplateDirectiveModel {

    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private ITopicService topicService;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody
            templateDirectiveBody) throws TemplateException, IOException {
        Page<Map<String, Object>> page = new Page<>();
        String keyword = String.valueOf(map.get("keyword"));
        String tab = String.valueOf(map.get("tab"));
        String forumsName = String.valueOf(map.get("forumsName"));
        Integer pageNo = Integer.parseInt(map.get("pageNo").toString());

//        if (!StringUtils.isEmpty(forumsName)) {
            Integer pageSize = Integer.parseInt(systemConfigService.selectAllConfig().get("page_size").toString());
            page = topicService.searchBar(pageNo, pageSize, tab, forumsName, keyword);
//        }else if (!StringUtils.isEmpty(keyword)) {
//            Integer pageSize = Integer.parseInt(systemConfigService.selectAllConfig().get("page_size").toString());
//            page = topicService.search(pageNo, pageSize, keyword);
//        }

        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
        environment.setVariable("page", builder.build().wrap(page));
        templateDirectiveBody.render(environment.getOut());
    }
}
