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

public class BoltListActivity extends Activity
{
    TextView tvDate;
    List<BoltListPojo> bolt;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;
    ListView listBolt;
    ImageView imgAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolt_list);

        listBolt = findViewById(R.id.listBolt);
        imgAdd = findViewById(R.id.imgAdd);
        tvDate = findViewById(R.id.tvDate);

        bolt = new ArrayList<>();

        tvDate.setText(tvDate.getText().toString()+getIntent().getStringExtra("date"));

        try
        {
            db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        Cursor cur = db.rawQuery("select id,type,qty from tbl_boltlist where ProjectId = "+getIntent().getIntExtra("projectId",0)+" and date like '%"+getIntent().getStringExtra("date")+"%'",null);

        while (cur.moveToNext())
        {
            BoltListPojo blp = new BoltListPojo();
            blp.setId(cur.getInt(0));
            blp.setType(cur.getString(1));
            blp.setQty(cur.getInt(2));
            bolt.add(blp);
        }

        BoltListAdapter boltListAdapter = new BoltListAdapter(getApplicationContext(),R.layout.bolt_list_adapter,bolt);
        listBolt.setAdapter(boltListAdapter);

        db.close();


    }
}