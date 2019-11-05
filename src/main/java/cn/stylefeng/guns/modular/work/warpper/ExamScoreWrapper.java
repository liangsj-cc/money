package cn.stylefeng.guns.modular.work.warpper;

import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.util.DecimalUtil;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public class ExamScoreWrapper extends BaseControllerWrapper {
    public ExamScoreWrapper(Map<String, Object> single) {
        super(single);
    }

    public ExamScoreWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public ExamScoreWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public ExamScoreWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }
    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        String examType = "";
        if(map.get("exam_type").equals("0")){
            examType = "每日一练";
        }else{
            examType = "月考";
        }
        map.put("exam_type", examType);

    }
}
