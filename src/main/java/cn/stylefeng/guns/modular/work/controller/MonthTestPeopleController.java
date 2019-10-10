package cn.stylefeng.guns.modular.work.controller;

import cn.stylefeng.guns.modular.work.entity.MonthTestPeople;
import cn.stylefeng.guns.modular.work.service.MonthTestPeopleService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/month_test")
@Controller
public class MonthTestPeopleController extends BaseController {

    private String  PREFIX = "/modular/work/month_test/";

    @Autowired
    MonthTestPeopleService monthTestPeopleService;

    /**
     * 首页
     */
    @RequestMapping("")
    public String index(){ return PREFIX+"/month_test_index.html";}

    /**
     * 导入月考人员页面
     *
     * @author liangsj
     * @Date 2019年10月03日09:27:22
     */
    @RequestMapping("/monthtest_import")
    public String importExcle() {
        return PREFIX + "monthtest_import.html";
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
            String filedisplay = "月考人员导入模板.xls";
            filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename="+ filedisplay);

            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            Sheet sheet = workbook.createSheet("月考人员导入模板");
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
            cell.setCellValue("考试时间(例：10月考试：20191001)");
            cell.setCellStyle(style);
            sheet.setColumnWidth(2, (15 * 256));  //设置列宽，50个字符宽

            // 第五步，写入实体数据 实际应用中这些数据从数据库得到
            row = sheet.createRow(1);
            row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("");
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
     * 导入月考人员
     * @param file
     * @return
     */
    @RequestMapping("/importExcle")
    @ResponseBody
    @Transactional
    public Object uploadExcle(@RequestParam MultipartFile file){
        if(file == null){
            return "2001";
        }
        String name = file.getOriginalFilename();
        long size = file.getSize();
        if (name == null || ("").equals(name) && size == 0){
            return "2001";
        }
        Map<String,Object> map = monthTestPeopleService.importPeople(file);
        return map;
    }
}
