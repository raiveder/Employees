package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telecom.ConnectionRequest;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Connection connection;
    Button btnAdd;
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

        getTextFromSQL();
    }

    private void getTextFromSQL() {
        try {
            DBHelper dbHelper = new DBHelper();
            connection = dbHelper.connectionClass();

            if (connection != null) {
                String query = "Select * FROM Employees";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                printHeader();

                int id = 1;
                TableLayout dbOutput = findViewById(R.id.dbOutput);
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
                    params.bottomMargin = (int) 10;
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
            DBHelper dbHelper = new DBHelper();
            connection = dbHelper.connectionClass();
            id = String.valueOf((v.getId()));
            if (connection != null) {
                    Intent intent = new Intent(MainActivity.this, Change.class);
                    startActivity(intent);
            } else {
                Toast.makeText(this, "Проверьте подключение!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Возникла ошибка!", Toast.LENGTH_LONG).show();
        }

        getTextFromSQL();
    }
}