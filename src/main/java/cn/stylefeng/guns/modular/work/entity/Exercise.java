package cn.stylefeng.guns.modular.work.entity;

import cn.stylefeng.guns.modular.work.common.Bas;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 习题库
 */
@TableName("work_exercise")
@Data
public class Exercise extends Bas implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    Long id;

    // 题目名称
    String name;
    // 题目选项
    String options;
    // 题目正确答案  1 正确 0 错误
    String rights;
    // 题目
    String label;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

}
