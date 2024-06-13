package com.example.site_supervisor;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
    ListView listMaterial;
    ImageView imgAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_consumption);

        listMaterial = findViewById(R.id.listMaterial);
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

        Cursor cur = db.rawQuery("select id,assembly_mark,name,net_weight from tbl_billofmaterialdetails where ProjectId = "+getIntent().getIntExtra("projectId",0)+" and date like '%"+getIntent().getStringExtra("date")+"%'",null);

        while (cur.moveToNext())
        {
            MaterialConsumptionPojo mcp = new MaterialConsumptionPojo();
            mcp.setId(cur.getInt(0));
            mcp.setAssemblyMark(cur.getString(1));
            mcp.setName(cur.getString(2));
            mcp.setWeight(cur.getDouble(3));
            material.add(mcp);
        }

        MaterialConsumptionAdapter materialAdapter = new MaterialConsumptionAdapter(getApplicationContext(),R.layout.material_consumption_adapter,material);
        listMaterial.setAdapter(materialAdapter);

        db.close();

        imgAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPopupMenu(v);
            }
        });

    }

    private void showPopupMenu(View view)
    {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_add_material, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        EditText etAssemblyMark = popupView.findViewById(R.id.etAssemblyMark);
        EditText etWeight = popupView.findViewById(R.id.etWeight);
        Button btAdd = popupView.findViewById(R.id.btAdd);

        btAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

                Cursor cur = db.rawQuery("select Max(id) from tbl_billofmaterialdetails",null);
                cur.moveToFirst();
                int id = cur.getInt(0)+1;

                cur = db.rawQuery("select name from tbl_billofmaterialdetails where lower(assembly_mark) = '"+etAssemblyMark.getText().toString().toLowerCase()+"'",null);
                cur.moveToFirst();
                MaterialConsumptionPojo mcp = new MaterialConsumptionPojo();
                mcp.setId(id);
                mcp.setAssemblyMark(etAssemblyMark.getText().toString().toUpperCase());
                mcp.setName(cur.getString(0));
                mcp.setWeight(Double.parseDouble(etWeight.getText().toString()));

                ContentValues values = new ContentValues();
                values.put("id", id);
                values.put("ProjectID", getIntent().getIntExtra("projectId",0));
                values.put("assembly_mark",mcp.getAssemblyMark());
                values.put("name",mcp.getName());
                values.put("net_weight",mcp.getWeight());
                values.put("date",getIntent().getStringExtra("date"));

                long newRowId = db.insert("tbl_billofmaterialdetails", null, values);

                if (newRowId == -1)
                {
                    Toast.makeText(getApplicationContext(), "Error inserting data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Data inserted with row ID: " + newRowId, Toast.LENGTH_SHORT).show();
                }

                db.close();
                material.add(mcp);

                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
    }

}