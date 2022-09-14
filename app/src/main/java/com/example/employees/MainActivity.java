package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.ConnectionRequest;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Connection connection;
    String ConnectionResult = "";
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((view -> {
            Intent intent = new Intent(MainActivity.this,AddData.class);
            startActivity(intent);
        }));

        getTextFromSQL();
    }

    public void getTextFromSQL() {
        try {
            DBHelper dbHelper = new DBHelper();
            connection = dbHelper.connectionClass();

            if (connection != null) {
                String query = "Select * FROM Employees";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                printHeader();


                TableLayout dbOutput = findViewById(R.id.dbOutput);
                while (resultSet.next()) {
                    TableRow dbOutputRow = new TableRow(this);
                    dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    TableRow.LayoutParams params = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    TextView outputID = new TextView(this);
                    params.weight = 1.0f;
                    outputID.setLayoutParams(params);
                    outputID.setText(resultSet.getString(1));
                    dbOutputRow.addView(outputID);

                    TextView outputSurname = new TextView(this);
                    params.weight = 3.0f;
                    outputSurname.setLayoutParams(params);
                    outputSurname.setText(resultSet.getString(2));
                    dbOutputRow.addView(outputSurname);

                    TextView outputFirstname = new TextView(this);
                    params.weight = 3.0f;
                    outputFirstname.setLayoutParams(params);
                    outputFirstname.setText(resultSet.getString(3));
                    dbOutputRow.addView(outputFirstname);

                    TextView outputAge = new TextView(this);
                    params.weight = 2.0f;
                    outputAge.setLayoutParams(params);
                    outputAge.setText(resultSet.getString(4));
                    dbOutputRow.addView(outputAge);

                    dbOutput.addView(dbOutputRow);

                }

            } else {
                ConnectionResult = "Check connection";
            }
        } catch (Exception ex) {

        }
    }

    public void printHeader() {
        TableLayout dbHeaders = findViewById(R.id.dbOutput);
        dbHeaders.removeAllViews();
        TableRow dbOutputRow = new TableRow(this);
        dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams params = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView outputID = new TextView(this);
        params.weight = 1.0f;
        outputID.setLayoutParams(params);
        outputID.setText("№\n");
        dbOutputRow.addView(outputID);

        TextView outputSurname = new TextView(this);
        params.weight = 3.0f;
        outputSurname.setLayoutParams(params);
        outputSurname.setText("Фамилия\n");
        dbOutputRow.addView(outputSurname);

        TextView outputFirstname = new TextView(this);
        params.weight = 3.0f;
        outputFirstname.setLayoutParams(params);
        outputFirstname.setText("Имя\n");
        dbOutputRow.addView(outputFirstname);

        TextView outputAge = new TextView(this);
        params.weight = 2.0f;
        outputAge.setLayoutParams(params);
        outputAge.setText("Возраст\n");
        dbOutputRow.addView(outputAge);

        dbHeaders.addView(dbOutputRow);
    }

}