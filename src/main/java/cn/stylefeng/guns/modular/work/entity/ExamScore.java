package cn.stylefeng.guns.modular.work.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ExamScore {
    private String userName;
    private String deptName;
    private String score;
    private Date createTime;
    private String examType;
}
