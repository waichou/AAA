package com.test.greendao.more2more;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhouwei on 2018/5/23.
 */

@Entity
public class JoinEntity {
    @Id(autoincrement = true)
    private Long mJoinId;

    private Long mId;

    private Long m2Id;

    @Generated(hash = 319247775)
    public JoinEntity(Long mJoinId, Long mId, Long m2Id) {
        this.mJoinId = mJoinId;
        this.mId = mId;
        this.m2Id = m2Id;
    }

    @Generated(hash = 1041518148)
    public JoinEntity() {
    }

    public Long getMJoinId() {
        return this.mJoinId;
    }

    public void setMJoinId(Long mJoinId) {
        this.mJoinId = mJoinId;
    }

    public Long getMId() {
        return this.mId;
    }

    public void setMId(Long mId) {
        this.mId = mId;
    }

    public Long getM2Id() {
        return this.m2Id;
    }

    public void setM2Id(Long m2Id) {
        this.m2Id = m2Id;
    }
}
