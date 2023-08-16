package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    Button login, signup;

    EditText name, email, contact, password, confirmpassword, dob;

//    RadioButton male, female;
    RadioGroup gender;

    Calendar calendar;

    Spinner city;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", sCity = "", sGender;

    SQLiteDatabase db;

//    String[] cityarray = {"Ahmedabad", "vadodora", "Rajkot", "Bhavnagar", "Surat", "Veravad", "Dhoraji", "Kadi", "Patola"};
    ArrayList<String> arrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        login = findViewById(R.id.signup_login_button);
        signup = findViewById(R.id.signup_signup_button);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_emailid);
        contact = findViewById(R.id.signup_contactno);
        password = findViewById(R.id.signup_password);
        confirmpassword = findViewById(R.id.signup_confirm_password);
        dob = findViewById(R.id.signup_dob);
        calendar = Calendar.getInstance();

        db = openOrCreateDatabase("Internship", MODE_PRIVATE, null);

        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(20), EMAIL VARCHAR(20), CONTACT INT(10), PASSWORD VARCHAR(20), GENDER VARCHAR(6), CITY VARCHAR(15), DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-LLLL-yyyy", Locale.getDefault());
                dob.setText(sdf.format(calendar.getTime()));
            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(SignupActivity.this, dateClick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        arrayList = new ArrayList<>();

        arrayList.add("select your city");
        arrayList.add("Vadodara");
        arrayList.add("Surat");
        arrayList.add("Rajkot");
        arrayList.add("Bhavnagar");
        arrayList.add("Gandhinagar");
        arrayList.add("Ahmedabad");

        city = findViewById(R.id.signup_city);

        ArrayAdapter adapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_list_item_1, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0) {
                    sCity = "";
                }
                else {
                    sCity = arrayList.get(i);
                    new CommonMethod(SignupActivity.this, sCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gender = findViewById(R.id.signup_gender);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radiobutton = findViewById(i);
                sGender = radiobutton.getText().toString();
                new CommonMethod(SignupActivity.this, sGender);
                System.out.println(i);
            }
        });

        

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equals("")) {
                    name.setError("Name required");
                } else if (email.getText().toString().trim().equals("")) {
                    email.setError("Email Id required");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Enter valid email address");
                } else if (contact.getText().toString().trim().equals("")) {
                    contact.setError("Contact number required");
                } else if (contact.getText().toString().trim().length()<10) {
                    contact.setError("Valid contact number required");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password required");
                } else if (password.getText().toString().trim().length()<6) {
                    password.setError("Minimum 6 characters required");
                } else if (confirmpassword.getText().toString().trim().equals("")) {
                    password.setError("Confirm Password required");
                } else if (confirmpassword.getText().toString().trim().length()<6) {
                    password.setError("Minimum 6 characters required");
                } else if (!confirmpassword.getText().toString().trim().matches(password.getText().toString().trim())) {
                    confirmpassword.setError("Password does not match");
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(SignupActivity.this, "Please select gender");
                } else if (sCity.equals("")) {
                    new CommonMethod(SignupActivity.this, "Please select city");
                } else if (dob.getText().toString().trim().equals("")) {
                    dob.setError("Please select date of birth");
                } else {

                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL = '"+email.getText().toString()+"' OR CONTACT = '"+contact.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if(cursor.getCount() > 0) {
                        new CommonMethod(SignupActivity.this, "EmailId/Contact no. already registered");
                    } else {

                        String insertQuery = "INSERT INTO USERS VALUES(NULL, '" + name.getText().toString() + "', '" + email.getText().toString() + "', '" + contact.getText().toString() + "', '" + password.getText().toString() + "', '" + sGender + "', '" + sCity + "', '" + dob.getText().toString() + "')";
                        db.execSQL(insertQuery);

                        System.out.println("Signup successfully");
//                        Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                        startActivity(intent);

                        new CommonMethod(SignupActivity.this, "Signup successfully");
                        new CommonMethod(view, "Signup Successfully");
                        new CommonMethod(SignupActivity.this, HomeActivity.class);

                        onBackPressed();
                    }

                }
            }
        });
    }
}