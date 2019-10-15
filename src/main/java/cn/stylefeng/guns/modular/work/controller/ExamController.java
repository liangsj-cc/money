package cn.stylefeng.guns.modular.work.controller;


import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;


import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.work.entity.Exam;
import cn.stylefeng.guns.modular.work.entity.Exercise;
import cn.stylefeng.guns.modular.work.service.ExamService;
import cn.stylefeng.guns.modular.work.service.ExerciseService;
import cn.stylefeng.guns.modular.work.warpper.ExamWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;

import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.validator.constraints.EAN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController {

    private String PREFIX = "/modular/work/exam/";

    @Autowired
    ExamService examService;
    @Autowired
    ExerciseService exerciseService;

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
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String type
    ) {
        LambdaQueryWrapper<Exam> lw = new LambdaQueryWrapper<>();
        if (name != null) {
            lw.like(Exam::getName, name);
        }

        if (StrUtil.isNotBlank(type)) {
            lw.eq(Exam::getType, type);
        }
        IPage<Exam> examPage = examService.page(LayuiPageFactory.defaultPage(), lw);
        examPage.getRecords().forEach(
                item ->
                        item.set("deptName",
                                ConstantFactory.me().getDeptName(item.getDeptId())
                        )
        );
        return LayuiPageFactory.createPageInfo(examPage);
    }


    @GetMapping("/add")
    public String addView() {
        return PREFIX + "exam_add.html";
    }

    @RequestMapping("/add/json")
    @ResponseBody
    public ResponseData addExam(Exam exam) {
        if (examService.deptHasExam(exam.getDeptId(), exam.getType())) {
            throw new ServiceException(BizExceptionEnum.CANT_ADD_EXAM_DEPT_HAS_EXAM);
        }

        examService.save(exam);
        return SUCCESS_TIP;
    }


    @RequestMapping("/edit/json")
    @ResponseBody
    public ResponseData editExam(Exam exam) {
        Exam dbExam = examService.getById(exam.getId());

        if (!(dbExam.getType().equals(exam.getType()) && dbExam.getDeptId().equals(exam.getDeptId()))) {
            // 类型不变不需要判断可以
            if (examService.deptHasExam(exam.getDeptId(), exam.getType())) {
                throw new ServiceException(BizExceptionEnum.CANT_ADD_EXAM_DEPT_HAS_EXAM);
            }
        }
        examService.updateById(exam);

        return SUCCESS_TIP;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        examService.removeById(id);
        return SUCCESS_TIP;
    }

    @GetMapping("/exam_edit")
    public String examEditHtml() {
        return PREFIX + "exam_edit.html";
    }

    @RequestMapping("getExamInfo")
    @ResponseBody
    public Object getExamInfo(Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new RequestEmptyException();
        }

        Exam exam = examService.getById(id);
        exam.set("deptName", ConstantFactory.me().getDeptName(exam.getDeptId()));
        return ResponseData.success(exam);
    }

    @GetMapping("/day")
    public String examDay(Model model) {

        Long deptId = ShiroKit.getUserNotNull().getDeptId();
        Exam exam = examService.getOne(
                new LambdaQueryWrapper<Exam>().eq(Exam::getDeptId, deptId).eq(Exam::getType,
                        "0"));
        Map map = JSONObject.parseObject(exam.getSelector(), Map.class);
        List<Exercise> exercises = exerciseService.selectByUserAndType(map, exam.getNum());

        List<List<String>> exops = exercises.stream().map(e -> JSONArray.parseArray(e.getOptions(), String.class)).collect(Collectors.toList());
        List<Boolean> more =
                exercises
                        .stream()
                        .map(e -> JSONArray.parseArray(e.getRights(), Long.class))
                        .map(rs -> rs.stream().mapToLong(value -> value).sum() > 1)
                        .collect(Collectors.toList());

        model.addAttribute("es", exercises);
        model.addAttribute("exops", exops);
        model.addAttribute("rig", more);
        model.addAttribute("exam",exam);
        return PREFIX + "day.html";
    }

    @GetMapping("/month")
    @ResponseBody
    public Object examMonth() {
        return "montu";
    }

}
