package cn.stylefeng.guns.modular.work.controller;


import cn.stylefeng.guns.core.common.page.LayuiPageFactory;

import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.work.service.ExamService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController {

    private String PREFIX = "/modular/work/exam/";

    @Autowired
    ExamService examService;

    /**
     * 跳转到习题管理首页
     *
     * @author liangsj
     * @Date 2019年10月01日08:27:22
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "exam.html";
    }

    /**
     * 跳转到习题导入页
     *
     * @author liangsj
     * @Date 2019年10月01日08:27:22
     */
    @RequestMapping("exam_add")
    public String examAdd() {
        return PREFIX + "exam_import.html";
    }

    /**
     * 查询人员列表
     *
     * @author liangsj
     * @Date 2019年10月02日08:27:22
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String examName,
                       @RequestParam(required = false) String examType) {


        if (ShiroKit.isAdmin()) {
            Page<Map<String, Object>> peoples = examService.selectExam(null, examName, examType);

            return LayuiPageFactory.createPageInfo(peoples);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            Page<Map<String, Object>> peoples = examService.selectExam(null, examName, examType);

            return LayuiPageFactory.createPageInfo(peoples);
        }

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


            String filedisplay = "习题导入模板.xls";

            filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename="+ filedisplay);

            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            Sheet sheet = workbook.createSheet("习题导入模板");
            // 第三步，在sheet中添加表头第0行
            Row row = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER); // 创建一个居中格式

            Cell cell = row.createCell(0);
            cell.setCellValue("题目");
            cell.setCellStyle(style);
            sheet.setColumnWidth(0, (25 * 256));  //设置列宽，50个字符宽

            cell = row.createCell(1);
            cell.setCellValue("选项A");
            cell.setCellStyle(style);
            sheet.setColumnWidth(1, (20 * 256));  //设置列宽，50个字符宽

            cell = row.createCell(2);
            cell.setCellValue("选项B");
            cell.setCellStyle(style);
            sheet.setColumnWidth(2, (15 * 256));  //设置列宽，50个字符宽

            cell = row.createCell(3);
            cell.setCellValue("选项C");
            cell.setCellStyle(style);
            sheet.setColumnWidth(3, (15 * 256));  //设置列宽，50个字符宽

            cell = row.createCell(4);
            cell.setCellValue("选项D");
            cell.setCellStyle(style);
            sheet.setColumnWidth(4, (15 * 256));  //设置列宽，50个字符宽

            cell = row.createCell(5);
            cell.setCellValue("正确答案");
            cell.setCellStyle(style);
            sheet.setColumnWidth(5, (15 * 256));  //设置列宽，50个字符宽

            cell = row.createCell(6);
            cell.setCellValue("题目类型");
            cell.setCellStyle(style);
            sheet.setColumnWidth(6, (15 * 256));  //设置列宽，50个字符宽

            // 第五步，写入实体数据 实际应用中这些数据从数据库得到
            row = sheet.createRow(1);
            row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("");
            row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("");
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
     *导入习题
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
        try{
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet sheet0=workbook.getSheetAt(0);
            HSSFRow row0 = sheet0.getRow(0);
            sheet0.removeRow(row0);
            JSONObject json = new JSONObject();
            for (Row row : sheet0){
                String name1 = row.getCell(0).getStringCellValue();
                json.put("name",name1);
                List<String> list = new ArrayList<>();
                list.add("A."+row.getCell(1).getStringCellValue());
                list.add("B."+row.getCell(2).getStringCellValue());
                list.add("C."+row.getCell(3).getStringCellValue());
                list.add("D."+row.getCell(4).getStringCellValue());

                json.put("option",list);
                String answer = row.getCell(5).getStringCellValue();
                Integer[] intArray0 = {1,1,1,1};
                if("A".equals(answer.trim())){
                    intArray0[0]=0;
                }
                if("B".equals(answer.trim())){
                    intArray0[1]=0;
                }
                if("C".equals(answer.trim())){
                    intArray0[2]=0;
                }
                if("D".equals(answer.trim())){
                    intArray0[3]=0;
                }
                json.put("rights",intArray0);
                //解析成json后添加至数据库
                examService.examAdd(json,  row.getCell(6).getStringCellValue());
            }
        }catch (Exception e){

        }
        return SUCCESS_TIP;
    }

    /**
     *
     * @param examId
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Long examId) {
        this.examService.removeById(examId);
        return SUCCESS_TIP;
    }

}
