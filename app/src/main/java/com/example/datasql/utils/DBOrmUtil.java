package com.example.datasql.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.datasql.entity.OrmStudent;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DBOrmUtil extends OrmLiteSqliteOpenHelper {

    public DBOrmUtil(Context context) {
        super(context, "test.db",
                null,3);
    }

    private static DBOrmUtil ormUtil;
    public static synchronized DBOrmUtil newInstance(Context context){
        if (ormUtil == null){
            ormUtil = new DBOrmUtil(context);
        }
        return ormUtil;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, OrmStudent.class);
            Log.i("DBOrmUtil","student表创建成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource,OrmStudent.class,true);
            onCreate(sqLiteDatabase,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
