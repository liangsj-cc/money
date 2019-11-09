package cn.stylefeng.guns.modular.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("work_exam_user")
@Data
@NoArgsConstructor
public class ExamUser {

    public ExamUser(Long userId, Long examId) {
        this.userId = userId;
        this.examId = examId;
    }

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    private Long userId;
    private Long examId;
}
