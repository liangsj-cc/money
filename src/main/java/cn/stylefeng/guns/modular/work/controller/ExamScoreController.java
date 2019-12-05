package cn.stylefeng.guns.modular.work.controller;

import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.system.warpper.UserWrapper;
import cn.stylefeng.guns.modular.work.entity.NoExamUser;
import cn.stylefeng.guns.modular.work.service.ExamScoreService;
import cn.stylefeng.guns.modular.work.service.ExamService;
import cn.stylefeng.guns.modular.work.warpper.ExamScoreWrapper;
import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/exam_score")
public class ExamScoreController {
    private String PREFIX = "/modular/work/exam_score/";

    @Autowired
    ExamScoreService examScoreService;
    /**
     * 跳转到习题管理首页
     *
     * @author liangsj
     * @Date 2019年10月01日08:27:22
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "exam_score.html";
    }


    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String timeLimit,
                       @RequestParam(required = false) Long deptId,
                       @RequestParam(required = false) String examType,
                       @RequestParam(required = false) String isexam) {

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }
        Page<Map<String, Object>> users = null;
        if("1".equals(isexam)||isexam==null){
            users = examScoreService.selectUserScore(null, name, beginTime, endTime, deptId,examType);
        }

        Page wrapped = new ExamScoreWrapper(users).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);

    }
    /**
     * 为考试人员管理
     */
     @RequestMapping("/noexam")
     public String no_Exam(){
         return "/modular/work/no_exam/no_exam.html";
     }

    /**
     * @param name
     * @param deptId
     * @param examType
     * @return
     */
    @RequestMapping("/noexamlist")
    @ResponseBody
    public Object noexamlist(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) Long deptId,
                       @RequestParam(required = false) String examType) throws ParseException {
        if(examType==null){
            examType="0";
        }
        if(beginTime==null){
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            beginTime = sm.format(new Date());
        }
        Page<Map<String, Object>>  users = examScoreService.selectNoExamUser(null, name, beginTime, deptId, examType);
        return LayuiPageFactory.createPageInfo(users);
    }

//    @RequestMapping(value = "/export")
//    @ResponseBody
//    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        try {
//            Workbook workbook = new HSSFWorkbook();
//            request.setCharacterEncoding("UTF-8");
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/x-download");
//
//
//            String filedisplay = ".xls";
//
//            filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
//            response.addHeader("Content-Disposition", "attachment;filename="+ filedisplay);
//
//            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
//            Sheet sheet = workbook.createSheet("人员导入模板");
//            // 第三步，在sheet中添加表头第0行
//            Row row = sheet.createRow(0);
//            // 第四步，创建单元格，并设置值表头 设置表头居中
//            CellStyle style = workbook.createCellStyle();
//            style.setAlignment(CellStyle.ALIGN_CENTER); // 创建一个居中格式
//
//            Cell cell = row.createCell(0);
//            cell.setCellValue("姓名");
//            cell.setCellStyle(style);
//            sheet.setColumnWidth(0, (25 * 256));  //设置列宽，50个字符宽
//
//            cell = row.createCell(1);
//            cell.setCellValue("身份证号");
//            cell.setCellStyle(style);
//            sheet.setColumnWidth(1, (20 * 256));  //设置列宽，50个字符宽
//
//            cell = row.createCell(2);
//            cell.setCellValue("性别");
//            cell.setCellStyle(style);
//            sheet.setColumnWidth(2, (15 * 256));  //设置列宽，50个字符宽
//
//            cell = row.createCell(3);
//            cell.setCellValue("部门名称");
//            cell.setCellStyle(style);
//            sheet.setColumnWidth(3, (15 * 256));  //设置列宽，50个字符宽
//
//            cell = row.createCell(4);
//            cell.setCellValue("工种类型");
//            cell.setCellStyle(style);
//            sheet.setColumnWidth(4, (15 * 256));  //设置列宽，50个字符宽
//
//            // 第五步，写入实体数据 实际应用中这些数据从数据库得到
//            row = sheet.createRow(1);
//            row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("");
//            row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("");
//            row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("");
//            row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("");
//            row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("");
//            // 第六步，将文件存到指定位置
//            try
//            {
//                OutputStream out = response.getOutputStream();
//                workbook.write(out);
//                out.close();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//        } catch (Exception ex) {
//
//        }
//    }
}
