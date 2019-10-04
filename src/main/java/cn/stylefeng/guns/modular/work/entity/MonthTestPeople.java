package cn.stylefeng.guns.modular.work.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * <p>
 * 月考人员表
 * </p>
 *
 * @author stylefeng
 * @since 2019-09-28
 */
@TableName("work_month_test")
@Data
public class MonthTestPeople implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "test_id", type = IdType.ID_WORKER)
    private Long testId;

    /**
     * 名字
     */
    @TableField("people_name")
    private String peopleName;

    /**
     * 身份证号
     */
    @TableField("people_identify")
    private String peopleIdentify;

    /**
     * 月考时间（比如说6月月考人员导入后，这个存进来的就是6）
     */
    @TableField("test_time")
    private Date testTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}
