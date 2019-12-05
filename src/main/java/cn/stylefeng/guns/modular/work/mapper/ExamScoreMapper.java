package cn.stylefeng.guns.modular.work.mapper;

import cn.stylefeng.guns.modular.work.entity.NoExamUser;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
public interface ExamScoreMapper {

    /**
     * 根据条件查询用户列表
     */
    Page<Map<String, Object>> selectUserScore(@Param("page") Page page, @Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptId") Long deptId, @Param("examType")String examType);

    /**
     * 查询未考试人员
     */
    Page<Map<String, Object>> selectNoExamUser(@Param("page") Page page, @Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("deptId") Long deptId, @Param("examType") String examType);
}
