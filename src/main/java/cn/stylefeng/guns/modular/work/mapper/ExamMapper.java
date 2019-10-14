package cn.stylefeng.guns.modular.work.mapper;

import cn.stylefeng.guns.modular.work.entity.Exam;
import cn.stylefeng.guns.modular.work.entity.People;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


public interface ExamMapper extends BaseMapper<Exam> {

    /**
     * 根据人员姓名查询人员列表
     */
    Page<Map<String, Object>> selectExam(@Param("page") Page page, @Param("dataScope") DataScope dataScope, @Param("examName") String examName, @Param("examType") String examType);


    /**
     * 通过账号获取用户
     */
    People getByAccount(@Param("peopleIdentify") String peopleIdentify);
}