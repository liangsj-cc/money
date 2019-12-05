package cn.stylefeng.guns.modular.work.entity;

import lombok.Data;

import java.util.Date;

@Data
public class NoExamUser {
    private String userName;
    private String deptName;
    private Date examDate;
    private String examType;
}
