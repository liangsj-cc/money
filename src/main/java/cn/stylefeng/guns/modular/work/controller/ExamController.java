package cn.stylefeng.guns.modular.work.controller;


import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;

import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.work.entity.Exam;
import cn.stylefeng.guns.modular.work.service.ExamService;
import cn.stylefeng.guns.modular.work.warpper.ExamWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;

import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.validator.constraints.EAN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController {

    private String PREFIX = "/modular/work/exam/";

    @Autowired
    ExamService examService;

    /**
     * 跳转到习题管理首页
     *
     * @author liangsj
     * @Date 2019年10月01日08:27:22
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "exam.html";
    }

    /**
     * 跳转到习题导入页
     *
     * @author liangsj
     * @Date 2019年10月01日08:27:22
     */
    @RequestMapping("exam_add")
    public String examAdd() {
        return PREFIX + "exam_import.html";
    }

    @RequestMapping("list")
    @ResponseBody
    public Object list() {
        IPage<Exam> examPage = examService.page(LayuiPageFactory.defaultPage());
        examPage.getRecords().forEach(
                item ->
                        item.set("deptName",
                                ConstantFactory.me().getDeptName(item.getDeptId())
                        )
        );
        return LayuiPageFactory.createPageInfo(examPage);
    }

    @GetMapping("/day")
    public String examDay() {
        return PREFIX + "day.html";
    }

    @GetMapping("/month")
    @ResponseBody
    public Object examMonth() {
        return "montu";
    }

}
