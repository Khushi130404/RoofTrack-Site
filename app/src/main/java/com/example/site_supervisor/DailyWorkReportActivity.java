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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DailyWorkReportActivity extends Activity
{

    TextView tvDate;
    List<DailyWorkReportPojo> report;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Balaji_Site_Supervisor.db";
    String path = dbPath+dbName;
    ListView listWork;
    ImageView imgAdd;
    String todayDate;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_work_report);

        listWork = findViewById(R.id.listWork);
        imgAdd = findViewById(R.id.imgAdd);
        tvDate = findViewById(R.id.tvDate);

        report = new ArrayList<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        todayDate = today.format(formatter);
        tvDate.setText(tvDate.getText().toString()+todayDate);

        try
        {
            db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        Cursor cur = db.rawQuery("select id,srno,ItemDetails,units,qty,workqty,Remarks from tbl_ProjectSite_Details_dailyReport where ProjectId = "+getIntent().getIntExtra("projectId",0),null);

        while(cur.moveToNext())
        {
            DailyWorkReportPojo dwp = new DailyWorkReportPojo();

            dwp.setId(cur.getInt(0));
            dwp.setSrno(cur.getInt(1));
            dwp.setItemDetails(cur.getString(2));
            dwp.setUnit(cur.getString(3));
            dwp.setQty(cur.getString(4));
            dwp.setWorkQty(cur.getString(5));
            dwp.setRemark(cur.getString(6));
            report.add(dwp);
        }

        DailyWorkReportAdapter reportAdapter = new DailyWorkReportAdapter(getApplicationContext(),R.layout.daily_work_report_adapter,report);
        listWork.setAdapter(reportAdapter);

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
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_add_report, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        EditText etSrno = popupView.findViewById(R.id.etSrno);
        EditText etItemDetails = popupView.findViewById(R.id.etItemDetails);
        EditText etUnit = popupView.findViewById(R.id.etUnit);
        EditText etProjectQty = popupView.findViewById(R.id.etProjectQty);
        EditText etQtyWork = popupView.findViewById(R.id.etQtyWork);
        EditText etRemark = popupView.findViewById(R.id.etRemark);
        Button btAdd = popupView.findViewById(R.id.btAdd);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DailyWorkReportPojo work = new DailyWorkReportPojo();

                try
                {
                    db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

                Cursor cur = db.rawQuery("select Max(id) from tbl_ProjectSite_Details_dailyReport",null);
                cur.moveToFirst();
                int id = cur.getInt(0)+1;

                work.setId(id);
                work.setSrno(Integer.parseInt(etSrno.getText().toString()));
                work.setItemDetails(etItemDetails.getText().toString());
                work.setUnit(etUnit.getText().toString());
                work.setQty(etProjectQty.getText().toString());
                work.setWorkQty(etQtyWork.getText().toString());
                work.setRemark(etRemark.getText().toString());

                report.add(work);

                ContentValues values = new ContentValues();
                values.put("id", id);
                values.put("ProjectID", getIntent().getIntExtra("projectId",0));
                values.put("srno", work.getSrno());
                values.put("ItemDetails", work.getItemDetails());
                values.put("units", work.getUnit());
                values.put("qty", work.getQty());
                values.put("workqty", work.getWorkQty());
                values.put("Remarks", work.getRemark());
                values.put("ProjectDate", todayDate);

                long newRowId = db.insert("tbl_ProjectSite_Details_dailyReport", null, values);

                if (newRowId == -1)
                {
                    Toast.makeText(getApplicationContext(), "Error inserting data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Data inserted with row ID: " + newRowId, Toast.LENGTH_SHORT).show();
                }

                db.close();
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
    }
}