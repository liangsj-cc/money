package cn.stylefeng.guns.modular.work.service;

import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.work.entity.Exam;
import cn.stylefeng.guns.modular.work.entity.People;
import cn.stylefeng.guns.modular.work.mapper.ExamMapper;
import cn.stylefeng.guns.modular.work.mapper.PeopleMapper;
import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ExamService extends ServiceImpl<ExamMapper, Exam> {


    /**
     * 根据条件查询习题列表
     *
     * @author liangsj
     * @Date 2019/10/01
     */
    public Page<Map<String, Object>> selectExam(DataScope dataScope, String examName,String examType) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectExam(page, dataScope, examName, examType);
    }
    /**
     * 根据excle导入习题
     *
     * @author liangsj
     * @Date 2019/10/01
     */
    public void examAdd(JSONObject json, String examType) {
        Exam exam = new Exam();
        exam.setCreateTime(new Date());
        exam.setExamName(json.toString());
        exam.setExamType(examType);
        this.save(exam);
    }
}
