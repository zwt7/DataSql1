package com.example.datasql.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.datasql.R;
import com.example.datasql.adapter.StudentAdapter;
import com.example.datasql.entity.OrmStudent;
import com.example.datasql.service.StudentService;
import com.example.datasql.service.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ADD_REQUEST = 100;
    private static final int MODIFY_REQUEST = 101;
    private ListView lv_list;
    private StudentAdapter studentAdapter;
    private List<OrmStudent> students;
    private int selectedPos;
    private List<String> contacts;
    private OrmStudent selectedStudent;
    private Button add, amend, delete,read;
    private StudentService studentService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initView() {
        add = findViewById(R.id.btn_add);
        amend = findViewById(R.id.btn_amend);
        delete = findViewById(R.id.btn_delete);
        read=findViewById(R.id.btn_read);
        lv_list = findViewById(R.id.lv_list);

        read.setOnClickListener(this);

        studentAdapter = new StudentAdapter(this,students);
        lv_list.setAdapter(studentAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                intent.putExtra("flag", "添加");
                startActivityForResult(intent, ADD_REQUEST);
            }
        });

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                selectedPos = position;
                selectedStudent = (OrmStudent) parent.getItemAtPosition(position);
                Toast.makeText(MainActivity.this,selectedStudent.toString(),Toast.LENGTH_LONG).show();
                amend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                        intent.putExtra("flag", "修改");

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("student_obj", selectedStudent);
                        intent.putExtras(bundle);

                        startActivityForResult(intent, MODIFY_REQUEST);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        studentService.delete(selectedStudent);
                        students.remove(position);
                        studentAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    // 从SQLite数据库获取数据
    private void initData() {
        // 从SQLite数据库获取宿舍列表
        studentService = new StudentServiceImpl(this);
        students = studentService.getAllStudents();
        // 若数据库中没数据，则初始化数据列表，防止ListView报错
        if (students == null) {
            students = new ArrayList<>();
        }
    }

    // 接收InsertActivity的返回的添加或修改后的flag对象，更新flags，刷新列表
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            // 更新列表
            selectedStudent = (OrmStudent) bundle.get("student_obj");
            if (requestCode == MODIFY_REQUEST) {
                students.set(selectedPos, selectedStudent);
            } else if (requestCode == ADD_REQUEST) {
                students.add(selectedStudent);
            }
            // 刷新ListView
            studentAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        //判断是否有运行时权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);

        }
        else{
          //读取联系人
            readContacts();//需要申请运行时权限
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1&&grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

        }
    }

    private void readContacts() {
        Cursor cursor=getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null
        );
        contacts=new ArrayList<>();
        if (cursor != null && cursor.moveToNext()) {
            do {
                String name=cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contacts.add( name + ":" + phone);

            }
            while (cursor.moveToNext());{
                cursor.close();

            }
          }
          //设置Adapter
        if(contacts.isEmpty()){
            Toast.makeText(MainActivity.this,"没有联系人",Toast.LENGTH_LONG).show();
            return;
        }
        ArrayAdapter<String>arrayAdapter=new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,contacts);
        lv_list.setAdapter(arrayAdapter);
    }

}
