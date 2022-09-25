package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner;
    Connection connection;
    Button btnAdd;
    TableLayout dbOutput;
    EditText findBySurname;
    static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((view -> {
            Intent intent = new Intent(MainActivity.this, AddData.class);
            startActivity(intent);
        }));
        dbOutput = findViewById(R.id.dbOutput);

        findBySurname = findViewById(R.id.FindSurname);
        findBySurname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                getTextFromSQL("Select * FROM Employees WHERE Surname LIKE '%" + findBySurname.getText() + "%'");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                findBySurname.setText("");

                if(spinner.getSelectedItemPosition() == 0) {
                    getTextFromSQL("Select * FROM Employees");
                }

                if(spinner.getSelectedItemPosition() == 1) {
                    getTextFromSQL("Select * FROM Employees ORDER BY Age ASC");
                }

                if(spinner.getSelectedItemPosition() == 2) {
                    getTextFromSQL("Select * FROM Employees ORDER BY Age DESC");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        getTextFromSQL("Select * FROM Employees");
    }

    private void getTextFromSQL(String query) {
        try {
            DBHelper dbHelper = new DBHelper();
            connection = dbHelper.connectionClass();

            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                dbOutput.removeAllViews();
                printHeader();

                int id = 1;
                while (resultSet.next()) {
                    TableRow dbOutputRow = new TableRow(this);
                    dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    TableRow.LayoutParams params = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    TextView outputID = new TextView(this);
                    params.weight = 1.0f;
                    outputID.setLayoutParams(params);
                    outputID.setText(String.valueOf(id++));
                    outputID.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    dbOutputRow.addView(outputID);

                    TextView outputSurname = new TextView(this);
                    params.weight = 3.0f;
                    outputSurname.setLayoutParams(params);
                    outputSurname.setText(resultSet.getString(2));
                    outputSurname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    dbOutputRow.addView(outputSurname);

                    TextView outputFirstname = new TextView(this);
                    params.weight = 3.0f;
                    outputFirstname.setLayoutParams(params);
                    outputFirstname.setText(resultSet.getString(3));
                    outputFirstname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    dbOutputRow.addView(outputFirstname);

                    TextView outputAge = new TextView(this);
                    params.weight = 2.0f;
                    outputAge.setLayoutParams(params);
                    outputAge.setText(resultSet.getString(4));
                    outputAge.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    dbOutputRow.addView(outputAge);

                    Button changeBtn = new Button(this);
                    changeBtn.setOnClickListener(this);
                    params.weight = 1.0f;
                    params.width = 1;
                    params.bottomMargin = 10;
                    changeBtn.setLayoutParams(params);
                    changeBtn.setBackgroundResource(R.drawable.buttonbackground);
                    changeBtn.setText("-->");
                    changeBtn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    changeBtn.setId(resultSet.getInt(1));
                    dbOutputRow.addView(changeBtn);

                    dbOutput.addView(dbOutputRow);
                }

            } else {
                Toast.makeText(this, "Проверьте подключение!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Возникла ошибка!", Toast.LENGTH_LONG).show();
        }
    }

    private void printHeader() {
        TableLayout dbHeaders = findViewById(R.id.dbOutput);
        dbHeaders.removeAllViews();
        TableRow dbHeadersRow = new TableRow(this);
        dbHeadersRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams params = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView outputID = new TextView(this);
        params.weight = 1.0f;
        outputID.setLayoutParams(params);
        outputID.setText("№\n");
        outputID.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dbHeadersRow.addView(outputID);

        TextView outputSurname = new TextView(this);
        params.weight = 3.0f;
        outputSurname.setLayoutParams(params);
        outputSurname.setText("Фамилия\n");
        outputSurname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        dbHeadersRow.addView(outputSurname);

        TextView outputFirstname = new TextView(this);
        params.weight = 3.0f;
        outputFirstname.setLayoutParams(params);
        outputFirstname.setText("Имя\n");
        outputFirstname.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        dbHeadersRow.addView(outputFirstname);

        TextView outputAge = new TextView(this);
        params.weight = 2.0f;
        outputAge.setLayoutParams(params);
        outputAge.setText("Возраст\n");
        outputAge.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dbHeadersRow.addView(outputAge);

        TextView outputBtn = new TextView(this);
        params.weight = 1.0f;
        outputBtn.setLayoutParams(params);
        dbHeadersRow.addView(outputBtn);

        dbHeaders.addView(dbHeadersRow);
    }

    @Override
    public void onClick(View v) {
        try {
            id = String.valueOf((v.getId()));
            DBHelper dbHelper = new DBHelper();
            connection = dbHelper.connectionClass();
            if (connection != null) {
                    Intent intent = new Intent(MainActivity.this, Change.class);
                    startActivity(intent);
            } else {
                Toast.makeText(this, "Проверьте подключение!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Возникла ошибка!", Toast.LENGTH_LONG).show();
        }
    }
}