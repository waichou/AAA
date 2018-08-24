package com.test.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by zhouwei on 2018/5/18.
 */

@Entity(indexes = {@Index(value = "tea_id" ,unique = true),@Index(value = "tea_name")})
public class Teacher {

    @Unique
    @Id///唯一表示字符串id不唯一
    private String tea_id;

    private String tea_name;

    @Generated(hash = 1913614136)
    public Teacher(String tea_id, String tea_name) {
        this.tea_id = tea_id;
        this.tea_name = tea_name;
    }

    @Generated(hash = 1630413260)
    public Teacher() {
    }

    public String getTea_id() {
        return this.tea_id;
    }

    public void setTea_id(String tea_id) {
        this.tea_id = tea_id;
    }

    public String getTea_name() {
        return this.tea_name;
    }

    public void setTea_name(String tea_name) {
        this.tea_name = tea_name;
    }


    @Override
    public String toString() {
        return "Teacher{" +
                "tea_id='" + tea_id + '\'' +
                ", tea_name='" + tea_name + '\'' +
                '}';
    }
}
