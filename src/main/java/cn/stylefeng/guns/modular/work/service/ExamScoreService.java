package cn.stylefeng.guns.modular.work.service;

import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.modular.work.mapper.ExamScoreMapper;
import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExamScoreService {

    @Autowired
    ExamScoreMapper examScoreMapper;

    public Page<Map<String, Object>> selectUserScore(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return examScoreMapper.selectUserScore(page, dataScope, name, beginTime, endTime, deptId);
    }
}
