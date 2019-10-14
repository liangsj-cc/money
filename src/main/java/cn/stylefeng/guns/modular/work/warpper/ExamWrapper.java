package cn.stylefeng.guns.modular.work.warpper;

import cn.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import cn.stylefeng.guns.core.util.DecimalUtil;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public class ExamWrapper extends BaseControllerWrapper {
    public ExamWrapper(Map<String, Object> single) {
        super(single);
    }

    public ExamWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public ExamWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public ExamWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("deptName", ConstantFactory.me().getDeptName(DecimalUtil.getLong(map.get("deptId"))));
    }
}
