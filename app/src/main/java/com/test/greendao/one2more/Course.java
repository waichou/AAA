package com.test.greendao.one2more;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhouwei on 2018/5/23.
 */

@Entity //一对多 多的一方（多的一方要为一的一方的主键作为外键）
public class Course {
    @Id @Unique @Property(nameInDb = "c_id")
    private String cId;

    @Property(nameInDb = "c_name")
    private String cName;

    @Property(nameInDb = "stu_id")
    private String mStuId;//外键

    @Generated(hash = 1091965652)
    public Course(String cId, String cName, String mStuId) {
        this.cId = cId;
        this.cName = cName;
        this.mStuId = mStuId;
    }

    @Generated(hash = 1355838961)
    public Course() {
    }

    public String getCId() {
        return this.cId;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }

    @Override
    public String toString() {
        return "Course{" +
                "cId='" + cId + '\'' +
                ", cName='" + cName + '\'' +
                '}';
    }

    public String getCName() {
        return this.cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getMStuId() {
        return this.mStuId;
    }

    public void setMStuId(String mStuId) {
        this.mStuId = mStuId;
    }
}
