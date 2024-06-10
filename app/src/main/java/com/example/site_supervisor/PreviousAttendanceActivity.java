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

import java.util.ArrayList;
import java.util.List;

public class PreviousAttendanceActivity extends Activity
{
    TextView tvDate;
    List<WorkerAttendancePojo> worker;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;
    ListView listAttendance;
    ImageView imgAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_attendance);

        tvDate = findViewById(R.id.tvDate);
        listAttendance = findViewById(R.id.listAttendance);
        imgAdd = findViewById(R.id.imgAdd);

        worker = new ArrayList<>();
        tvDate.setText(tvDate.getText().toString()+getIntent().getStringExtra("date"));

        try
        {
            db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        Cursor cur = db.rawQuery("select id,srno,e_name,a_status,in_time,out_time,rate from daily_atten where ProjectId = "+getIntent().getIntExtra("projectId",0)+" and atten_date like '%"+getIntent().getStringExtra("date")+"%'",null);

        while (cur.moveToNext())
        {
            WorkerAttendancePojo temp = new WorkerAttendancePojo();
            temp.setId(cur.getInt(0));
            temp.setSrno(cur.getInt(1));
            temp.setName(cur.getString(2));
            temp.setPreset(cur.getString(3));
            temp.setInTime(cur.getString(4));
            temp.setOutTime(cur.getString(5));
            temp.setRate((double)cur.getInt(6));
            worker.add(temp);
        }

        WorkerAttendanceAdapter workerAdapter = new WorkerAttendanceAdapter(getApplicationContext(),R.layout.worker_attendance,worker);
        listAttendance.setAdapter(workerAdapter);

        imgAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPopupMenu(v);
            }
        });

        db.close();
    }

    private void showPopupMenu(View view)
    {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_add_worker, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        EditText etSrno = popupView.findViewById(R.id.etSrno);
        EditText etName = popupView.findViewById(R.id.etName);
        EditText etPresent = popupView.findViewById(R.id.etPresent);
        EditText etInTime = popupView.findViewById(R.id.etInTime);
        EditText etOutTime = popupView.findViewById(R.id.etOutTime);
        EditText etRate = popupView.findViewById(R.id.etRate);
        Button btAdd = popupView.findViewById(R.id.btAdd);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                WorkerAttendancePojo work = new WorkerAttendancePojo();

                try
                {
                    db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

                Cursor cur = db.rawQuery("select Max(id) from daily_atten",null);
                cur.moveToFirst();
                int id = cur.getInt(0)+1;

                work.setId(id);
                work.setSrno(Integer.parseInt(etSrno.getText().toString()));
                work.setName(etName.getText().toString());
                work.setPreset(etPresent.getText().toString());
                work.setInTime(etInTime.getText().toString());
                work.setOutTime(etOutTime.getText().toString());
                work.setRate(Double.parseDouble(etRate.getText().toString()));

                worker.add(work);

                ContentValues values = new ContentValues();
                values.put("id", id);
                values.put("ProjectId", getIntent().getIntExtra("projectId",0));
                values.put("atten_date", getIntent().getStringExtra("date"));
                values.put("e_code", 0);
                values.put("e_name", work.getName());
                values.put("a_status", work.getPreset());
                values.put("in_time", work.getInTime());
                values.put("out_time", work.getOutTime());
                values.put("srno", work.getSrno());
                values.put("rate", work.getRate());

                long newRowId = db.insert("daily_atten", null, values);

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