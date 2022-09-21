package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Change extends AppCompatActivity implements View.OnClickListener {

    Button btnBack;
    Button btnChange;
    Button btnDel;
    TextView txtSurname;
    TextView txtName;
    TextView txtAge;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((view -> {
            Intent intent = new Intent(Change.this, MainActivity.class);
            startActivity(intent);
        }));

        btnChange = findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);

        btnDel = findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);

        txtSurname = findViewById(R.id.Surname);
        txtSurname.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtSurname.setHint("");
            else
                txtSurname.setHint("Фамилия");
        });

        txtName = findViewById(R.id.Name);
        txtName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtName.setHint("");
            else
                txtName.setHint("Имя");
        });

        txtAge = findViewById(R.id.Age);
        txtAge.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtAge.setHint("");
            else
                txtAge.setHint("Возраст");
        });

        setText();
    }

    private void setText(){
        try {
            DBHelper dbHelper = new DBHelper();
            connection = dbHelper.connectionClass();

            if (connection != null) {
                String query = "Select * FROM Employees WHERE Id = " + MainActivity.id;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    txtSurname.setText(resultSet.getString(2));
                    txtName.setText(resultSet.getString(3));
                    txtAge.setText(resultSet.getString(4));
                }

                txtSurname.clearFocus();
                txtName.clearFocus();
                txtAge.clearFocus();

            } else {
                Toast.makeText(this, "Проверьте подключение!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Возникла ошибка!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        String query;

        switch (v.getId()) {
            case R.id.btnChange:
                String Surname = txtSurname.getText().toString();
                String Name = txtName.getText().toString();
                String Age = txtAge.getText().toString();

                query = "UPDATE Employees SET Surname = '" + Surname +
                        "', Firstname ='" + Name + "', Age = " + Age +
                        " WHERE Id = " + MainActivity.id;
                updateQuery(query, "Данные успешно изменены");
                break;

            case R.id.btnDel:
                query = "DELETE FROM Employees WHERE Id = " + MainActivity.id;
                updateQuery(query, "Сотрудник успешно удалён");

                Intent intent = new Intent(Change.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void updateQuery(String query, String yesMessage) {

        try {
            DBHelper dbHelper = new DBHelper();
            connection = dbHelper.connectionClass();

            if (connection != null) {
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);

                txtSurname.clearFocus();
                txtName.clearFocus();
                txtAge.clearFocus();

                Toast.makeText(this, yesMessage, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Проверьте подключение!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Возникла ошибка!", Toast.LENGTH_LONG).show();
        }
    }
}