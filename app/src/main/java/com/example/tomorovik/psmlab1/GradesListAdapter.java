package com.example.tomorovik.psmlab1;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tomorovik on 11.03.2017.
 */

public class GradesListAdapter extends ArrayAdapter<GradeModel> {

    // Variables
    List<GradeModel> gradesList;
    Context context;

    // Constructor which accepts incoming data and context and sets it to proper fields.
    public GradesListAdapter(@NonNull Context context, @NonNull List<GradeModel> gradesList) {
        super(context, R.layout.activity_grades, gradesList);
        this.gradesList = gradesList;
        this.context = context;
    }

    /// Overridden method needed for ViewHolder Pattern to work
    @Override
    public int getViewTypeCount() {
        if (gradesList.size() > 0) {
            return gradesList.size();
        }
        return 1;
    }

    /// Overridden method needed for ViewHolder Pattern to work
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    // Method which inflates view with layout parameters from grade_list_row
    // and set onCheckedChanged listener to RadioGroup in this view
    // and also set Tag for it with GradeModel object
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(context);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = li.inflate(R.layout.grade_list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.gradeNumberTV);
            viewHolder.rg = (RadioGroup) convertView.findViewById(R.id.grades);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GradeModel gradeModel = gradesList.get(position);
        viewHolder.tv.setText(gradeModel.getName());
        RadioGroup rg = (RadioGroup) convertView.findViewById(R.id.grades);
        rg.setTag(gradesList.get(position));
        viewHolder.rg = rg;

        rg.setOnCheckedChangeListener(listener);
        checkRadioButton(rg, position);
        return convertView;
    }

    RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            updateGrade(group, checkedId);
        }
    };

    // Custom method which checks proper RadioButton in RadioGroup
    void checkRadioButton(RadioGroup group, int position) {
        switch (gradesList.get(position).getGrade()) {
            case 2:
                group.check(R.id.grade2);
                break;
            case 3:
                group.check(R.id.grade3);
                break;
            case 4:
                group.check(R.id.grade4);
                break;
            case 5:
                group.check(R.id.grade5);
                break;
        }
    }

    // Custom method which updates grade field of GradeModel object
    // by value of proper RadioButton
    void updateGrade(RadioGroup group, int checkedId) {
        GradeModel gradeModel = (GradeModel) group.getTag();
        switch (checkedId) {
            case R.id.grade2:
                gradeModel.setGrade(2);
                break;
            case R.id.grade3:
                gradeModel.setGrade(3);
                break;
            case R.id.grade4:
                gradeModel.setGrade(4);
                break;
            case R.id.grade5:
                gradeModel.setGrade(5);
                break;
        }
    }

    static class ViewHolder {
        TextView tv;
        RadioGroup rg;
    }
}

