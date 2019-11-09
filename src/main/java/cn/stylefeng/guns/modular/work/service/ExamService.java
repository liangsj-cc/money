package cn.stylefeng.guns.modular.work.service;

import cn.stylefeng.guns.modular.work.entity.Exam;
import cn.stylefeng.guns.modular.work.mapper.ExamMapper;
import cn.stylefeng.guns.modular.work.mapper.ExamScoreMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ExamService extends ServiceImpl<ExamMapper, Exam> {

    public List<Exam> finExamByUserIdAndType(Long userId, String type) {
        return baseMapper.finExamByUserIdAndType(userId, type);
    }
}
