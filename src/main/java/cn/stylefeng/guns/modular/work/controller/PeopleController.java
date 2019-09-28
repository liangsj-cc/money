package cn.stylefeng.guns.modular.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 人员控制器
 *
 * @author liangsj
 * @Date 2019年9月28日20:27:22
 */
@Controller
@RequestMapping("/people")
public class PeopleController {

    private String PREFIX = "/modular/work/people/";

    /**
     * 跳转到人员管理首页
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "people.html";
    }
}
