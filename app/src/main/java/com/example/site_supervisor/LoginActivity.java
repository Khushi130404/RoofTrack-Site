package com.example.site_supervisor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoginActivity extends Activity
{
    Spinner sp;
    EditText etPhone;
    CheckBox cbRemember;
    Random random;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    SQLiteDatabase db = null;
    Button btSignIn;
    String path;
    SharedPreferences share;
    List<String> company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        share = getSharedPreferences("siteSupervisor",MODE_PRIVATE);

        if(share.getInt("remember",0)==2)
        {
            Intent i = new Intent(getApplicationContext(),OnSiteDashboardActivity.class);
            startActivity(i);
            finish();
        }

        company = new ArrayList<>();
        company.add("Shri Balaji Roofing");
        company.add("Steelonn Infrastructure");

        sp = findViewById(R.id.sp);
        etPhone = findViewById(R.id.etPhone);
        cbRemember = findViewById(R.id.cbRemember);
        btSignIn = findViewById(R.id.btSignIn);

        ArrayAdapter<String> ad = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,company);
        sp.setAdapter(ad);

        path = dbPath+dbName;
        random = new Random();

        btSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Database does'nt exists",Toast.LENGTH_SHORT).show();
                }

                String phone = etPhone.getText().toString();
                Cursor cur = db.rawQuery("select * from crm_user_registration where usertype = 'Site Supervisor'",null);
                boolean flag = false;
                while(cur.moveToNext())
                {
                    String temp_phone = cur.getString(14);
                    if(temp_phone.equals(phone))
                    {
                        flag = true;
                        //Toast.makeText(getApplicationContext(),"Phone "+phone,Toast.LENGTH_LONG).show();

                        int otp = random.nextInt(9999);
                        while(otp<1000)
                        {
                            otp = random.nextInt(9999);
                        }
                        Intent i = new Intent(getApplicationContext(),GenerateOTPActivity.class);
                        if(cbRemember.isChecked())
                        {
                            SharedPreferences.Editor edit = share.edit();
                            edit.putInt("remember",1);
                            edit.apply();
                        }
                        SharedPreferences.Editor edit = share.edit();
                        edit.putString("phone",phone);
                        edit.putString("company",sp.getSelectedItem().toString());
                        edit.apply();
                        i.putExtra("otp",otp);
                        SmsManager sms=SmsManager.getDefault();
                        String message = "Your OTP for LogIn is "+otp;
                        sms.sendTextMessage(phone, null, message, null,null);
                        startActivity(i);
                        break;
                    }
                }

                db.close();

                if(!flag)
                {
                    Toast.makeText(getApplicationContext(),"Phone not found",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}