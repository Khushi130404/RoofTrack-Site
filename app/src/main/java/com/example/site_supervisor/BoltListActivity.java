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
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_add_bolt, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        EditText etBolt = popupView.findViewById(R.id.etBolt);
        EditText etQty = popupView.findViewById(R.id.etQty);
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

                Cursor cur = db.rawQuery("select Max(id) from tbl_boltlist",null);
                cur.moveToFirst();
                int id = cur.getInt(0)+1;

                try
                {
                    BoltListPojo blp = new BoltListPojo();
                    blp.setId(id);
                    if(etBolt.getText().toString().equals(""))
                    {
                        throw new EmptyStringException();
                    }
                    blp.setType(etBolt.getText().toString().toUpperCase());
                    blp.setQty(Integer.parseInt(etQty.getText().toString()));

                    ContentValues values = new ContentValues();
                    values.put("id", id);
                    values.put("ProjectID", getIntent().getIntExtra("projectId",0));
                    values.put("type",blp.getType());
                    values.put("qty",blp.getQty());
                    values.put("date",getIntent().getStringExtra("date"));

                    long newRowId = db.insert("tbl_boltlist", null, values);

                    if (newRowId == -1)
                    {
                        Toast.makeText(getApplicationContext(), "Error inserting data", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Data inserted with row ID: " + newRowId, Toast.LENGTH_SHORT).show();
                    }

                    db.close();
                    bolt.add(blp);
                    popupWindow.dismiss();
                }
                catch (NumberFormatException nfe)
                {
                    Toast.makeText(getApplicationContext(),"Qty should be Integer...!",Toast.LENGTH_SHORT).show();
                }
                catch (EmptyStringException ese)
                {
                    Toast.makeText(getApplicationContext(),ese.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
    }
}