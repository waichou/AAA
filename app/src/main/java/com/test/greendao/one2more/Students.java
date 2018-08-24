package com.test.greendao.one2more;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.test.greendao.dao.DaoSession;
import com.test.greendao.dao.CourseDao;
import com.test.greendao.dao.StudentsDao;

/**
 * Created by zhouwei on 2018/5/23.
 */

@Entity//一对多 --> 一的一方
public class Students {
    @Id @Unique @Property(nameInDb = "stu_id")
    private String sId;

    @Property(nameInDb = "stuId")
    private String sStuId;

    @Property(nameInDb = "stu_name")
    private String sName;

    @ToMany(referencedJoinProperty = "mStuId")//注意cId是Course实体类中的属性Id名称
    private List<Course> mCourseList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 998494339)
    private transient StudentsDao myDao;

    @Generated(hash = 1168282891)
    public Students(String sId, String sStuId, String sName) {
        this.sId = sId;
        this.sStuId = sStuId;
        this.sName = sName;
    }

    @Generated(hash = 174834727)
    public Students() {
    }

    public String getSId() {
        return this.sId;
    }

    public void setSId(String sId) {
        this.sId = sId;
    }

    public String getSStuId() {
        return this.sStuId;
    }

    public void setSStuId(String sStuId) {
        this.sStuId = sStuId;
    }

    public String getSName() {
        return this.sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 333836586)
    public List<Course> getMCourseList() {
        if (mCourseList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CourseDao targetDao = daoSession.getCourseDao();
            List<Course> mCourseListNew = targetDao._queryStudents_MCourseList(sId);
            synchronized (this) {
                if (mCourseList == null) {
                    mCourseList = mCourseListNew;
                }
            }
        }
        return mCourseList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 630392373)
    public synchronized void resetMCourseList() {
        mCourseList = null;
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
    @Generated(hash = 194085185)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStudentsDao() : null;
    }

}
