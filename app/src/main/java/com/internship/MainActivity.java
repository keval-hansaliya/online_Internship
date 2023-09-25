package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button login, signup;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    EditText email, password;

    ImageView passwordHide, passwordShow;

    SQLiteDatabase db;

    SharedPreferences sp;

    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        db = openOrCreateDatabase("Internship", MODE_PRIVATE, null);

        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(20), EMAIL VARCHAR(20), CONTACT INT(10), PASSWORD VARCHAR(20), GENDER VARCHAR(6), CITY VARCHAR(15), DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        login = findViewById(R.id.main_login_button);

        email = findViewById(R.id.main_emailid);
        password = findViewById(R.id.main_password);

        remember = findViewById(R.id.main_remember);

        signup = findViewById(R.id.main_signup_button);

        passwordShow = findViewById(R.id.main_password_show);
        passwordHide = findViewById(R.id.main_password_hide);

        passwordHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordHide.setVisibility(View.GONE);
                passwordShow.setVisibility(View.VISIBLE);

                password.setTransformationMethod(null);
            }
        });

        passwordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordShow.setVisibility(View.GONE);
                passwordHide.setVisibility(View.VISIBLE);

                password.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

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

                        while(cursor.moveToNext()) {
                            String sUserId = cursor.getString(0);
                            String sName = cursor.getString(1);
                            String sEmail = cursor.getString(2);
                            String sContact = cursor.getString(3);
                            String sPassword = cursor.getString(4);
                            String sGender = cursor.getString(5);
                            String sCity = cursor.getString(6);
                            String sDob = cursor.getString(7);

                            sp.edit().putString(ConstantSp.ID, sUserId).commit();
                            sp.edit().putString(ConstantSp.NAME, sName).commit();
                            sp.edit().putString(ConstantSp.EMAIL, sEmail).commit();
                            sp.edit().putString(ConstantSp.CONTACT, sContact).commit();
                            sp.edit().putString(ConstantSp.PASSWORD, sPassword).commit();
                            sp.edit().putString(ConstantSp.GENDER, sGender).commit();
                            sp.edit().putString(ConstantSp.CITY, sCity).commit();
                            sp.edit().putString(ConstantSp.DOB, sDob).commit();
                            if (remember.isChecked()) {
                                sp.edit().putString(ConstantSp.REMEMBER, "yes").commit();
                            } else {
                                sp.edit().putString(ConstantSp.REMEMBER, "").commit();
                            }


                            Log.d("User_data", sUserId+"\n"+sName+"\n"+sEmail+"\n"+sContact+"\n"+sPassword+"\n"+sGender+"\n"+sCity+"\n"+sDob);
                        }

//                        Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                        startActivity(intent);

                        new CommonMethod(MainActivity.this, "Login successfully");
                        new CommonMethod(view, "Login Successfully");
                        new CommonMethod(MainActivity.this, DashboardActivity.class);
                    } else {
                        new CommonMethod(MainActivity.this, "Wrong credentials");
                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finishAffinity();
    }
}