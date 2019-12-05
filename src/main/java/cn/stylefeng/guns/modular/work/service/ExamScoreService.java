package cn.stylefeng.guns.modular.work.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.work.entity.NoExamUser;
import cn.stylefeng.guns.modular.work.mapper.ExamScoreMapper;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExamScoreService {

    @Autowired
    ExamScoreMapper examScoreMapper;

    public Page<Map<String, Object>> selectUserScore(DataScope dataScope, String name, String beginTime, String endTime, Long deptId,String examType) {
        Page page = LayuiPageFactory.defaultPage();
        return examScoreMapper.selectUserScore(page, dataScope, name, beginTime, endTime, deptId,examType);
    }

    public Page<Map<String, Object>> selectNoExamUser(DataScope dataScope,String name, String beginTime,  Long deptId, String examType) throws ParseException {
        Page page = LayuiPageFactory.defaultPage();
        //String monday = getMonday(beginTime);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       // Date d = sdf.parse(monday);
       // Calendar cal = Calendar.getInstance();
      //  cal.setTime(d);
       // List<NoExamUser> list = new ArrayList<>();
//        List<NoExamUser> list1 = new ArrayList<>();
//        for (int i = 1; i <= 7; i++) {
//            cal.add(Calendar.DAY_OF_WEEK,1);
//            String times = sdf.format(cal.getTime());
        //list =
            //for (NoExamUser no :list1) {
               // list.add(no);
           // }
        //}

//        page.setTotal(list.size());
//        page.setRecords(list);
        return examScoreMapper.selectNoExamUser(page,dataScope,name,beginTime,deptId,examType);
    }

    public static String  getMonday(String beginTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(beginTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        // System.out.println("所在周星期一的日期：" + imptimeBegin);
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        // System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeBegin;
    }

    public static void main(String[] args) throws ParseException {
//        System.out.println(getMonday("2019-12-11 00:00:00"));
        String monday = getMonday("2019-12-11 00:00:00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(monday);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        for (int i = 1; i <= 7; i++) {
            cal.add(Calendar.DAY_OF_WEEK,1);
            sdf.format(cal.getTime());
        }

    }
}
