package com.example.datasql.service;

import android.content.Context;

import com.example.datasql.dao.OrmStudentDao;
import com.example.datasql.dao.OrmStudentDaoImpl;
import com.example.datasql.entity.OrmStudent;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private OrmStudentDao studentDao;

    public StudentServiceImpl(Context context) {
        studentDao = new OrmStudentDaoImpl(context);
    }

    public List<OrmStudent> getAllStudents() {
        return studentDao.selectAllStudents();
    }

    public void insert(OrmStudent student) {
        studentDao.insert(student);
    }

    @Override
    public void update(OrmStudent student) {
        studentDao.update(student);
    }

    @Override
    public void delete(OrmStudent student) {
        studentDao.delete(student);
    }

    @Override
    public void delete(int _id) {
        studentDao.delete(_id);
    }

}
