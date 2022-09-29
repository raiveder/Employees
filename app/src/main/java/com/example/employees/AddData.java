package com.example.employees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Statement;

public class AddData extends AppCompatActivity implements View.OnClickListener {

    Button btnBack;
    Button btnAdd;
    TextView txtSurname;
    TextView txtName;
    TextView txtAge;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((view -> {
            Intent intent = new Intent(AddData.this, MainActivity.class);
            startActivity(intent);
        }));

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        txtSurname = findViewById(R.id.FindSurname);
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

    }

    @Override
    public void onClick(View v) {
        String Surname = txtSurname.getText().toString();
        String Name = txtName.getText().toString();
        String Age = txtAge.getText().toString();

        switch (v.getId()) {

            case R.id.btnAdd:
                try {
                    ConnectionHelper dbHelper = new ConnectionHelper();
                    connection = dbHelper.connectionClass();

                    if (connection != null) {
                        String query = "INSERT INTO Employees VALUES('" + Surname + "', '" + Name +
                                "', " + Age + ")";
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(query);

                        txtSurname.setText(null);
                        txtName.setText(null);
                        txtAge.setText(null);

                        txtSurname.clearFocus();
                        txtName.clearFocus();
                        txtAge.clearFocus();

                        Toast.makeText(this, "Сотрудник успешно добавлен", Toast.LENGTH_LONG).show();
                        break;
                    } else {
                        Toast.makeText(this, "Проверьте подключение!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(this, "Возникла ошибка!", Toast.LENGTH_LONG).show();
                }
        }
    }
}