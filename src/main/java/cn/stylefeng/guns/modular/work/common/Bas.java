package cn.stylefeng.guns.modular.work.common;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体类继承该类获取 map
 */
public class Bas implements Serializable {

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
}
