package com.test.greendao.more2more;

import org.greenrobot.greendao.annotation.*;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.test.greendao.dao.DaoSession;
import com.test.greendao.dao.More2BeanDao;
import com.test.greendao.dao.MoreBeanDao;

/**
 * Created by zhouwei on 2018/5/23.
 */

@Entity
public class MoreBean {

    @Id(autoincrement = true)
    private Long mId;

    @Property(nameInDb = "more_name_id")
    private String mMoreName;

    @ToMany
    @org.greenrobot.greendao.annotation.JoinEntity(
            entity = JoinEntity.class,
            sourceProperty = "mId",
            targetProperty = "m2Id"
    )
    private List<More2Bean> more2BeanList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2102750404)
    private transient MoreBeanDao myDao;

    @Generated(hash = 128192365)
    public MoreBean(Long mId, String mMoreName) {
        this.mId = mId;
        this.mMoreName = mMoreName;
    }

    @Generated(hash = 1360162637)
    public MoreBean() {
    }

    public Long getMId() {
        return this.mId;
    }

    public void setMId(Long mId) {
        this.mId = mId;
    }

    public String getMMoreName() {
        return this.mMoreName;
    }

    public void setMMoreName(String mMoreName) {
        this.mMoreName = mMoreName;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1299290688)
    public List<More2Bean> getMore2BeanList() {
        if (more2BeanList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            More2BeanDao targetDao = daoSession.getMore2BeanDao();
            List<More2Bean> more2BeanListNew = targetDao
                    ._queryMoreBean_More2BeanList(mId);
            synchronized (this) {
                if (more2BeanList == null) {
                    more2BeanList = more2BeanListNew;
                }
            }
        }
        return more2BeanList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 377956840)
    public synchronized void resetMore2BeanList() {
        more2BeanList = null;
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
    @Generated(hash = 1353059569)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMoreBeanDao() : null;
    }

}
