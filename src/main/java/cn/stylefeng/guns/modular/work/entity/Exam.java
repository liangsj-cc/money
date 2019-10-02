package cn.stylefeng.guns.modular.work.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


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
public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "exam_id", type = IdType.ID_WORKER)
    private Long examId;

    /**
     * 题目
     */
    @TableField("exam_name")
    private String examName;



    /**
     * 题目类型（也就是工种类型）
     */
    @TableField("exam_type")
    private String examType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}
