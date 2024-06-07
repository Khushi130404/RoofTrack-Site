package com.example.site_supervisor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyProjectDetailsActivity extends Activity
{
    Spinner spProject;
    TextView tvCustomer, tvPONo;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Balaji_Site_Supervisor.db";
    String path;
    SpinnerAdapter spinnerAdapter;
    Button btAttendance, btWorkReport, btPrevAttendance;
    DatePicker date;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_project_details);

        spProject = findViewById(R.id.spProject);
        tvCustomer = findViewById(R.id.tvCustomer);
        tvPONo = findViewById(R.id.tvPONo);
        btAttendance = findViewById(R.id.btAttendance);
        btWorkReport = findViewById(R.id.btWorkReport);
        btPrevAttendance = findViewById(R.id.btPrevAttendance);
        date = findViewById(R.id.date);

        path = dbPath+dbName;

        try
        {
            db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Cursor cur = db.rawQuery("select ProjectName from tbl_ProjectSite",null);

        List<String> projectName = new ArrayList<>();

        while (cur.moveToNext())
        {
            projectName.add(cur.getString(0));
        }
        db.close();
        spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,projectName);
        spProject.setAdapter(spinnerAdapter);

        spProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                Cursor cur = db.rawQuery("select CustomerName,CustomerPO from tbl_ProjectSite where ProjectName = '"+projectName.get(position)+"'",null);
                cur.moveToFirst();
                tvCustomer.setText(cur.getString(0));
                tvPONo.setText(cur.getString(1));

                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        btPrevAttendance.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Cursor cur = db.rawQuery("select ProjectID from tbl_ProjectSite where ProjectName = '"+spProject.getSelectedItem().toString()+"'",null);
                cur.moveToFirst();

                Intent i = new Intent(getApplicationContext(), PreviousAttendanceActivity.class);
                i.putExtra("date",""+addZero(date.getDayOfMonth())+"-"+addZero((date.getMonth()+1))+"-"+date.getYear());
                i.putExtra("projectId",cur.getInt(0));
                db.close();
                startActivity(i);
            }
        });

        btAttendance.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Cursor cur = db.rawQuery("select ProjectID from tbl_ProjectSite where ProjectName = '"+spProject.getSelectedItem().toString()+"'",null);
                cur.moveToFirst();

                Intent i = new Intent(getApplicationContext(), DailyAttendanceActivity.class);
                i.putExtra("projectId",cur.getInt(0));
                db.close();
                startActivity(i);
            }
        });

        btWorkReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Cursor cur = db.rawQuery("select ProjectID from tbl_ProjectSite where ProjectName = '"+spProject.getSelectedItem().toString()+"'",null);
                cur.moveToFirst();

                db.close();

                Intent i = new Intent(getApplicationContext(), DailyWorkReportActivity.class);
                i.putExtra("projectId",cur.getInt(0));
                startActivity(i);
            }
        });
    }

    private String addZero(int date)
    {
        return ((date<10) ? "0" : "")+date;
    }
}