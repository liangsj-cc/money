package cn.stylefeng.guns.modular.work.service;

import cn.stylefeng.guns.modular.work.entity.Exercise;
import cn.stylefeng.guns.modular.work.mapper.ExerciseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExerciseService extends ServiceImpl<ExerciseMapper, Exercise> {

    @Resource
    ExerciseMapper exerciseMapper;

    public Page<Exercise> exercisePage(Page page,Map map) {
        return exerciseMapper.selectExercise(page, map);
    }
}
