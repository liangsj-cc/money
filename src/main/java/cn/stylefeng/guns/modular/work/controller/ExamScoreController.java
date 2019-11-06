package cn.stylefeng.guns.modular.work.controller;

import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.warpper.UserWrapper;
import cn.stylefeng.guns.modular.work.service.ExamScoreService;
import cn.stylefeng.guns.modular.work.service.ExamService;
import cn.stylefeng.guns.modular.work.warpper.ExamScoreWrapper;
import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/exam_score")
public class ExamScoreController {
    private String PREFIX = "/modular/work/exam_score/";

    @Autowired
    ExamScoreService examScoreService;
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


    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String timeLimit,
                       @RequestParam(required = false) Long deptId,
                       @RequestParam(required = false) String examType) {

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
            Page<Map<String, Object>> users = examScoreService.selectUserScore(null, name, beginTime, endTime, deptId,examType);
            Page wrapped = new ExamScoreWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);

    }


}
