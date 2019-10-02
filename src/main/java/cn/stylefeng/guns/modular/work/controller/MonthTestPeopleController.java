package cn.stylefeng.guns.modular.work.controller;

import cn.stylefeng.guns.modular.work.service.MonthTestPeopleService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/month_test")
@Controller
public class MonthTestPeopleController extends BaseController {

    private String  PREFIX = "/modular/work/month_test/";

    @Autowired
    MonthTestPeopleService monthTestPeopleService;

}
