package cn.stylefeng.guns.modular.work.mapper;


import cn.stylefeng.guns.modular.work.entity.MonthTestPeople;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface MonthTestPeopleMapper extends BaseMapper<MonthTestPeople> {
    Page<Map<String, Object>> selectMonthPeople(@Param("page") Page page, @Param("dataScope")DataScope dataScope, @Param("peopleName")String peopleName,@Param("poepleType") String poepleType, @Param("peopleDept") String peopleDept,@Param("testTime") String testTime);

    Integer selectPeopleIsTrue(@Param("peopleIdentity")String identityId);
}
