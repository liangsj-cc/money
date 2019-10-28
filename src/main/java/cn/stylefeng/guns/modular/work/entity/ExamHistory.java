package cn.stylefeng.guns.modular.work.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@TableName("work_exam_history")
@Data
public class ExamHistory {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @TableField("dept_id")
    private Long deptId;

    @TableField("user_id")
    private Long userId;

    @TableField("exam_json")
    private String examJson;

    @TableField("exam_type")
    private String type;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


}
