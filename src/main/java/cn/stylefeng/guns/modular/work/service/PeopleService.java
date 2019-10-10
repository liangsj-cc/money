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
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
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

    public Map<String, Object> peopleAdd(People people) {
        // 判断人员是否重复
        People thePeople = this.getByAccount(people.getPeopleIdentify());
        if (thePeople != null) {
            Map<String,Object> map = new HashMap<>();
            map.put("code","ERROR");
            map.put("peopleName",people.getPeopleName());
            return map;
        }
        if("".equals(people.getPeopleSex())){
            people.setPeopleSex("男");
        }
        people.setCreateTime(new Date());
        this.save(people);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("code","OK");
        return map1;
    }

    private People getByAccount(String peopleIdentify) {
        return this.baseMapper.getByAccount(peopleIdentify);
    }

    @Transactional
    public Map<String,Object> importExcle(MultipartFile file) {
        Map<String ,Object> map = new HashMap<>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet sheet0 = workbook.getSheetAt(0);
            HSSFRow row0 = sheet0.getRow(0);
            sheet0.removeRow(row0);
            JSONObject json = new JSONObject();
            for (Row row : sheet0) {
                People people = new People();
                people.setPeopleName(row.getCell(0).getStringCellValue());
                people.setPeopleIdentify(row.getCell(1).getStringCellValue());
                people.setPeopleSex(row.getCell(2).getStringCellValue());
                people.setPeopleDept(row.getCell(3).getStringCellValue());
                people.setPeopleType(row.getCell(4).getStringCellValue());
                //解析成json后添加至数据库
                map =  this.peopleAdd(people);
               if("ERROR".equals(map.get("code"))){
                    return map;
               }
            }
        }catch (Exception e){
            throw new ServiceException(BizExceptionEnum.USER_ALREADY_REG);
        }
        return map;
    }
}
