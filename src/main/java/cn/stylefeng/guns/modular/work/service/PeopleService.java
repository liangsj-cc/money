package cn.stylefeng.guns.modular.work.service;

import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.entity.User;
import cn.stylefeng.guns.modular.system.factory.UserFactory;
import cn.stylefeng.guns.modular.system.mapper.UserMapper;
import cn.stylefeng.guns.modular.system.model.UserDto;
import cn.stylefeng.guns.modular.work.entity.People;
import cn.stylefeng.guns.modular.work.mapper.PeopleMapper;
import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class PeopleService extends ServiceImpl<PeopleMapper, People> {


    /**
     * 根据条件查询人员列表
     *
     * @author liangsj
     * @Date 2019/9/29
     */
    public Page<Map<String, Object>> selectPeople(DataScope dataScope, String peopleName,String peopleDept,String peopleType ) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectPeople(page, dataScope, peopleName, peopleDept, peopleType);
    }

    public void peopleAdd(People people) {
        // 判断人员是否重复
        People thePeople = this.getByAccount(people.getPeopleIdentify());
        if (thePeople != null) {
            throw new ServiceException(BizExceptionEnum.USER_ALREADY_REG);
        }
        if("".equals(people.getPeopleSex())){
            people.setPeopleSex("男");
        }
        people.setCreateTime(new Date());
        this.save(people);
    }

    private People getByAccount(String peopleIdentify) {
        return this.baseMapper.getByAccount(peopleIdentify);
    }
}
