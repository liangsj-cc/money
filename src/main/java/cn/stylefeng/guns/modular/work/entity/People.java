package cn.stylefeng.guns.modular.work.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 人员表
 * </p>
 *
 * @author stylefeng
 * @since  2019-09-28
 */
@TableName("work_people")
@Data
public class People implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "people_id", type = IdType.ID_WORKER)
    private Long peopleId;

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
     * 部门
     */
    @TableField("people_dept")
    private String peopleDept;

    /**
     * 工种
     */
    @TableField("people_type")
    private String peopleType;

    /**
     * 性别
     */
    @TableField("people_sex")
    private String peopleSex;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}
