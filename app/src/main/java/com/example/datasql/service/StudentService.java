package com.example.datasql.service;

import com.example.datasql.entity.OrmStudent;
import java.util.List;

public interface StudentService {
    List<OrmStudent> getAllStudents();

    void insert(OrmStudent student);

    void update(OrmStudent student);

    void delete(OrmStudent student);
    void delete(int _id);
}
