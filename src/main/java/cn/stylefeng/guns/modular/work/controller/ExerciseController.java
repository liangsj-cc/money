package cn.stylefeng.guns.modular.work.controller;

import cn.hutool.core.util.StrUtil;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.work.entity.Exercise;
import cn.stylefeng.guns.modular.work.service.ExerciseService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.applet.resources.MsgAppletViewer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exercise")
public class ExerciseController extends BaseController {
    private String PREFIX = "/modular/work/exercise/";

    // 默认的导入模板头部
    private String[] chartDefaultHeader = new String[]{
            "题目", "选项A", "选项B", "选项C", "选项D", "正确答案", "标签名"
    };

    @Autowired
    ExerciseService exerciseService;

    @RequestMapping
    public String index() {
        return PREFIX + "exercise.html";
    }

    @RequestMapping("/exercise_import")
    public String exerciseImport() {
        return PREFIX + "exercise_import.html";
    }

    @ResponseBody
    @GetMapping("/list")
    public Object list(@RequestParam(value = "lables", defaultValue = "{}", required = false) String lables) {

        Map map = Optional.of(JSONObject.parseObject(lables, Map.class)).orElse(new HashMap());
        Page<Exercise> page = exerciseService.exercisePage(LayuiPageFactory.defaultPage(), map);
        // IPage exerciseIPage = exerciseService.page(LayuiPageFactory.defaultPage());
        return LayuiPageFactory.createPageInfo(page);
    }


    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Workbook workbook = new HSSFWorkbook();
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-download");
            String filedisplay = "习题导入模板.xls";

            filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            Sheet sheet = workbook.createSheet("习题导入模板");
            // 第三步，在sheet中添加表头第0行
            Row row = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_CENTER); // 创建一个居中格

            for (int i = 0; i < chartDefaultHeader.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(chartDefaultHeader[i]);
                cell.setCellStyle(style);
                sheet.setColumnWidth(i, (25 * 256));
            }

            // 第六步，将文件存到指定位置
            try {
                OutputStream out = response.getOutputStream();
                workbook.write(out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {

        }
    }

    @RequestMapping("/importExcle")
    @ResponseBody
    @Transactional
    public Object uploadExcle(@RequestParam MultipartFile file) {
        if (file == null) {
            return "2001";
        }
        String name = file.getOriginalFilename();
        long size = file.getSize();
        if (name == null || ("").equals(name) && size == 0) {
            return "2001";
        }


        try {


            Map<Integer, String> opHeaders = new HashMap<>();
            Map<Integer, String> labelsKey = new HashMap<>();

            // 题目位置
            int nameNum = -1;
            // 正确答案位置
            int rightsNum = -1;


            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet sheet0 = workbook.getSheetAt(0);
            HSSFRow row0 = sheet0.getRow(0);


            for (int i = 0; i < row0.getPhysicalNumberOfCells(); i++) {
                String s = row0.getCell(i).getStringCellValue();
                // 题目
                if ("题目".equals(s)) {
                    if (nameNum == -1) {
                        nameNum = i;
                    }
                } else if ("正确答案".equals(s)) {
                    if (rightsNum == -1) {
                        rightsNum = i;
                    }
                } else if (s != null && s.contains("选项")) {
                    opHeaders.put(i, s.replace("选项", ""));
                } else {
                    if(StrUtil.isNotBlank(s)){
                        labelsKey.put(i, s);
                    }
                }
            }
            sheet0.removeRow(row0);
            List<Exercise> exercises = new ArrayList<>();
            for (Row row : sheet0) {

                Exercise exercise = new Exercise();
                exercise.setName(row.getCell(nameNum).getStringCellValue());
                JSONObject labels = new JSONObject();
                labelsKey.forEach((i, item) -> {
                    labels.put(item, row.getCell(i).getStringCellValue());
                });


                exercise.setLabel(labels.toJSONString());

                JSONArray options = new JSONArray();

                opHeaders.forEach((i, item) -> {
                    if(!row.getCell(i).getStringCellValue().isEmpty()){
                        options.add(item + "." + row.getCell(i).getStringCellValue());
                    }
                });
                exercise.setOptions(options.toJSONString());
                JSONArray rights = new JSONArray();

                // 找到所有正确的答案
                List<String> strings = Arrays.stream(row.getCell(rightsNum)
                        .getStringCellValue().split(","))
                        .map(s -> s.replace(" ", ""))
                        .collect(Collectors.toList());
                opHeaders.values().stream()
                        .map(h -> strings.contains(h) ? 1 : 0)
                        .forEach(rights::add);

                if (rights.stream().mapToLong(value -> Long.valueOf(value.toString())).sum() > 0) {
                    exercise.setRights(rights.toJSONString());
                    exercises.add(exercise);
                }
                //解析成json后添加至数据库
            }
            exerciseService.saveBatch(exercises);
        } catch (Exception e) {
            e.printStackTrace();
            return "2001";
        }
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Long execriseId) {
        this.exerciseService.removeById(execriseId);
        return SUCCESS_TIP;
    }
}
