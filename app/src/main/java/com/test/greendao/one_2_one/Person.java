package com.test.greendao.one_2_one;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.test.greendao.dao.DaoSession;
import com.test.greendao.dao.HeadDao;
import com.test.greendao.dao.PersonDao;

/**
 * Created by zhouwei on 2018/5/23.
 */

@Entity(active = false,nameInDb = "PERSON")
public class Person {

    @Id(autoincrement = true)
    @Property(nameInDb = "p_id")
    private Long pid;

    @Property(nameInDb = "p_name")
    private String pName;

//    @Property(nameInDb = "p_address")
//    private String address;

    @Property(nameInDb = "h_id")
    private Long hId;//外键
    @ToOne(joinProperty = "hId")//一对一需要引入另一个表的主键作为这个表外键作为关联关系
    private Head head;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 778611619)
    private transient PersonDao myDao;
    @Generated(hash = 779374785)
    public Person(Long pid, String pName, Long hId) {
        this.pid = pid;
        this.pName = pName;
        this.hId = hId;
    }
    @Generated(hash = 1024547259)
    public Person() {
    }
    public Long getPid() {
        return this.pid;
    }
    public void setPid(Long pid) {
        this.pid = pid;
    }
    public String getPName() {
        return this.pName;
    }
    public void setPName(String pName) {
        this.pName = pName;
    }
    public Long getHId() {
        return this.hId;
    }
    public void setHId(Long hId) {
        this.hId = hId;
    }
    @Generated(hash = 80859862)
    private transient Long head__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 733533705)
    public Head getHead() {
        Long __key = this.hId;
        if (head__resolvedKey == null || !head__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            HeadDao targetDao = daoSession.getHeadDao();
            Head headNew = targetDao.load(__key);
            synchronized (this) {
                head = headNew;
                head__resolvedKey = __key;
            }
        }
        return head;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 821313359)
    public void setHead(Head head) {
        synchronized (this) {
            this.head = head;
            hId = head == null ? null : head.getHId();
            head__resolvedKey = hId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2056799268)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPersonDao() : null;
    }


}
