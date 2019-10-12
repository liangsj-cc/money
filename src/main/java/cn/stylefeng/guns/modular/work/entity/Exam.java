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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Exam   {

    @TableField(exist = false)
    private Map<String, Object> tails;

    @JsonAnyGetter
    private Map<String, Object> getTails() {
        return this.tails;
    }

    public void set(String key, Object value) {
        if (this.tails == null) {
            this.tails = new HashMap<>();
        }
        this.tails.put(key, value);
    }


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
     * 题目类型（也就是工种类型）
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
