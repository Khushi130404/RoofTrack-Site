package com.example.site_supervisor;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

import java.util.List;

public class StockMaterialActivity extends Activity {

    TextView tvDate;
    List<StockMaterialPojo> stock;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;
    ListView listStock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_material);

        tvDate = findViewById(R.id.tvDate);
        listStock = findViewById(R.id.listStock);

        try
        {
            db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        Cursor cur = db.rawQuery("select itemcode,itemname, sum(cast(qty as real)) from tbl_dc_details group by itemcode,itemname",null);
        int id = 0;
        while(cur.moveToNext())
        {
            id++;
            StockMaterialPojo smp = new StockMaterialPojo();
            smp.setPos(id);
            smp.setCode(cur.getString(0));
            smp.setItemname(cur.getString(1));
            smp.setDcQty(cur.getFloat(3));
            Cursor cur2 = db.rawQuery("select ifnull(sum(qty),0) from tbl_billofmaterialdetails where ProjectId = "+getIntent().getIntExtra("projectId",0)+" and assembly_mark = '"+cur.getString(0)+"'",null);
            cur2.moveToFirst();
            smp.setUsedQty(cur.getInt(0));
            smp.setStockQty(smp.getDcQty()-smp.getUsedQty());
        }

        StockMaterialAdapter stockMaterialAdapter = new StockMaterialAdapter(getApplicationContext(),R.layout.stock_material_adapter,stock);
        listStock.setAdapter(stockMaterialAdapter);
        db.close();
    }
}