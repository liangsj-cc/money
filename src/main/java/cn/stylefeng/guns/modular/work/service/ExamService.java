package cn.stylefeng.guns.modular.work.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.stylefeng.guns.modular.work.entity.Exam;
import cn.stylefeng.guns.modular.work.entity.TopList;
import cn.stylefeng.guns.modular.work.mapper.ExamMapper;
import cn.stylefeng.guns.modular.work.mapper.ExamScoreMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class ExamService extends ServiceImpl<ExamMapper, Exam> {

    public List<Exam> finExamByUserIdAndType(Long userId, String type) {
        return baseMapper.finExamByUserIdAndType(userId, type);
    }

    public List<TopList> getDayTop() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = formatter.format(date);
        Date date1 = DateUtil.parse(time);
        DateTime dateTime = DateUtil.offset(date1, DateField.DAY_OF_MONTH, -1);
        DateTime dateTime1 = DateUtil.offset(date1, DateField.SECOND, -1);
        return baseMapper.getDayTop(dateTime.toString(),dateTime1.toString());
    }

    public List<TopList> getMonthTop() {
        return baseMapper.getMonthTop();
    }

}
