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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class DeliveryChallanActivity extends Activity {

    TextView tvDate;
    List<DeliveryChallanPojo> challan;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;
    ListView listChallan;
    ImageView imgAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_challan);

        listChallan = findViewById(R.id.listChallan);
        imgAdd = findViewById(R.id.imgAdd);
        tvDate = findViewById(R.id.tvDate);

        challan = new ArrayList<>();
        tvDate.setText(tvDate.getText().toString()+getIntent().getStringExtra("date"));

        try
        {
            db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        Cursor cur = db.rawQuery("select id from tbl_dc where dsdate like '%"+getIntent().getStringExtra("date")+"%'",null);

        while (cur.moveToNext())
        {
            Cursor cur2 = db.rawQuery("select id,itemcode,itemname,uom,qty from tbl_dc_details where parentid = "+cur.getInt(0),null);

            while (cur2.moveToNext())
            {
                DeliveryChallanPojo dcp = new DeliveryChallanPojo();
                dcp.setId(cur2.getInt(0));
                dcp.setCode(cur2.getString(1));
                dcp.setItemName(cur2.getString(2));
                dcp.setUnit(cur2.getString(3));
                dcp.setQty(Float.parseFloat(cur2.getString(4)));
                challan.add(dcp);
            }
        }

        DeliveryChallanAdapter deliveryChallanAdapter = new DeliveryChallanAdapter(getApplicationContext(), R.layout.delivery_challan_adapter,challan);
        listChallan.setAdapter(deliveryChallanAdapter);

        db.close();
    }
}