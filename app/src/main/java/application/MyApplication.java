package application;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.test.greendao.dao.DaoMaster;
import com.test.greendao.dao.DaoSession;
import com.test.greendao.dao.PersonDao;
import com.test.greendao.dao.StudentDao;
import com.test.greendao.upgrade.DatabaseOpenHelper;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.SDCardUtils;


/**
 * Created by zhouwei on 2018/5/7.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication = null;

    //非加密数据实例
    private DaoSession daoSession;
    public SQLiteDatabase mSQLiteDatabase;
    //加密的数据库实例
    private DaoSession enctryptDaoSession;
    private Database mEntrcyptDatabase;

    //升级数据库表
    private SQLiteDatabase mUpgradeDatabase;
    private DaoSession mUpgradeSession;

    public  Context mContext;
    

    public static MyApplication getIntance(){
        return myApplication;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        myApplication = this;
        mContext = getApplicationContext();

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        initGreenDao();
    }
    private void initGreenDao(){
        //非加密的数据库
        mSQLiteDatabase = new DaoMaster.DevOpenHelper(this, SDCardUtils.INNERSDPATH + "/test.db3").getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(mSQLiteDatabase);
        daoSession = daoMaster.newSession();

        //加密的数据库
        mEntrcyptDatabase = new DaoMaster.DevOpenHelper(this,SDCardUtils.INNERSDPATH + "/entrcy.db3").getEncryptedWritableDb("123456");
        enctryptDaoSession = new DaoMaster(mEntrcyptDatabase).newSession();

        //升级数据表，重组实例化过程
        mUpgradeDatabase = new DatabaseOpenHelper(this, SDCardUtils.INNERSDPATH + "/upgrade.db3", null, DaoMaster.SCHEMA_VERSION, new DatabaseOpenHelper.DataBaseUpgradeLinstener() {
            @Override
            public List<String> upgradeForDelTable() {
                List<String> delTableName = new ArrayList<String>();
                delTableName.add("head_head");
                return delTableName;
            }

            @Override
            public Map<String, String> upgradeForRenameTableName() {
                Map<String,String> renameMap = new HashMap<String, String>();
                renameMap.put("PERSON","PERSON_T");
                return renameMap;
            }

            @Override
            public List<Class<? extends AbstractDao<?, ?>>> allTableColumnOrDelColumn() {
                List<Class<? extends AbstractDao<?, ?>>> classDaoList = new ArrayList<Class<? extends AbstractDao<?, ?>>>();
                classDaoList.add(PersonDao.class);
                return classDaoList;
            }


            @Override
            public List<String> upgradeForRenameTableConstraint() {
                return null;
            }
        }).getWritableDatabase();
        DaoMaster upgradeMaster = new DaoMaster(mUpgradeDatabase);
        mUpgradeSession = upgradeMaster.newSession();
    }

    /**
     * 公开获取当前数据库对应的唯一DaoSession，后期访问此数据库表的Dao
     * 可以通过此DaoSession来获取相应的XXXDao实例，做对表的CRUD操作
     * @return
     */
    public DaoSession getDaoSession(){
        if(daoSession == null){
            initGreenDao();
        }
        return daoSession;
    }

    /**
     * 加密的DaoSession
     * @return
     */
    public DaoSession getEncryptDaoSession(){
        if (enctryptDaoSession == null){
            initGreenDao();
        }
        return enctryptDaoSession;
    }

    public void closeDatabase(){
        if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen()){
            if (daoSession != null){
                daoSession.clear();
                daoSession = null;
            }

            mSQLiteDatabase.close();
        }

        if (mEntrcyptDatabase !=null){
            if (enctryptDaoSession != null){
                enctryptDaoSession.clear();
                enctryptDaoSession = null;
            }
            mEntrcyptDatabase.close();
        }
    }
}
