package samson.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 96428 on 2017/7/21.
 * This in ssmjavaconfig, samson.common.controller
 */
@RestController
public class BaseController {

    @RequestMapping("/index.do")
    public String getIndex() {
        return "Hello SSM on javaconfig";
    }
}
