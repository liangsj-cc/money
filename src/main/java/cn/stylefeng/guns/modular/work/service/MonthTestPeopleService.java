package cn.stylefeng.guns.modular.work.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.work.entity.Exam;
import cn.stylefeng.guns.modular.work.entity.MonthTestPeople;
import cn.stylefeng.guns.modular.work.mapper.ExamMapper;
import cn.stylefeng.guns.modular.work.mapper.MonthTestPeopleMapper;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MonthTestPeopleService extends ServiceImpl<MonthTestPeopleMapper, MonthTestPeople> {

    /**
     * 根据条件查询月考列表
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:45
     */
    public Page<Map<String, Object>> selectMonthTestPeople(DataScope dataScope, String name, String poepleType,String peopleDept, String month) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectMonthPeople(page, dataScope, name, poepleType, peopleDept, month);
    }


    @Transactional
    public Map<String, Object> importPeople(MultipartFile file) {
        Map<String,Object> map = new HashMap<>();
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
             workbook = new HSSFWorkbook(file.getInputStream());
        }catch (Exception e){

        }
            HSSFSheet sheet0 = workbook.getSheetAt(0);
            HSSFRow row0 = sheet0.getRow(0);
            sheet0.removeRow(row0);
            for (Row row : sheet0) {
                String peopleIdentityId = row.getCell(1).getStringCellValue();
                boolean isTrue = this.selectPeopleIsTrue(peopleIdentityId);
                if (!isTrue) {
                    map.put("code", "ERROR");
                    map.put("peopleName", row.getCell(0).getStringCellValue());
                    return map;
                } else {
                    MonthTestPeople mt = new MonthTestPeople();
                    mt.setPeopleName(row.getCell(0).getStringCellValue());
                    mt.setPeopleIdentify(row.getCell(1).getStringCellValue());
                    Cell cell0 = row.getCell(2);
                    cell0.setCellType(Cell.CELL_TYPE_STRING);
                    String testtimeStr = cell0.getStringCellValue();
                    DateTime dateTime = new DateTime(testtimeStr, "yyyyMMdd");
                    mt.setTestTime(dateTime.toTimestamp());
                    mt.setCreateTime(new Date());
                    this.save(mt);
                }
            }

        map.put("code","SUCCESS");

        return map;
    }

    /**
     * 查询身份证号码是否正确
     */
    public boolean selectPeopleIsTrue(String identityId){
       Integer count = this.baseMapper.selectPeopleIsTrue( identityId);
       if(count>=1){
           return true;
       }else{
           return false;
       }
    }
}

