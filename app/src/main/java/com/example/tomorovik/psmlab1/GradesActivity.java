package com.example.tomorovik.psmlab1;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GradesActivity extends AppCompatActivity {

    // Const variable to keep string for passing parameters
    public final static String GRADE = "grade";
    public final static String GRADES_AVG = "grades_avg";

    // Data variable
    List<GradeModel> gradesList;
    int gradesAmount;
    float gradesAvg;

    // Views variables
    Button readyBTN;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        readyBTN = (Button) findViewById(R.id.readyBTN);
        gradesList = new ArrayList<>();
        if (savedInstanceState == null) {                                               // Checks if saveInstanceState is null
            Bundle bundle = this.getIntent().getExtras();                               // Get extras from intent from MainActivity
            gradesAmount = bundle.getInt(MainActivity.GRADES_AMOUNT);
            for (int i = 0; i < gradesAmount; i++) {                                    // Fills List with GradeModel objects
                gradesList.add(new GradeModel("OCENA " + (i + 1)));
            }
        } else {
            gradesAmount = savedInstanceState.getInt(MainActivity.GRADES_AMOUNT);       // Fills list with save GradeModel objects from savedInstanceState
            for (int i = 0; i < gradesAmount; i++) {
                gradesList.add(new GradeModel("OCENA " + (i + 1), savedInstanceState.getInt(GRADE + i)));
            }
        }


        GradesListAdapter gradesListAdapter = new GradesListAdapter(this, gradesList);  // Create custom adapter object with current context and gradesList
        listView = (ListView) findViewById(R.id.gradeListView);                         // Finds proper ListView of this Activity
        listView.setAdapter(gradesListAdapter);                                         // Sets custom adapter to found listView

        // Creates setOnClickListener on readyBTN which will pass whole bundle as result from this activity and finish this activity
        readyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //boolean isAllSelected = (gradesList.stream().filter(gm->gm.getGrade()!=0).toArray()).length ==gradesAmount;
                if (checkIfAllGradesSelected()) {
                    calculateAvg();
                    Intent intent = new Intent();
                    intent.putExtra(GRADES_AVG,gradesAvg);
                    setResult(RESULT_OK, intent);
                    finish();
                } else
                    Toast.makeText(GradesActivity.this, "Nie wybrano wszystkich ocen!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /// Custom method for calculating average grade
    private void calculateAvg(){
        int sum = 0;
        for (int i = 0; i < gradesAmount; i++)
            sum += gradesList.get(i).getGrade();
        gradesAvg = (float) sum / gradesAmount;
    }

    /// Custom method for checking if all grade rows has been selected
    private boolean checkIfAllGradesSelected() {
        int selectedRows = 0;
        for (GradeModel gm :
                gradesList) {
            if (gm.getGrade() != 0)
                selectedRows++;
        }
        return selectedRows == gradesAmount;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveGrades(outState);
    }

    // Custom method for saving data
    void saveGrades(Bundle bundle) {
        bundle.putInt(MainActivity.GRADES_AMOUNT, gradesAmount);
        for (int i = 0; i < gradesAmount; i++) {
            bundle.putInt(GRADE + i, gradesList.get(i).getGrade());
        }
    }
}
