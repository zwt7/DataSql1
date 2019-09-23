package com.example.datasql.dao;

import com.example.datasql.entity.OrmStudent;

import java.util.List;

public interface OrmStudentDao {

    List<OrmStudent> selectAllStudents();

    void insert(OrmStudent student);

    void update(OrmStudent student);

    void delete(OrmStudent student);
    void delete(int _id);
}
