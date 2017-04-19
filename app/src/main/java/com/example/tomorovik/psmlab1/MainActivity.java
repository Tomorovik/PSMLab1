package com.example.tomorovik.psmlab1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Constants for passing parameters.
    final Context context = this;
    public final static String GRADES_AMOUNT = "grades_amount";
    public final static String FIRST_NAME = "first_name";
    public final static String LAST_NAME = "last_name";
    public final static String AVG_CALCULATED = "avg_calculated";
    public final static String IS_BACK = "is_back";

    // View variables
    private TextView avgResultTV;
    private EditText firstNameET;
    private EditText lastNameET;
    private EditText gradesAmountET;
    private Button submitBTN;
    private Button resultBTN;

    // Data variables
    private boolean isAvgComputed = false;
    private float computedAvg = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNameET = (EditText) findViewById(R.id.firstNameET);
        lastNameET = (EditText) findViewById(R.id.lastNameET);
        gradesAmountET = (EditText) findViewById(R.id.gradesAmountET);
        submitBTN = (Button) findViewById(R.id.SubmitBTN);
        resultBTN = (Button) findViewById(R.id.resultBTN);
        avgResultTV = (TextView) findViewById(R.id.avg_resultTV);

        // Create listener for focus change events
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkIfValid(v, hasFocus);
            }
        };

        // Assign listener to proper Views
        firstNameET.setOnFocusChangeListener(onFocusChangeListener);
        lastNameET.setOnFocusChangeListener(onFocusChangeListener);
        gradesAmountET.setOnFocusChangeListener(onFocusChangeListener);

        // Set Focus on first EditText field
        if (getCurrentFocus() == null)
            firstNameET.requestFocus();

        // Create text watcher
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfValid(getCurrentFocus(), false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        // Assign text watcher for proper Views
        firstNameET.addTextChangedListener(textWatcher);
        lastNameET.addTextChangedListener(textWatcher);
        gradesAmountET.addTextChangedListener(textWatcher);

        // Set anonymous onClickListener for moving to another activity
        // along with parameter value for
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GradesActivity.class);
                intent.putExtra(GRADES_AMOUNT, Integer.parseInt(gradesAmountET.getText().toString()));
                startActivityForResult(intent, 1);
            }
        });

        // Set anonymous onClickListener for closing app button.
        resultBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                if (computedAvg >= 3) message = "Gratulacje! Otrzymujesz zaliczenie! ";
                else message = "Wysyłam podanie o zaliczenie warunkowe.";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                finish();
            }
        });

        // Checks and fills EditText Fields if savedInstanceState were present
        if (savedInstanceState != null) {
            firstNameET.setText(savedInstanceState.getString(FIRST_NAME));
            lastNameET.setText(savedInstanceState.getString(LAST_NAME));
            gradesAmountET.setText(savedInstanceState.getString(GRADES_AMOUNT));
            isAvgComputed = savedInstanceState.getBoolean(IS_BACK);
            computedAvg = savedInstanceState.getFloat(AVG_CALCULATED);
        }
    }

    // Save data onSaveInstanceState event
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveData(outState);
    }

    // Custom method for saving data
    private void saveData(Bundle outState) {
        outState.putString(FIRST_NAME, firstNameET.getText().toString());
        outState.putString(LAST_NAME, lastNameET.getText().toString());
        outState.putString(GRADES_AMOUNT, gradesAmountET.getText().toString());
        outState.putFloat(AVG_CALCULATED, computedAvg);
        outState.putBoolean(IS_BACK, isAvgComputed);
    }

    // Method which receives info from GradesActivity about grades and calculates average grade
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                computedAvg = bundle.getFloat(GradesActivity.GRADES_AVG);
                isAvgComputed = true;
            }
            showButton();
        } catch (Exception ex) {
        }
    }

    // Custom method which checks id of focused View and sends Toasts in case of wrong values
    private void checkIfValid(View view, boolean hasFocus) {
        try {
            if (hasFocus) return;
            switch (view.getId()) {
                case R.id.firstNameET:
                    if (!checkFirstName()) {
                        Toast.makeText(this, "IMIE nie może być puste!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.lastNameET:
                    if (!checkLastName()) {
                        Toast.makeText(this, "NAZWISKO nie może być puste!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.gradesAmountET:
                    if (!checkGradesAmount()) {
                        Toast.makeText(this, "LICZBA OCEN musi byc miedzy 5 a 15", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            showButton();
        } catch (Exception ex) {
        }
    }

    // Check the firstName EditText if it's empty field
    private boolean checkFirstName() {
        boolean result = false;
        try {
            result = !firstNameET.getText().toString().isEmpty();
        } catch (Exception ex) {
        }
        return result;
    }

    // Check the lastName EditText if it's empty field
    private boolean checkLastName() {
        boolean result = false;
        try {
            result = !lastNameET.getText().toString().isEmpty();
        } catch (Exception ex) {
        }
        return result;
    }

    // Check the gradesAmount EditText if it's empty field and is in correct range
    private boolean checkGradesAmount() {
        boolean result = false;
        try {

            if (gradesAmountET.getText().toString().isEmpty() )
                result = false;
            else {
                int gradesAmount = Integer.parseInt(gradesAmountET.getText().toString());
                result = gradesAmount >= 5 && gradesAmount <= 15;
            }
        } catch (Exception ex) {
        }
        return result;
    }

    // Custom method for showing buttons at proper conditions
    private void showButton() {
            if (isAvgComputed) {
            firstNameET.setEnabled(false);
            lastNameET.setEnabled(false);
            gradesAmountET.setEnabled(false);
            submitBTN.setVisibility(View.GONE);
            if (computedAvg >= 3) {
                resultBTN.setText(getString(R.string.success));
            } else
                resultBTN.setText(getString(R.string.failure));
            avgResultTV.setText(String.format("Twoja średnia to: %1.2f", computedAvg));
            avgResultTV.setVisibility(View.VISIBLE);
            resultBTN.setVisibility(View.VISIBLE);
        } else {
            submitBTN.setVisibility(checkFirstName() && checkLastName() && checkGradesAmount() ? View.VISIBLE : View.GONE);
            resultBTN.setVisibility(View.GONE);
            avgResultTV.setVisibility(View.GONE);
        }
    }
}
