package com.test.greendao;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zhouwei.aaa.R;
import com.test.greendao.bean.Student;
import com.test.greendao.bean.Teacher;
import com.test.greendao.dao.CourseDao;
import com.test.greendao.dao.DaoMaster;
import com.test.greendao.dao.HeadDao;
import com.test.greendao.dao.JoinEntityDao;
import com.test.greendao.dao.More2BeanDao;
import com.test.greendao.dao.MoreBeanDao;
import com.test.greendao.dao.PersonDao;
import com.test.greendao.dao.StudentDao;
import com.test.greendao.dao.StudentsDao;
import com.test.greendao.dao.TeacherDao;
import com.test.greendao.more2more.JoinEntity;
import com.test.greendao.more2more.More2Bean;
import com.test.greendao.more2more.MoreBean;
import com.test.greendao.one2more.Course;
import com.test.greendao.one2more.Students;
import com.test.greendao.one_2_one.Head;
import com.test.greendao.one_2_one.Person;

import org.greenrobot.greendao.database.Database;

import java.util.List;
import java.util.UUID;

import application.MyApplication;
import utils.LogUtils;
import utils.SDCardUtils;

public class TestGreenDaoActivity extends AppCompatActivity {

    private TextView showResultTv;
    private StudentDao mStudentDao;
    private TeacherDao mEntrcyptDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_green_dao);

        showResultTv = (TextView)findViewById(R.id.show_result_tv_id);

        mStudentDao = MyApplication.getIntance().getDaoSession().getStudentDao();
        LogUtils.printResult("是否为空？" + (mStudentDao==null));

        mEntrcyptDao = MyApplication.getIntance().getEncryptDaoSession().getTeacherDao();

    }

    public void insert2Db(View view) {
        Student student = new Student(new Long(1),"jack");
        long rowResult = mStudentDao.insert(student);
        LogUtils.printResult("" +rowResult);

        Student student2 = new Student(null,"tom");//注意是null
        long rowResult2 = mStudentDao.insert(student2);
        LogUtils.printResult(""+rowResult2);

    }

    public void delete2Db(View view) {

        //第一种删除方式
//        mStudentDao.deleteByKey(1L);

        //第二种删除方式
        Student stu  = new Student( new Long(1),"jack");
        stu.__setDaoSession(MyApplication.getIntance().getDaoSession());
//        stu.delete();
    }

    public void update2Db(View view) {
        //第一种方式
        //select * from  student where stu_name = 'jack'
//        Student student = mStudentDao.queryBuilder().where(StudentDao.Properties.StuName.eq("jack")).build().unique();
//        LogUtils.printResult(student);
//
//        student.setStuName("tom");
//
//        mStudentDao.update(student);

        //第二种方式
        Student stu = new Student( new Long(1),"2222");
        stu.__setDaoSession(MyApplication.getIntance().getDaoSession());
        stu.refresh();

        LogUtils.printResult("更新结果："+ stu);
    }


    public void query2Db(View view) {

        //原生SQL语句的使用
        String querySQL = "select * from student t where id=1 and STU_NAME like 'jack'";
        Cursor cursor = MyApplication.getIntance().getDaoSession().getDatabase().rawQuery(querySQL, null);
        if (cursor != null && cursor.moveToFirst()){
            LogUtils.printResult("原生查询结果:" +cursor.getString(cursor.getColumnIndex("STU_NAME")));
            cursor.close();
        }

        //采用默认id 降序查询所有数据
        List<Student> stuList = mStudentDao.queryBuilder().list();
        for (Student stu: stuList){
            LogUtils.printResult("stu=" + stu.getStuName());
        }

        LogUtils.printResult("--------------------------------");

        List<Student> stuList2 = mStudentDao.queryBuilder().orderDesc(StudentDao.Properties.StuName).list();
        for (Student stu: stuList2){
            LogUtils.printResult("stu=" + stu.getStuName());
        }


//        //select * from student where stu_name = 'tom'
//        Student tomStu = mStudentDao.queryBuilder().where(StudentDao.Properties.StuName.eq("tom")).build().unique();
//        LogUtils.printResult(tomStu);


//        //select * from student where stu_name like '%t%'
//        Student tomLikeStu = mStudentDao.queryBuilder().where(StudentDao.Properties.StuName.like("%t%")).build().unique();
//        LogUtils.printResult(tomLikeStu);
//
//        //select * from student where stu_name in ('jack','tom')
//        Student inStu = mStudentDao.queryBuilder().where(StudentDao.Properties.StuName.in("jack,tom")).build().unique();
//        LogUtils.printResult(inStu);
    }

    //-------------------------------------------- 加密访问数据库 ---------------------------------------------------//

    public void entrcptInsert(View view) {
        Teacher t1 = new Teacher(UUID.randomUUID().toString(),"小明");
        mEntrcyptDao.insert(t1);
    }

    public void deleteInsert(View view) {
        Teacher teacher = mEntrcyptDao.queryBuilder().unique();

        mEntrcyptDao.delete(teacher);

    }

    public void updateEntrcpt(View view) {


    }

    public void queryEntrcpt(View view) {
        StringBuffer buffer = new StringBuffer();

        //加密方式访问
        List<Teacher> teacher = mEntrcyptDao.queryBuilder().list();
        for (int i = 0; i < teacher.size(); i++) {
            buffer.append(teacher.get(i)).append("-");
            LogUtils.printResult("加密访问结果：" +teacher.get(i).toString());
        }

        showResultTv.setText("" + buffer.toString());



        //发现直接在Activity中获取不到值，仅能在利用Application初始化的Session去获取
      /*  Database encryptDb = new DaoMaster.DevOpenHelper(this,SDCardUtils.INNERSDPATH + "/teacher.db3").getEncryptedWritableDb("123456");
        TeacherDao teacherDao = new DaoMaster(encryptDb).newSession().getTeacherDao();
        List<Teacher> teacher2 = teacherDao.queryBuilder().list();
        for (int i = 0; i < teacher2.size(); i++) {
            LogUtils.printResult("非加密方式访问" + teacher2.get(i).toString());
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MyApplication.getIntance().closeDatabase();

        Runtime.getRuntime().exit(0);
    }


    /**
     * 多对一 一对一  多对多 参考https://www.jianshu.com/p/dbec25bd575f
     * @param view
     */
    public void one2one(View view) {
        Head head = new Head(1L,"head_1");
        Person person = new Person(null,"person_1",1L);

        HeadDao headDao = MyApplication.getIntance().getDaoSession().getHeadDao();
        PersonDao personDao = MyApplication.getIntance().getDaoSession().getPersonDao();
        headDao.insert(head);
        personDao.insert(person);

        List<Person> persons = personDao.queryBuilder().build().list();
        for (Person p : persons) {
           LogUtils.printResult(p.toString());
        }
    }

    public void one2More(View view) {
        Students s1 = new Students("aaa","xxx","小明");

        Course  c1 = new Course("111","数学","aaa");
        Course  c2 = new Course("222","语文","aaa");
        Course  c3 = new Course("333","英语","aaa");

        CourseDao courseDao = MyApplication.getIntance().getDaoSession().getCourseDao();
        StudentsDao studentDao = MyApplication.getIntance().getDaoSession().getStudentsDao();

        studentDao.insert(s1);
        courseDao.insertInTx(c1,c2,c3);

        List<Students> studentList = studentDao.queryBuilder().list();
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < studentList.size(); i++) {
            b.append(studentList.get(i).getMCourseList().toString());
        }
        showResultTv.setText(b.toString());
    }

    /**
     * 多对多插入
     * @param view
     */
    public void more2More(View view) {
        MoreBean mb1 = new MoreBean(1L,"m1_1");
        MoreBean mb2 = new MoreBean(2L,"m1_2");

        More2Bean mb2_1 = new More2Bean(1L,"m2_1");
        More2Bean mb2_2 = new More2Bean(2L,"m2_2");

        JoinEntity j1  = new JoinEntity(null,1L,1L);
        JoinEntity j2  = new JoinEntity(null,1L,2L);

        JoinEntity j3  = new JoinEntity(null,2L,1L);
        JoinEntity j4  = new JoinEntity(null,2L,2L);


        MoreBeanDao moreBeanDao = MyApplication.getIntance().getDaoSession().getMoreBeanDao();
        More2BeanDao more2BeanDao = MyApplication.getIntance().getDaoSession().getMore2BeanDao();
        JoinEntityDao joinEntityDao = MyApplication.getIntance().getDaoSession().getJoinEntityDao();

        moreBeanDao.insert(mb1);
        moreBeanDao.insert(mb2);

        more2BeanDao.insert(mb2_1);
        more2BeanDao.insert(mb2_2);

        joinEntityDao.insert(j1);
        joinEntityDao.insert(j2);
        joinEntityDao.insert(j3);
        joinEntityDao.insert(j4);
    }

    public void one2oneForDel(View view) {
        PersonDao personDao = MyApplication.getIntance().getDaoSession().getPersonDao();
        HeadDao headDao = MyApplication.getIntance().getDaoSession().getHeadDao();

        headDao.deleteByKey(1L);
        personDao.deleteByKey(1L);
    }

    public void upgradeForDB(View view) {

    }
}
