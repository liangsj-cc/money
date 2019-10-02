package cn.stylefeng.guns.modular.work.controller;

import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.work.entity.People;
import cn.stylefeng.guns.modular.work.service.PeopleService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 人员控制器
 *
 * @author liangsj
 * @Date 2019年9月28日20:27:22
 */
@Controller
@RequestMapping("/people")
public class PeopleController extends BaseController {

    @Autowired
    PeopleService peopleService;
    private String PREFIX = "/modular/work/people/";

    /**
     * 跳转到人员管理首页
     *
     * @author liangsj
     * @Date 2019年9月29日08:27:22
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "people.html";
    }

    /**
     * 添加人员页面
     *
     * @author liangsj
     * @Date 2019年9月29日08:27:22
     */
    @RequestMapping("/people_add")
    public String addView() {
        return PREFIX + "people_add.html";
    }


    /**
     * 导入人员页面
     *
     * @author liangsj
     * @Date 2019年9月29日08:27:22
     */
    @RequestMapping("/people_import")
    public String importExcle() {
        return PREFIX + "people_import.html";
    }

    /**
     * 查询人员列表
     *
     * @author liangsj
     * @Date 2019年9月29日08:27:22
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String peopleName,
                       @RequestParam(required = false) String peopleDept,
                       @RequestParam(required = false) String peopleType) {


        if (ShiroKit.isAdmin()) {
            Page<Map<String, Object>> peoples = peopleService.selectPeople(null, peopleName, peopleDept, peopleType);
            //Page wrapped = new UserWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(peoples);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            Page<Map<String, Object>> peoples = peopleService.selectPeople(null, peopleName, peopleDept, peopleType);
            //Page wrapped = new UserWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(peoples);
        }

    }

    /**
     * 添加单个人员
     *
     * @author liangsj
     * @Date 2019年9月29日16:07:22
     */
    @RequestMapping("/add")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData add(@Valid People people, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.peopleService.peopleAdd(people);
        return SUCCESS_TIP;
    }

    /**
    * 导出excle人员模板
     *
     * @author liangsj
     * @Date 2019年9月29日16:07:22
     */
    @RequestMapping(value = "/export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Workbook workbook = new HSSFWorkbook();
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-download");


            String filedisplay = "人员导入模板.xls";

            filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename="+ filedisplay);

            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            Sheet sheet = workbook.createSheet("人员导入模板");
            // 第三步，在sheet中添加表头第0行
            Row row = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER); // 创建一个居中格式

            Cell cell = row.createCell(0);
            cell.setCellValue("姓名");
            cell.setCellStyle(style);
            sheet.setColumnWidth(0, (25 * 256));  //设置列宽，50个字符宽

            cell = row.createCell(1);
            cell.setCellValue("身份证号");
            cell.setCellStyle(style);
            sheet.setColumnWidth(1, (20 * 256));  //设置列宽，50个字符宽

            cell = row.createCell(2);
            cell.setCellValue("性别");
            cell.setCellStyle(style);
            sheet.setColumnWidth(2, (15 * 256));  //设置列宽，50个字符宽

            cell = row.createCell(3);
            cell.setCellValue("部门名称");
            cell.setCellStyle(style);
            sheet.setColumnWidth(3, (15 * 256));  //设置列宽，50个字符宽

            cell = row.createCell(4);
            cell.setCellValue("工种类型");
            cell.setCellStyle(style);
            sheet.setColumnWidth(4, (15 * 256));  //设置列宽，50个字符宽

            // 第五步，写入实体数据 实际应用中这些数据从数据库得到
            row = sheet.createRow(1);
            row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("");
            // 第六步，将文件存到指定位置
            try
            {
                OutputStream out = response.getOutputStream();
                workbook.write(out);
                out.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        } catch (Exception ex) {

        }
    }

    /**
     *  删除人员
     * @param peopleId
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Long peopleId) {
        this.peopleService.removeById(peopleId);
        return SUCCESS_TIP;
    }

    /**
     *导入人员
     */
    @RequestMapping("/importExcle")
    @ResponseBody
    public Object uploadExcle(@RequestParam MultipartFile file){
        if(file == null){
            return "2001";
        }
        String name = file.getOriginalFilename();
        long size = file.getSize();
        if (name == null || ("").equals(name) && size == 0){
            return "2001";
        }
        try{
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet sheet0=workbook.getSheetAt(0);
            HSSFRow row0 = sheet0.getRow(0);
            sheet0.removeRow(row0);
            JSONObject json = new JSONObject();
            for (Row row : sheet0){
                People people = new People();
                people.setPeopleName(row.getCell(0).getStringCellValue());
                people.setPeopleIdentify(row.getCell(1).getStringCellValue());
                people.setPeopleSex(row.getCell(2).getStringCellValue());
                people.setPeopleDept(row.getCell(3).getStringCellValue());
                people.setPeopleType(row.getCell(4).getStringCellValue());
                //解析成json后添加至数据库
                peopleService.peopleAdd(people);
            }
        }catch (Exception e){

        }
        return SUCCESS_TIP;
    }

}
