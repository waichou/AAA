package com.test.greendao.more2more;

import org.greenrobot.greendao.annotation.*;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.test.greendao.dao.DaoSession;
import com.test.greendao.dao.MoreBeanDao;
import com.test.greendao.dao.More2BeanDao;

/**
 * Created by zhouwei on 2018/5/23.
 */

@Entity
public class More2Bean {

    @Id(autoincrement = true)
    private Long m2Id;

    @Property(nameInDb = "more_2name_id")
    private String mMore2Name;

    @ToMany
    @org.greenrobot.greendao.annotation.JoinEntity(
            entity = JoinEntity.class,
            sourceProperty = "m2Id",
            targetProperty = "mId"
    )
    private List<MoreBean> moreBeanList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2056027118)
    private transient More2BeanDao myDao;

    @Generated(hash = 1386577503)
    public More2Bean(Long m2Id, String mMore2Name) {
        this.m2Id = m2Id;
        this.mMore2Name = mMore2Name;
    }

    @Generated(hash = 1428136396)
    public More2Bean() {
    }

    public Long getM2Id() {
        return this.m2Id;
    }

    public void setM2Id(Long m2Id) {
        this.m2Id = m2Id;
    }

    public String getMMore2Name() {
        return this.mMore2Name;
    }

    public void setMMore2Name(String mMore2Name) {
        this.mMore2Name = mMore2Name;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 948349896)
    public List<MoreBean> getMoreBeanList() {
        if (moreBeanList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MoreBeanDao targetDao = daoSession.getMoreBeanDao();
            List<MoreBean> moreBeanListNew = targetDao
                    ._queryMore2Bean_MoreBeanList(m2Id);
            synchronized (this) {
                if (moreBeanList == null) {
                    moreBeanList = moreBeanListNew;
                }
            }
        }
        return moreBeanList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 314397034)
    public synchronized void resetMoreBeanList() {
        moreBeanList = null;
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
    @Generated(hash = 1935911141)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMore2BeanDao() : null;
    }


}
