package cn.stylefeng.guns.modular.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exam_history")
public class ExamHistoryController {
    private String PREFIX = "/modular/work/exam_history/";

    @RequestMapping("")
    public String index() {
        return PREFIX + "index.html";
    }


}
