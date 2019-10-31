package cn.stylefeng.guns.modular.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exam_score")
public class ExamScoreController {
    private String PREFIX = "/modular/work/exam_score/";

    /**
     * 跳转到习题管理首页
     *
     * @author liangsj
     * @Date 2019年10月01日08:27:22
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "exam_score.html";
    }
}
