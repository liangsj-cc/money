package cn.stylefeng.guns.modular.work.entity;

import cn.stylefeng.guns.modular.work.common.Bas;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * 习题表
 * </p>
 *
 * @author stylefeng
 * @since 2019-09-28
 */
@TableName("work_exam")
@Data
public class Exam  extends Bas  {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 题目 试题名字
     */
    @TableField("name")
    private String name;

    /**
     *  type : 题目类型 0日考 1 月考
     *
     */
    @TableField("type")
    private String type;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "selector")
    private String selector;

    @TableField(value = "dept_id")
    private Long deptId;

    // 题目个数
    @TableField(value = "num")
    private Integer num;

}
