package com.example.site_supervisor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class GenerateOTPActivity extends Activity
{
    Button btVerify,btSendAgain;
    EditText[] et;
    int etId[] = {R.id.et1,R.id.et2,R.id.et3,R.id.et4};
    Random random;
    SharedPreferences share;
    String phone;
    TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_otpactivity);

        int otp = getIntent().getIntExtra("otp",0000);
        btVerify = findViewById(R.id.btVerify);
        btSendAgain = findViewById(R.id.btSendAgain);
        tvPhone = findViewById(R.id.tvPhone);

        share = getSharedPreferences("siteSupervisor",MODE_PRIVATE);
        phone = share.getString("phone","0");

        tvPhone.setText(tvPhone.getText().toString()+phone);

        et = new EditText[4];
        random = new Random();

        for(int i=0; i<etId.length; i++)
        {
            et[i] = findViewById(etId[i]);
        }

        btSendAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int newOtp = random.nextInt(9999);
                while(newOtp<1000)
                {
                    newOtp = random.nextInt(9999);
                }

                Intent i = new Intent(getApplicationContext(),GenerateOTPActivity.class);
                i.putExtra("otp",newOtp);

                SmsManager sms=SmsManager.getDefault();
                String message = "Your OTP for LogIn is "+newOtp;
                sms.sendTextMessage(phone, null, message, null,null);
                startActivity(i);
                finish();
            }
        });

        btVerify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String verify= "";
                for(int i=0; i<4; i++)
                {
                    verify+=et[i].getText().toString();
                }
                if(verify.equals(""+otp))
                {
                    if(share.getInt("remember",0)==1)
                    {
                        SharedPreferences.Editor edit = share.edit();
                        edit.putInt("remember",2);
                        edit.apply();
                    }
                    Intent i = new Intent(getApplicationContext(),OnSiteDashboardActivity.class);
                    startActivity(i);
                    finish();
                    //Toast.makeText(getApplicationContext(), "Correct OTP", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(share.getInt("remember",0)==1)
                    {
                        SharedPreferences.Editor edit = share.edit();
                        edit.putInt("remember",0);
                        edit.apply();
                    }
                    Toast.makeText(getApplicationContext(), "Incorrect OTP...!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}