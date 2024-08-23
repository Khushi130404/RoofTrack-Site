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

                Cursor cur = null;

                try
                {
                    cur = db.rawQuery("select id from tbl_boltlist where upper(type) like '%"+etBolt.getText().toString().toUpperCase()+"%'",null);
                    cur.moveToFirst();
                    int id = cur.getInt(0);

                    cur = db.rawQuery("select Max(id) from tbl_boltlist",null);
                    cur.moveToFirst();
                    id = cur.getInt(0)+1;

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

                    cur.close();
                    db.close();
                    bolt.add(blp);
                    popupWindow.dismiss();
                }
                catch (NumberFormatException nfe)
                {
                    cur.close();
                    db.close();
                    Toast.makeText(getApplicationContext(),"Qty should be Integer...!",Toast.LENGTH_SHORT).show();
                }
                catch (EmptyStringException ese)
                {
                    cur.close();
                    db.close();
                    Toast.makeText(getApplicationContext(),ese.toString(),Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    cur.close();
                    db.close();
                    Toast.makeText(getApplicationContext(),"Bolt type doesn't exist...!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
    }

}