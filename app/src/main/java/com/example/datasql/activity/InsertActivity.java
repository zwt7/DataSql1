package com.example.datasql.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.datasql.R;
import com.example.datasql.entity.OrmStudent;
import com.example.datasql.service.StudentService;
import com.example.datasql.service.StudentServiceImpl;

import java.util.Arrays;
import java.util.List;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_name;
    private EditText ed_age;
    private Spinner sp_classname;
    private Button ensure, remove;
    private List<String> fStudent;
    private String flag;
    private OrmStudent student;
    private StudentService studentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        studentService = new StudentServiceImpl(this);
        initView();
        initData();
    }

    private void initView() {
        ed_name = findViewById(R.id.edt_name);
        sp_classname = findViewById(R.id.spn_class);
        ed_age = findViewById(R.id.edt_age);
        ensure = findViewById(R.id.btn_ensure);
        remove = findViewById(R.id.btn_remove);

        ensure.setOnClickListener(this);
        remove.setOnClickListener(this);
        fStudent = Arrays.asList(getResources().getStringArray(R.array.fStrudent));
        sp_classname.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                fStudent));
    }

    private void initData() {
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            student = (OrmStudent) bundle.getSerializable("student_obj");
            if (student != null) {
                ed_name.setText(String.valueOf(student.getName()));
                ed_name.setEnabled(false);
                sp_classname.setSelection(fStudent.indexOf(student.getClassmate()), true);
                ed_age.setText(String.valueOf(student.getAge()));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ensure:
                update();
                break;
            case R.id.btn_remove:
                Intent intent = new Intent(InsertActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void update() {
        if (student == null) {
            student = new OrmStudent();
        }
        student.setName(ed_name.getText().toString());
        student.setClassmate((String) sp_classname.getSelectedItem());
        student.setAge(Integer.valueOf(ed_age.getText().toString()));
        if ("修改".equals(flag)) {
            studentService.update(student);
        } else if ("添加".equals(flag)) {
            studentService.insert(student);
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("student_obj", student);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
