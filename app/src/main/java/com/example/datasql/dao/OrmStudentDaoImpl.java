package com.example.datasql.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.datasql.entity.OrmStudent;
import com.example.datasql.provider.MyContentProvider;
import com.example.datasql.utils.DBOrmUtil;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrmStudentDaoImpl implements OrmStudentDao {

    private DBOrmUtil dbOrmUtil;
    private Dao<OrmStudent,Integer> dao;
    private Context context;

    public OrmStudentDaoImpl(Context context) {
        this.context=context;
        dbOrmUtil = DBOrmUtil.newInstance(context);
        try {
            dao = dbOrmUtil.getDao(OrmStudent.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<OrmStudent> selectAllStudents() {
        List<OrmStudent> students = null;
//        try {
////            students = dao.queryForAll();
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////        return students;
        SQLiteDatabase db=dbOrmUtil.getReadableDatabase();
        Cursor cursor=context.getContentResolver().query(MyContentProvider.STUDENT_URI,
                null,null,null,null);

        if (cursor != null && cursor.getCount() > 0) {
            students = new ArrayList<>();
            while (cursor.moveToNext()) {
                OrmStudent student1 = new OrmStudent();
                student1.setGoto_id(cursor.getInt(cursor.getColumnIndex("goto_id")));
                student1.setName(cursor.getString(cursor.getColumnIndex("name")));
                student1.setClassmate(cursor.getString(cursor.getColumnIndex("classmate")));
                student1.setAge(cursor.getInt(cursor.getColumnIndex("age")));
                students.add(student1);
            }
            cursor.close();
        }
        db.close();
        return students;
    }

    @Override
    public void insert(OrmStudent student) {
        dbOrmUtil = DBOrmUtil.newInstance(context);
//        try {
//            dao.create(student);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        ContentValues values=new ContentValues();
        values.put("name",student.getName());
        values.put("classmate",student.getClassmate());
        values.put("age",student.getAge());
        Uri uri=context.getContentResolver().insert(MyContentProvider.STUDENT_URI,values);
        Log.i("DataSql",uri !=null? uri.toString() :"");
        dbOrmUtil.close();
    }

    @Override
    public void update(OrmStudent student) {
        dbOrmUtil = DBOrmUtil.newInstance(context);
//        try {
//            dao.update(student);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        ContentValues values=new ContentValues();
        values.put("name",student.getName());
        values.put("classmate",student.getClassmate());
        values.put("age",student.getAge());
        context.getContentResolver().update(MyContentProvider.STUDENT_URI,values,"goto_id=?"
                ,new String[]{ String.valueOf(student.getGoto_id())});
    }

    @Override
    public void delete(OrmStudent student) {
        try {
            dao.delete(student);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(int _id) {
        context.getContentResolver().delete(MyContentProvider.STUDENT_URI,"goto_id=?",new String[]{String.valueOf(_id)});
    }
}
