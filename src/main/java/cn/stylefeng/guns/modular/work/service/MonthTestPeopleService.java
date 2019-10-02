package cn.stylefeng.guns.modular.work.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.work.entity.Exam;
import cn.stylefeng.guns.modular.work.entity.MonthTestPeople;
import cn.stylefeng.guns.modular.work.mapper.ExamMapper;
import cn.stylefeng.guns.modular.work.mapper.MonthTestPeopleMapper;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MonthTestPeopleService extends ServiceImpl<MonthTestPeopleMapper, MonthTestPeople> {

    /**
     * 根据条件查询月考列表
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:45
     */
    public Page<Map<String, Object>> selectUsers(DataScope dataScope, String name, String poepleType,String peopleDept, String month) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectMonthPeople(page, dataScope, name, poepleType, peopleDept, month);
    }
}
