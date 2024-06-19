package com.example.site_supervisor;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TodaysAttendanceActivity extends Activity
{
    TextView tvDate;
    List<WorkerAttendancePojo> worker;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;
    ListView listAttendance;
    ImageView imgAdd;
    String todayDate;
    private PopupMenu pop;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_attendance);

        tvDate = findViewById(R.id.tvDate);
        listAttendance = findViewById(R.id.listAttendance);
        imgAdd = findViewById(R.id.imgAdd);

        worker = new ArrayList<>();

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

        Cursor cur = db.rawQuery("select id,srno,e_name,a_status,in_time,out_time,rate from daily_atten where ProjectId = "+getIntent().getIntExtra("projectId",0)+" and atten_date like '%"+todayDate+"%'",null);

        if(cur.getCount()!=0)
        {
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

            TodayAttendanceAdapter todayAdapter = new TodayAttendanceAdapter(getApplicationContext(),R.layout.todays_attendance_adapter,worker);
            listAttendance.setAdapter(todayAdapter);
        }
        else
        {
            Cursor cur2 = db.rawQuery("select srno, e_name from daily_atten where projectid = ? group by srno, e_name", new String[] { String.valueOf(getIntent().getIntExtra("projectId",0)) });
            Cursor cur_id = db.rawQuery("select Max(id) from daily_atten",null);
            cur_id.moveToFirst();
            int id = cur_id.getInt(0)+1;

            while (cur2.moveToNext())
            {
                WorkerAttendancePojo temp = new WorkerAttendancePojo();

                ContentValues values = new ContentValues();
                values.put("id", id);
                values.put("ProjectId", getIntent().getIntExtra("projectId",0));
                values.put("atten_date", todayDate);
                values.put("e_code", "0");
                values.put("e_name", cur2.getString(1));
                values.put("a_status", "A");
                values.put("in_time", "00:00");
                values.put("out_time", "00:00");
                values.put("srno", cur2.getInt(0));
                values.put("rate", 0);

                temp.setId(id);
                temp.setSrno(cur2.getInt(0));
                temp.setName(cur2.getString(1));
                temp.setPreset("A");
                temp.setInTime("00:00");
                temp.setOutTime("00:00");
                temp.setRate(0.0);

                long newRowId = db.insert("daily_atten", null, values);

                if (newRowId == -1)
                {
                    Toast.makeText(getApplicationContext(), "Error inserting data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Data inserted with row ID: " + newRowId, Toast.LENGTH_SHORT).show();
                }

                worker.add(temp);
                id++;
            }

            TodayAttendanceAdapter todayAdapter = new TodayAttendanceAdapter(getApplicationContext(),R.layout.todays_attendance_adapter,worker);
            listAttendance.setAdapter(todayAdapter);
        }

        db.close();

        imgAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
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

        etPresent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pop == null)
                {
                    presentPopupMenu(v,etInTime,etOutTime);
                }
                return true;
            }
        });

        try
        {
            db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        Cursor cur_srn = db.rawQuery("select Max(srno) from daily_atten where ProjectId = "+getIntent().getIntExtra("projectId",0),null);
        cur_srn.moveToFirst();
        int srno = cur_srn.getInt(0)+1;
        etSrno.setText(""+srno);
        db.close();

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkerAttendancePojo work = new WorkerAttendancePojo();

                try {
                    db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                try
                {
                    Cursor cur = db.rawQuery("select Max(id) from daily_atten",null);
                    cur.moveToFirst();
                    int id = cur.getInt(0)+1;

                    if(etName.getText().toString().equals("") || etPresent.getText().toString().equals(""))
                    {
                        throw new EmptyStringException();
                    }

                    work.setId(id);
                    work.setSrno(Integer.parseInt(etSrno.getText().toString()));
                    work.setName(etName.getText().toString());
                    work.setPreset(etPresent.getText().toString());

                    if(etInTime.getText().toString().equals(""))
                    {
                        etInTime.setText("00:00");
                    }
                    if(etOutTime.getText().toString().equals(""))
                    {
                        etOutTime.setText("00:00");
                    }

                    work.setInTime(etInTime.getText().toString());
                    work.setOutTime(etOutTime.getText().toString());
                    work.setRate(Double.parseDouble(etRate.getText().toString()));

                    worker.add(work);

                    ContentValues values = new ContentValues();
                    values.put("id", id);
                    values.put("ProjectId", getIntent().getIntExtra("projectId",0));
                    values.put("atten_date", todayDate);
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
                catch (NumberFormatException nfe)
                {
                    Toast.makeText(getApplicationContext(),"Rate should be Numeric...!",Toast.LENGTH_SHORT).show();
                }
                catch (EmptyStringException ese)
                {
                    Toast.makeText(getApplicationContext(),ese.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
    }

    private void presentPopupMenu(View v, EditText etIn, EditText etOut)
    {
        pop = new PopupMenu(getApplicationContext(),v);
        pop.getMenuInflater().inflate(R.menu.popup_present,pop.getMenu());

        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                EditText et = v.findViewById(R.id.etPresent);

                if(item.getTitle().toString().equals("Present"))
                {
                    et.setText("P");
                }
                else
                {
                    et.setText("A");
                    etIn.setText("00:00");
                    etOut.setText("00:00");
                }
                return true;
            }
        });

        pop.setOnDismissListener(new PopupMenu.OnDismissListener()
        {
            @Override
            public void onDismiss(PopupMenu menu)
            {
                pop = null;
            }
        });

        pop.show();
    }
}