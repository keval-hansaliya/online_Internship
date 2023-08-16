package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sp;

    Button logout, updateProfile;

    EditText name, email, contact, dob;

    RadioButton male, female;

    RadioGroup gender;

    Calendar calendar;

    Spinner city;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", sCity = "", sGender;

    SQLiteDatabase db;

    //    String[] cityarray = {"Ahmedabad", "vadodora", "Rajkot", "Bhavnagar", "Surat", "Veravad", "Dhoraji", "Kadi", "Patola"};
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

//        name = findViewById(R.id.home_name);
//        name.setText(sp.getString(ConstantSp.ID, "")+"\n"+
//                sp.getString(ConstantSp.NAME, "")+"\n"+
//                sp.getString(ConstantSp.EMAIL, "")+"\n"+
//                sp.getString(ConstantSp.CONTACT, "")+"\n"+
//                sp.getString(ConstantSp.PASSWORD, "")+"\n"+
//                sp.getString(ConstantSp.GENDER, "")+"\n"+
//                sp.getString(ConstantSp.CITY, "")+"\n"+
//                sp.getString(ConstantSp.DOB, ""));
        logout = findViewById(R.id.home_logout_button);
        updateProfile = findViewById(R.id.home_update_profile);

        name = findViewById(R.id.home_name);
        email = findViewById(R.id.home_emailid);
        contact = findViewById(R.id.home_contactno);
        dob = findViewById(R.id.home_dob);
        calendar = Calendar.getInstance();

        male = findViewById(R.id.home_male);
        female = findViewById(R.id.home_female);

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
                DatePickerDialog dpd = new DatePickerDialog(HomeActivity.this, dateClick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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

        city = findViewById(R.id.home_city);

        ArrayAdapter adapter = new ArrayAdapter(HomeActivity.this, android.R.layout.simple_list_item_1, arrayList);
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
                    new CommonMethod(HomeActivity.this, sCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gender = findViewById(R.id.home_gender);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radiobutton = findViewById(i);
                sGender = radiobutton.getText().toString();
                new CommonMethod(HomeActivity.this, sGender);
                System.out.println(i);
            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                new CommonMethod(HomeActivity.this, MainActivity.class);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
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
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(HomeActivity.this, "Please select gender");
                } else if (sCity.equals("")) {
                    new CommonMethod(HomeActivity.this, "Please select city");
                } else if (dob.getText().toString().trim().equals("")) {
                    dob.setError("Please select date of birth");
                } else {

                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL = '"+email.getText().toString()+"' OR CONTACT = '"+contact.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if(cursor.getCount() > 0) {
                        new CommonMethod(HomeActivity.this, "EmailId/Contact no. already registered");
                    } else {

                        String insertQuery = "INSERT INTO USERS VALUES(NULL, '" + name.getText().toString() + "', '" + email.getText().toString() + "', '" + contact.getText().toString() + "', '" + sGender + "', '" + sCity + "', '" + dob.getText().toString() + "')";
                        db.execSQL(insertQuery);

                        new CommonMethod(HomeActivity.this, "Signup successfully");
                        new CommonMethod(view, "Signup Successfully");
                        new CommonMethod(HomeActivity.this, HomeActivity.class);
                        onBackPressed();
                    }

                }
            }
        });

        setData();
    }

    private void setData() {
        Log.d("test", "test");
        name.setText(sp.getString(ConstantSp.NAME, ""));
        email.setText(sp.getString(ConstantSp.EMAIL, ""));
        contact.setText(sp.getString(ConstantSp.CONTACT, ""));
        dob.setText(sp.getString(ConstantSp.DOB, ""));

//        male.setChecked(true);

        sGender = sp.getString(ConstantSp.GENDER, "");
        if (sGender.equalsIgnoreCase("Female")) {
            female.setChecked(true);
        }
        else if (sGender.equalsIgnoreCase("MAle")) {
            male.setChecked(true);
        }

        sCity = sp.getString(ConstantSp.CITY, "");
        int sPosition = 0;
        for (int i=0; i<arrayList.size(); i++) {
            if (sCity.equalsIgnoreCase(arrayList.get(i))) {
                sPosition = i;
            }
        }

        city.setSelection(sPosition, true);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finishAffinity();
    }
}