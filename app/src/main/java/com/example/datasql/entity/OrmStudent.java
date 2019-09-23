package com.example.datasql.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "goto_student")
public class OrmStudent implements Serializable {
    @DatabaseField(generatedId = true)
    private int goto_id;
    @DatabaseField(index = true,columnName = "name",dataType = DataType.STRING)
    private String name;
    @DatabaseField
    private String classmate;
    @DatabaseField(columnName = "age",dataType = DataType.INTEGER,canBeNull = true)
    private int age;

    public OrmStudent() {
    }

    public OrmStudent(int goto_id, String name, String classmate, int age) {
        this.goto_id = goto_id;
        this.name = name;
        this.classmate = classmate;
        this.age = age;
    }

    public int getGoto_id() {
        return goto_id;
    }

    public void setGoto_id(int goto_id) {
        this.goto_id = goto_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassmate() {
        return classmate;
    }

    public void setClassmate(String classmate) {
        this.classmate = classmate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "goto_id=" + goto_id +
                ", name='" + name + '\'' +
                ", classmate='" + classmate + '\'' +
                ", age=" + age +
                '}';
    }
}
