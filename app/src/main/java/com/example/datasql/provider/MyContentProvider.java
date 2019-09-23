package com.example.datasql.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.datasql.utils.DBOrmUtil;

public class MyContentProvider extends ContentProvider {
    //表名
    private SQLiteDatabase db;
    public static final String TBL_NAME_STUDENT="goto_student";
    public static final String _ID="_id";

    //ContentProvider相关的常量
    private static final String CONTENT="content://";
    private static final String AUTHORIY="com.example.datasql.provider";
    private static final String URI=CONTENT+AUTHORIY+"/"+TBL_NAME_STUDENT;
    public static final Uri STUDENT_URI=Uri.parse(URI);

    //ContentProvider返回的数据类型的定义，数据集合
    //单项数据
    private static final String STUDENT_TYPE_ITEM="vnd.android.cursor.item/vnd."+AUTHORIY;
    //数据集合
    private static final String STUDENT_TYPE="vnd.android.cursor.dir/vnd"+AUTHORIY;

    static final int STUDENT=1;
    static final int STUDENT_ITEM=2;

    static UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(AUTHORIY,TBL_NAME_STUDENT,STUDENT);
        matcher.addURI(AUTHORIY,TBL_NAME_STUDENT+"/#",STUDENT_ITEM);
    }


    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int newId=0;  //更新不需要数据
        newId=db.delete(TBL_NAME_STUDENT,selection,selectionArgs);
        if(newId>0){
            return newId;
        }
        throw new IllegalArgumentException("插入失败"+uri);
    }
//根据UriMatcher中存储的URI进行类型匹配
    //作用：在进行增删改查的操作时，根据type选择对应的数据表
    @Override
    public String getType(Uri uri) {
        switch(matcher.match(uri)){
            case STUDENT:
                return STUDENT_TYPE;
            case STUDENT_ITEM:
                return STUDENT_TYPE_ITEM;
            default:
                throw new RuntimeException("错误的uri");
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long newId=db.insert(TBL_NAME_STUDENT,null,values);
        Uri newUri=Uri.parse(CONTENT+AUTHORIY+"/"+TBL_NAME_STUDENT+"/"+newId);
        if(newId>0){
            return newUri;
        }
        throw new IllegalArgumentException("插入失败"+uri);
    }

    @Override
    public boolean onCreate() {
        DBOrmUtil dbOrmUtil=new DBOrmUtil(getContext());
        db=dbOrmUtil.getWritableDatabase();
        return db!=null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor=null;
        switch(matcher.match(uri)){
            case STUDENT:
                cursor=db.query(TBL_NAME_STUDENT,projection,
                        selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case STUDENT_ITEM:
                String id=uri.getPathSegments().get(1);
                cursor=db.query(TBL_NAME_STUDENT,projection,
                        _ID+"+",new String[]{id},
                        null,null,sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int newId=0;
        newId=db.update(TBL_NAME_STUDENT,values,selection,selectionArgs);
        if(newId>0){
            return newId;
        }
        throw new IllegalArgumentException("插入失败"+uri);

    }
}
