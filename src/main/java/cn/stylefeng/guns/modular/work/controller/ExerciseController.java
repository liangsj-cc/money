package cn.stylefeng.guns.modular.work.controller;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.work.entity.Exercise;
import cn.stylefeng.guns.modular.work.service.ExerciseService;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/exercise")
public class ExerciseController {
    private String PREFIX = "/modular/work/exercise/";

    @Autowired
    ExerciseService exerciseService;

    @RequestMapping
    public String index() {
        return PREFIX + "exercise.html";
    }


    @ResponseBody
    @GetMapping("/list")
    public Object list(@RequestParam(value = "lables", defaultValue = "{}", required = false) String lables) {

        Map map = Optional.of(JSONObject.parseObject(lables, Map.class)).orElse(new HashMap());
        Page<Exercise> page = exerciseService.exercisePage(LayuiPageFactory.defaultPage(), map);
        // IPage exerciseIPage = exerciseService.page(LayuiPageFactory.defaultPage());
        return LayuiPageFactory.createPageInfo(page);
    }
}
