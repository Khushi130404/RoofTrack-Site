package com.example.site_supervisor;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class DeliveryChallanActivity extends Activity {

    TextView tvDate;
//    List<BoltListPojo> bolt;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;
//    ListView listBolt;
    ImageView imgAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_challan);

//        listBolt = findViewById(R.id.listBolt);
        imgAdd = findViewById(R.id.imgAdd);
        tvDate = findViewById(R.id.tvDate);

//        bolt = new ArrayList<>();

        tvDate.setText(tvDate.getText().toString()+getIntent().getStringExtra("date"));

        try
        {
            db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}