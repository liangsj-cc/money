package cn.stylefeng.guns.modular.work.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 人员得分表
 * </p>
 *
 * @author stylefeng
 * @since 2019-09-28
 */
@TableName("work_score")
@Data
public class Score implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "score_id", type = IdType.ID_WORKER)
    private Long scoreId;

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
     * 考试类型（月考/日考）
     */
    @TableField("score_type")
    private String scoreType;

    /**
     * 考试时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}
