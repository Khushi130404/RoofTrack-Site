package com.example.site_supervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

public class TodayAttendanceAdapter extends ArrayAdapter
{
    Context cont;
    int resource;
    List<WorkerAttendancePojo> worker;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Balaji_Site_Supervisor.db";
    String path = dbPath+dbName;

    public TodayAttendanceAdapter(Context cont, int resource, List worker)
    {
        super(cont, resource, worker);
        this.cont = cont;
        this.worker = worker;
        this.resource = resource;
    }

    public View getView(final int position, View convetView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(resource,null,false);

        WorkerAttendancePojo work = worker.get(position);

        EditText et[] = new EditText [5];
        CheckBox cbPresent = view.findViewById(R.id.cbPresent);

        int id [] = {R.id.etSrno,R.id.etName,R.id.etInTime,R.id.etOutTime,R.id.etRate};

        for (int i=0; i<id.length; i++)
        {
            et[i] = view.findViewById(id[i]);
        }

        Button btSet = view.findViewById(R.id.btSet);

        et[0].setText(""+work.getSrno());
        et[1].setText(work.getName());
        if(work.getPreset().equalsIgnoreCase("P"))
        {
            cbPresent.setChecked(true);
        }
        else
        {
            cbPresent.setChecked(false);
        }

        et[2].setText(work.getInTime());
        et[3].setText(work.getOutTime());
        et[4].setText(""+work.getRate());


        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
                }
                catch (Exception e)
                {
                    Toast.makeText(cont,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

                if(cbPresent.isChecked())
                {
                    worker.get(position).setPreset("P");
                }
                else
                {
                    worker.get(position).setPreset("A");
                }

                worker.get(position).setInTime(et[2].getText().toString());
                worker.get(position).setOutTime(et[3].getText().toString());
                worker.get(position).setRate(Double.parseDouble(et[4].getText().toString()));

                String updateQuery = "update daily_atten set ";
                updateQuery+="a_status = '"+worker.get(position).getPreset()+"', ";
                updateQuery+="in_time = '"+worker.get(position).getInTime()+"', ";
                updateQuery+="out_time = '"+worker.get(position).getOutTime()+"', ";
                updateQuery+="rate = "+worker.get(position).getRate()+" ";
                updateQuery+="where id = "+work.getId();

                try
                {
                    db.execSQL(updateQuery);
                    db.close();
                    Toast.makeText(cont,"Record Updated",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(cont,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        cbPresent.setOnClickListener(new View.OnClickListener()
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
                    Toast.makeText(cont,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

                String updateQuery="";

                if(cbPresent.isChecked())
                {
                    worker.get(position).setPreset("P");
                    updateQuery = "update daily_atten set a_status = 'P'";
                }
                else
                {
                    worker.get(position).setPreset("A");
                    updateQuery = "update daily_atten set a_status = 'A'";
                }

                updateQuery+="where id = "+work.getId();

                try
                {
                    db.execSQL(updateQuery);
                    db.close();
                    Toast.makeText(cont,"Record Updated",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(cont,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
