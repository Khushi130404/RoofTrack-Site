package com.example.site_supervisor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySQLiteHelper ms = new MySQLiteHelper(getApplicationContext());
        ms.checkDatabase();
        //ms.deleteDatabase();

        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }
}