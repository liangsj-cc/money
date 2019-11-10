package cn.stylefeng.guns.modular.work.controller;

import cn.stylefeng.guns.modular.work.entity.TopList;
import cn.stylefeng.guns.modular.work.service.ExamService;
import lombok.experimental.PackagePrivate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/exam_history")
public class ExamHistoryController {
    @Autowired
    private ExamService examService;

    private String PREFIX = "/modular/work/exam_history/";
    @RequestMapping("")
    public String index() {
        return PREFIX + "index.html";
    }

    /**
     * 日考TOP10
     * @return
     */
    @GetMapping("/dayTop")
    public String dayTop(Model model) {
        List<TopList> list = examService.getDayTop();
        model.addAttribute("list",list);
        return PREFIX + "daytop.html";
    }

    @GetMapping("/monthTop")
    public String getMonthTop(Model model){
        List<TopList> list = examService.getMonthTop();
        model.addAttribute("list",list);
        return PREFIX + "monthtop.html";
    }

}
