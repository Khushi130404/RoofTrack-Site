package com.example.site_supervisor;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MaterialConsumptionActivity extends Activity
{
    TextView tvDate;
    List<MaterialConsumptionPojo> material;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;
    ListView listWork;
    ImageView imgAdd;
    String todayDate;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_consumption);

        listWork = findViewById(R.id.listWork);
        imgAdd = findViewById(R.id.imgAdd);
        tvDate = findViewById(R.id.tvDate);

        material = new ArrayList<>();

        tvDate.setText(tvDate.getText().toString()+getIntent().getStringExtra("date"));

        try
        {
            db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        Cursor cur = db.rawQuery("select id,assembly_mark,name,net_weight from tbl_billofmaterialdetails where ProjectId = "+getIntent().getIntExtra("projectId",0)+" and date = '"+getIntent().getStringExtra("date")+"'",null);

        while (cur.moveToNext())
        {
            MaterialConsumptionPojo mcp = new MaterialConsumptionPojo();
            mcp.setId(cur.getInt(0));
            mcp.setAssemblyMark(cur.getString(1));
            mcp.setName(cur.getString(2));
            mcp.setWeight(cur.getDouble(3));
            material.add(mcp);
        }
    }
}