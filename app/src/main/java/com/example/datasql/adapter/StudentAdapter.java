package com.example.datasql.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.datasql.R;
import com.example.datasql.entity.OrmStudent;

import java.util.List;

public class StudentAdapter extends BaseAdapter {
    private List<OrmStudent> students;
    private Context context;

    public StudentAdapter(Context context,List<OrmStudent> students) {
        this.context = context;
        this.students = students;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);

            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_class = convertView.findViewById(R.id.tv_class);
            holder.tv_age = convertView.findViewById(R.id.tv_age);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrmStudent msg = students.get(position);
        holder.tv_name.setText(msg.getName());
        holder.tv_class.setText(msg.getClassmate());
        holder.tv_age.setText(String.valueOf(msg.getAge()));

        return convertView;
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_class;
        TextView tv_age;
    }
}
