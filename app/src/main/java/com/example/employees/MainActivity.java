package com.example.employees;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner;
    Connection connection;
    Button btnAdd;
    ImageView imageView;
    EditText findBySurname;
    static String id;

    View v;
    List<Mask> data;
    ListView listView;
    AdapterMask pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        v = findViewById(com.google.android.material.R.id.ghost_view);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((view -> {
            Intent intent = new Intent(MainActivity.this, AddData.class);
            startActivity(intent);
        }));

        findBySurname = findViewById(R.id.FindSurname);
        findBySurname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                getTextFromSQL(v, "Select * FROM Employees WHERE Surname LIKE '%" + findBySurname.getText() + "%'");
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
                    getTextFromSQL(v, "Select * FROM Employees");
                }

                if(spinner.getSelectedItemPosition() == 1) {
                    //Collections.sort(data, Collections.reverseOrder());
                    getTextFromSQL(v, "Select * FROM Employees ORDER BY Age ASC");
                }

                if(spinner.getSelectedItemPosition() == 2) {
                    getTextFromSQL(v, "Select * FROM Employees ORDER BY Age DESC");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        listView = findViewById(R.id.lvData);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                try {
                    id = String.valueOf(arg3);
                    ConnectionHelper dbHelper = new ConnectionHelper();
                    connection = dbHelper.connectionClass();
                    if (connection != null) {
                        Intent intent = new Intent(MainActivity.this, Change.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Проверьте подключение!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Возникла ошибка!", Toast.LENGTH_LONG).show();
                }
            }
        });

        getTextFromSQL(v, "Select * FROM Employees");
    }



    /*//Из строки в изображение
    private Bitmap getImgBitmap(String encodedImg) {
        if (encodedImg != null) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return BitmapFactory.decodeResource(DeteilsMask.this.getResources(),
                R.drawable.picture);
    }*/

    //Изображение в строку
    public static String encodeImage(Bitmap bitmap) {
        int prevW = 150;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();
        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return "";
    }

    public void enterMobile() {
        pAdapter.notifyDataSetInvalidated();
        listView.setAdapter(pAdapter);
    }

    public void getTextFromSQL(View v, String query) {
        data = new ArrayList<Mask>();
        pAdapter = new AdapterMask(MainActivity.this, data);
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    Mask tempMask = new Mask
                            (resultSet.getInt("Id"),
                                    resultSet.getString("Surname"),
                                    resultSet.getString("Firstname"),
                                    Integer.parseInt(resultSet.getString("Age")),
                                    resultSet.getString("Image")
                            );
                    data.add(tempMask);
                    pAdapter.notifyDataSetInvalidated();
                }
                connection.close();
            } else {
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        enterMobile();

    }

    @Override
    public void onClick(View v) {
        try {
            id = String.valueOf((v.getId()));
            ConnectionHelper dbHelper = new ConnectionHelper();
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