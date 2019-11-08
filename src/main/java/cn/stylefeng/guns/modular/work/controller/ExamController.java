package cn.stylefeng.guns.modular.work.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;


import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.core.shiro.ShiroUser;
import cn.stylefeng.guns.modular.work.entity.Exam;
import cn.stylefeng.guns.modular.work.entity.ExamHistory;
import cn.stylefeng.guns.modular.work.entity.Exercise;
import cn.stylefeng.guns.modular.work.service.ExamHistoryService;
import cn.stylefeng.guns.modular.work.service.ExamService;
import cn.stylefeng.guns.modular.work.service.ExerciseService;
import cn.stylefeng.guns.modular.work.warpper.ExamWrapper;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;

import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController {

    private String PREFIX = "/modular/work/exam/";

    @Autowired
    ExamService examService;
    @Autowired
    ExerciseService exerciseService;

    @Autowired
    ExamHistoryService examHistoryService;

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
        ShiroUser shiroUser = ShiroKit.getUserNotNull();
        Long deptId = shiroUser.getDeptId();
        Long userId = shiroUser.getId();

        List<ExamHistory> examHistoryList = examHistoryService.list(new LambdaQueryWrapper<ExamHistory>()
                .eq(ExamHistory::getDeptId, deptId)
                .eq(ExamHistory::getUserId, userId)
                .eq(ExamHistory::getType, "0")
                .gt(ExamHistory::getCreateTime, DateUtil.beginOfDay(new Date()))
                .le(ExamHistory::getCreateTime, DateUtil.endOfDay(new Date()))
        );
        if (examHistoryList.size() == 1) {
            String examJson = examHistoryList.get(0).getExamJson();
            Map map = JSONObject.parseObject(examJson, Map.class);
            model.addAllAttributes(map);
            model.addAttribute("examHistoryId", examHistoryList.get(0).getId());
            String answer = examHistoryList.get(0).getAnswer();
            model.addAttribute("answer", answer == null ? null : answer.replace("\"", "\'")
            );
            if (answer != null) {
                model.addAttribute("score", examHistoryList.get(0).getScore());
                List<Exercise> exercises = JSONArray.parseArray(map.get("es").toString(), Exercise.class);
                List<String> answerRight = exercises.stream().map(item -> {
                    List<String> opts = JSONArray.parseArray(item.getOptions(), String.class);
                    List<Integer> rights = JSONArray.parseArray(item.getRights(), Integer.class);
                    List<String> rightOptsH = new ArrayList<>();
                    for (int i = 0; i < rights.size(); i++) {
                        if (rights.get(i) == 1) {
                            rightOptsH.add((opts.get(i).split("\\."))[0]);
                        }
                    }
                    return rightOptsH.stream().reduce((a, b) -> a + "," + b).orElse("");
                }).collect(Collectors.toList());
                model.addAttribute("answerRight", answerRight);
                model.addAttribute("rightIds",
                        JSONArray
                                .parseArray(
                                        examHistoryList.get(0).getRightIds(), String.class));
            } else {
                model.addAttribute("score", null);
                model.addAttribute("answerRight", null);
                model.addAttribute("rightIds", null);
            }

            return PREFIX + "day.html";
        }

        Exam exam = examService.getOne(
                new LambdaQueryWrapper<Exam>().eq(Exam::getDeptId, deptId).eq(Exam::getType,
                        "0"));
        if (exam == null) {
            return PREFIX + "noexam.html";
        }
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
        model.addAttribute("exam", exam);
        model.addAttribute("answer", null);
        model.addAttribute("answerRight", null);
        model.addAttribute("score", null);
        model.addAttribute("rightIds", null);
        ExamHistory examHistory = new ExamHistory();
        examHistory.setDeptId(deptId);
        examHistory.setUserId(userId);
        // 0 日考
        examHistory.setType("0");
        examHistory.setExamJson(JSONObject.toJSONString(model.asMap()));
        examHistoryService.save(examHistory);
        model.addAttribute("examHistoryId", examHistory.getId());
        return PREFIX + "day.html";
    }


    @RequestMapping("/comp/{examHistoryId}")
    @ResponseBody
    public Object comp(@PathVariable("examHistoryId") String id, String answer, String rs) {
        ExamHistory examHistory = examHistoryService.getById(id);
        if (examHistory.getAnswer() != null) {
            throw new ServiceException(BizExceptionEnum.EXAM_COMP);
        }
        Map<String, Object> rightMap = JSONObject.parseObject(rs, Map.class);

        int rightNum = 0;
        List<String> rightIds = new ArrayList<>();
        for (Map.Entry<String, Object> entry : rightMap.entrySet()) {
            String exerciseId = entry.getKey();
            Object value = entry.getValue();
            String right = exerciseService.getById(exerciseId).getRights();
            List<Integer> intRightList = JSONArray.parseArray(right, Integer.class);
            if (value instanceof JSONArray) {
                JSONArray jsonValue = (JSONArray) value;
                boolean isRight = jsonValue
                        .stream()
                        .mapToInt(i -> Integer.valueOf(i.toString()))
                        .allMatch(i -> intRightList.get(i) == 1)
                        &&
                        intRightList.stream().filter(i -> i == 1).count() == jsonValue.size();
                if (isRight) {
                    rightNum++;
                    rightIds.add(exerciseId);
                }

            } else {
                Integer integerValue = Integer.valueOf(value.toString());
                if (intRightList.get(integerValue) == 1) {
                    rightNum++;
                    rightIds.add(exerciseId);
                }
            }
        }

        examHistory.setAnswer(answer);
        examHistory.setScore((long) Math.round(100 * rightNum / rightMap.size()));
        examHistory.setRightIds(JSONArray.toJSONString(rightIds));
        examHistoryService.updateById(examHistory);
        return SUCCESS_TIP;
    }


    @GetMapping("/month")
    public Object examMonth(Model model) {
        ShiroUser shiroUser = ShiroKit.getUserNotNull();
        Long deptId = shiroUser.getDeptId();
        Long userId = shiroUser.getId();

        List<ExamHistory> examHistoryList = examHistoryService.list(new LambdaQueryWrapper<ExamHistory>()
                .eq(ExamHistory::getDeptId, deptId)
                .eq(ExamHistory::getUserId, userId)
                .eq(ExamHistory::getType, "1")
                .gt(ExamHistory::getCreateTime, DateUtil.beginOfMonth(new Date()))
                .le(ExamHistory::getCreateTime, DateUtil.endOfMonth(new Date()))
        );
        if (examHistoryList.size() == 1) {

            String examJson = examHistoryList.get(0).getExamJson();
            Map map = JSONObject.parseObject(examJson, Map.class);
            model.addAllAttributes(map);
            model.addAttribute("examHistoryId", examHistoryList.get(0).getId());
            String answer = examHistoryList.get(0).getAnswer();
            model.addAttribute("answer", answer == null ? null : answer.replace("\"", "\'")
            );
            if (answer != null) {
                model.addAttribute("score", examHistoryList.get(0).getScore());
                List<Exercise> exercises = JSONArray.parseArray(map.get("es").toString(), Exercise.class);
                List<String> answerRight = exercises.stream().map(item -> {
                    List<String> opts = JSONArray.parseArray(item.getOptions(), String.class);
                    List<Integer> rights = JSONArray.parseArray(item.getRights(), Integer.class);
                    List<String> rightOptsH = new ArrayList<>();
                    for (int i = 0; i < rights.size(); i++) {
                        if (rights.get(i) == 1) {
                            rightOptsH.add((opts.get(i).split("\\."))[0]);
                        }
                    }
                    return rightOptsH.stream().reduce((a, b) -> a + "," + b).orElse("");
                }).collect(Collectors.toList());
                model.addAttribute("answerRight", answerRight);
                model.addAttribute("rightIds",
                        JSONArray
                                .parseArray(
                                        examHistoryList.get(0).getRightIds(), String.class));
            } else {
                model.addAttribute("score", null);
                model.addAttribute("answerRight", null);
                model.addAttribute("rightIds", null);
            }

            return PREFIX + "month.html";
        }

        Exam exam = examService.getOne(
                new LambdaQueryWrapper<Exam>().eq(Exam::getDeptId, deptId).eq(Exam::getType,
                        "1"));
        if (exam == null) {
            return PREFIX + "noexam.html";
        }
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
        model.addAttribute("exam", exam);
        model.addAttribute("answer", null);
        model.addAttribute("answerRight", null);
        model.addAttribute("score", null);
        model.addAttribute("rightIds", null);


        ExamHistory examHistory = new ExamHistory();
        examHistory.setDeptId(deptId);
        examHistory.setUserId(userId);
        // 0 月考
        examHistory.setType("1");
        examHistory.setExamJson(JSONObject.toJSONString(model.asMap()));
        examHistoryService.save(examHistory);
        model.addAttribute("examHistoryId", examHistory.getId());
        return PREFIX + "month.html";
    }


}
