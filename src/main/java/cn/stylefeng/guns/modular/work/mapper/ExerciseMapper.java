package cn.stylefeng.guns.modular.work.mapper;

import cn.stylefeng.guns.modular.work.entity.Exercise;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface ExerciseMapper  extends BaseMapper<Exercise> {
    Page<Exercise> selectExercise(@Param("page") Page page, @Param("condition") Map<String, String> condition);
}
