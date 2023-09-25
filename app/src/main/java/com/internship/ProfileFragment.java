package com.internship;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;


import android.app.DatePickerDialog;
//import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ProfileFragment extends Fragment {

    SharedPreferences sp;

    Button logout, updateProfile, editProfile;

    EditText name, email, contact, dob;

    RadioButton male, female;

    RadioGroup gender;

    Calendar calendar;

    Spinner city;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", sCity = "", sGender;

    SQLiteDatabase db;

    //    String[] cityarray = {"Ahmedabad", "vadodora", "Rajkot", "Bhavnagar", "Surat", "Veravad", "Dhoraji", "Kadi", "Patola"};
    ArrayList<String> arrayList;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sp = getActivity().getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

//        name = view.findViewById()(R.id.home_name);
//        name.setText(sp.getString(ConstantSp.ID, "")+"\n"+
//                sp.getString(ConstantSp.NAME, "")+"\n"+
//                sp.getString(ConstantSp.EMAIL, "")+"\n"+
//                sp.getString(ConstantSp.CONTACT, "")+"\n"+
//                sp.getString(ConstantSp.PASSWORD, "")+"\n"+
//                sp.getString(ConstantSp.GENDER, "")+"\n"+
//                sp.getString(ConstantSp.CITY, "")+"\n"+
//                sp.getString(ConstantSp.DOB, ""));
        logout = view.findViewById(R.id.home_logout_button);
        updateProfile = view.findViewById(R.id.home_update_profile);

        name = view.findViewById(R.id.home_name);
        email = view.findViewById(R.id.home_emailid);
        contact = view.findViewById(R.id.home_contactno);
        dob = view.findViewById(R.id.home_dob);
        calendar = Calendar.getInstance();

        male = view.findViewById(R.id.home_male);
        female = view.findViewById(R.id.home_female);

        db = getActivity().openOrCreateDatabase("Internship", MODE_PRIVATE, null);

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
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), dateClick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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

        city = view.findViewById(R.id.home_city);

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
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
                    new CommonMethod(getActivity(), sCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gender = view.findViewById(R.id.home_gender);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radiobutton = view.findViewById(i);
                sGender = radiobutton.getText().toString();
                new CommonMethod(getActivity(), sGender);
                System.out.println(i);
            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                new CommonMethod(getActivity(), MainActivity.class);
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
                    new CommonMethod(getActivity(), "Please select gender");
                } else if (sCity.equals("")) {
                    new CommonMethod(getActivity(), "Please select city");
                } else if (dob.getText().toString().trim().equals("")) {
                    dob.setError("Please select date of birth");
                } else {

                    String selectQuery = "SELECT * FROM USERS WHERE USERID = '"+sp.getString(ConstantSp.ID, "")+"'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if(cursor.getCount() > 0) {
                        String updateQuery = "UPDATE USERS SET NAME = '"+name.getText().toString()+"', CONTACT = '"+contact.getText().toString()+"', GENDER = '"+sGender+"', CITY = '"+sCity+"', DOB = '"+dob.getText().toString()+"' WHERE USERID = '"+sp.getString(ConstantSp.ID, "")+"'";
                        db.execSQL(updateQuery);

                        sp.edit().putString(ConstantSp.NAME, name.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL, email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT, contact.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.GENDER, sGender).commit();
                        sp.edit().putString(ConstantSp.CITY, sCity).commit();
                        sp.edit().putString(ConstantSp.DOB, dob.getText().toString()).commit();

                        new CommonMethod(getActivity(), "Data updated");
                        setData(false);
                    } else {
                        new CommonMethod(getActivity(), "Invalid userId");
                    }

                }
            }
        });

        editProfile = view.findViewById(R.id.home_edit_profile);

        setData(false);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });
        return view;
    }

    private void setData(boolean isEnable) {

        name.setEnabled(isEnable);
        email.setEnabled(isEnable);
        email.setEnabled(isEnable);
        contact.setEnabled(isEnable);
        male.setEnabled(isEnable);
        female.setEnabled(isEnable);
        city.setEnabled(isEnable);
        dob.setEnabled(isEnable);

        if (isEnable) {
            updateProfile.setVisibility(View.VISIBLE);
            editProfile.setVisibility(View.GONE);
        }
        else {
            updateProfile.setVisibility(View.GONE);
            editProfile.setVisibility(View.VISIBLE);
        }

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
}