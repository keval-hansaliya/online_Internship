package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button login, signup;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    EditText email, password;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(20), EMAIL VARCHAR(20), CONTACT INT(10), PASSWORD VARCHAR(20), GENDER VARCHAR(6), CITY VARCHAR(15), DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        login = findViewById(R.id.main_login_button);

        email = findViewById(R.id.main_emailid);
        password = findViewById(R.id.main_password);

        signup = findViewById(R.id.main_signup_button);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().equals("")) {
                    email.setError("Email Id required");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Enter valid email address");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password required");
                } else if (password.getText().toString().trim().length()<6) {
                    password.setError("Minimum 6 characters required");
                } else {
                    System.out.println("Login successfully");

                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL = '"+email.getText().toString()+"' AND PASSWORD = '"+password.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    Log.d("CURSOR_COUNT", String.valueOf(cursor.getCount()));

                    if (cursor.getCount()>0) {

//                        Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                        startActivity(intent);

                        new CommonMethod(MainActivity.this, "Login successfully");
                        new CommonMethod(view, "Login Successfully");
                        new CommonMethod(MainActivity.this, HomeActivity.class);
                    } else {
                        new CommonMethod(MainActivity.this, "Wrong credentials");
                    }

                }
            }
        });
    }
}