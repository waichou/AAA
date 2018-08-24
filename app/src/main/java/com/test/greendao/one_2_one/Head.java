package com.test.greendao.one_2_one;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhouwei on 2018/5/23.
 */

@Entity(nameInDb = "HEAD")
public class Head {
    @Id@Property(nameInDb = "h_id")
    private Long hId;
    @Property(nameInDb = "h_name")
    private String hName;

    @Generated(hash = 2007400104)
    public Head(Long hId, String hName) {
        this.hId = hId;
        this.hName = hName;
    }
    @Generated(hash = 1745729831)
    public Head() {
    }
    public Long getHId() {
        return this.hId;
    }
    public void setHId(Long hId) {
        this.hId = hId;
    }
    public String getHName() {
        return this.hName;
    }
    public void setHName(String hName) {
        this.hName = hName;
    }


    @Override
    public String toString() {
        return "Head{" +
                "hName='" + hName + '\'' +
                '}';
    }
}
