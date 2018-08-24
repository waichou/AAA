package com.test.greendao.upgrade;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.test.greendao.dao.DaoMaster;
import com.test.greendao.dao.PersonDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.StandardDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by wenld- on 2015/12/24.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private DataBaseUpgradeLinstener mListener;

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version,DataBaseUpgradeLinstener linstener) {
        super(context, name, factory, version);
        mListener = linstener;
    }

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DaoMaster.createAllTables(new StandardDatabase(db), false);
    }

    /**
         第一、原有表中“添加新列√”
         第二、在原表中“更新列名”、“类型”以及“其他约束” （不满足条件，需要补充） 和 “删除掉原表某列√”
         第三、删除存在的表 √（不满足条件，需要补充）
         第四、更新表名（不满足条件，需要补充，不是很重要）√

     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (mListener != null){
                ///
               /* List<String> delTableNameList = mListener.upgradeForDelTable();
                if (delTableNameList != null) {
                    db.beginTransaction();
                    List<String> delSQl = new ArrayList<>();
                    for (int i = 0; delTableNameList != null && i < delTableNameList.size(); i++) {
                        delSQl.add("delete from '"+ delTableNameList.get(i) +"'");
                    }
                    for (int i = 0; i < delSQl.size(); i++) {
                        db.execSQL(delSQl.get(i));
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                }

                ///
                Map<String, String> renameTableMap = mListener.upgradeForRenameTableName();
                if (renameTableMap != null){
                    List<String> renameSQL = new ArrayList<>();

                    Set<Map.Entry<String, String>> entries = renameTableMap.entrySet();
                    Iterator<Map.Entry<String, String>> iterator =entries.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, String> next = iterator.next();
                        String tableName = next.getKey();
                        String newTableName = next.getValue();

                        renameSQL.add("alter table '"+tableName+"' rename to '"+newTableName+"'");
                    }

                    db.beginTransaction();
                    for (int i = 0; i < renameSQL.size(); i++) {
                        db.execSQL(renameSQL.get(i));
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                }*/
                ///
//                List<Class<? extends AbstractDao<?, ?>>> classDaoList = mListener.allTableColumnOrDelColumn();
//
//                DBMigrationHelper migratorHelper = new DBMigrationHelper();
//                migratorHelper.onUpgrade(db, ((classDaoList != null) || (classDaoList.size() > 0)) ? (Class<? extends AbstractDao<?, ?>>[]) classDaoList.toArray() : null);

                DBMigrationHelper migratorHelper = new DBMigrationHelper();
                migratorHelper.onUpgrade(db, new Class[]{PersonDao.class});
            }
        } catch (ClassCastException e) {
        }
    }

//    private List<String> delTableList = null;
//
//    /**
//     * 删除存在的表
//     * @param tableName
//     */
//    public void upgradeForDelTable(String ... tableName){
//        if (tableName ==null || tableName.length ==0){
//            return;
//        }
//        delTableList = Arrays.asList(tableName);
//    }

//    /**
//     * 更新表名
//     */
//    public void upgradeForRenameTableName(){
//
//    }

//    /**
//     * 在原表中“更新列名”、“类型”以及“其他约束”
//     */
//    public void upgradeForRenameTableConstraint(){
//
//    }


    public interface DataBaseUpgradeLinstener{
        public List<String> upgradeForDelTable( );//删除表
        public Map<String,String> upgradeForRenameTableName();//更新表名
        public List<Class<? extends AbstractDao<?, ?>>> allTableColumnOrDelColumn();//增加表字段或删除表字段

        public List<String> upgradeForRenameTableConstraint();//更新列
    }

}
