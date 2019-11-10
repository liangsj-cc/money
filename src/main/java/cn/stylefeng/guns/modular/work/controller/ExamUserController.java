package cn.stylefeng.guns.modular.work.controller;

import cn.stylefeng.guns.modular.work.entity.ExamUser;
import cn.stylefeng.guns.modular.work.service.ExamUserService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("exam_user")
public class ExamUserController extends BaseController {
    @Autowired
    ExamUserService examUserService;

    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public Object add(String examIds, String userIds) {
        List<Long> uIds = JSONArray.parseArray(userIds, Long.class);
        List<Long> eIds = JSONArray.parseArray(examIds, Long.class);
        examUserService.remove(new LambdaQueryWrapper<ExamUser>().in(
                ExamUser::getUserId, uIds
        ));
        if(uIds==null|| eIds==null){
            return SUCCESS_TIP;
        }
        List<ExamUser> examUsers = uIds.stream().map(uid ->
                eIds.stream().map(eid -> new ExamUser(uid, eid)).collect(Collectors.toList())

        ).reduce((a, b) -> {
            a.addAll(b);
            return a;
        }).orElse(new ArrayList<>());
        examUserService.saveBatch(examUsers);
        return SUCCESS_TIP;
    }
}
