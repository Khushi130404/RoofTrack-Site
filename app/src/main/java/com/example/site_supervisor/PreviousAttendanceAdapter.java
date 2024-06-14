package com.example.site_supervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.List;

public class PreviousAttendanceAdapter extends ArrayAdapter
{

    Context cont;
    int resource;
    List<WorkerAttendancePojo> worker;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;
    private PopupMenu pop;

    public PreviousAttendanceAdapter(Context cont, int resource, List worker)
    {
        super(cont, resource, worker);
        this.cont = cont;
        this.worker = worker;
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View convetView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(resource,null,false);

        WorkerAttendancePojo work = worker.get(position);

        EditText et[] = new EditText [6];
        int id [] = {R.id.etSrno,R.id.etName,R.id.etPresent,R.id.etInTime,R.id.etOutTime,R.id.etRate};

        for (int i=0; i<id.length; i++)
        {
            et[i] = view.findViewById(id[i]);
        }

        //Button btUpdate = view.findViewById(R.id.btUpdate);

        ImageView imgEdit = view.findViewById(R.id.imgEdit);

        et[0].setText(""+work.getSrno());
        et[1].setText(work.getName());
        et[2].setText(work.getPreset());
        et[3].setText(work.getInTime());
        et[4].setText(work.getOutTime());
        et[5].setText(""+work.getRate());

        for(int i=1; i<et.length; i++)
        {
            et[i].setEnabled(work.getEditable());
            et[i].setFocusable(work.getEditable());
            et[i].setFocusableInTouchMode(work.getEditable());
            et[i].setClickable(work.getEditable());
        }

        if(work.getEditable())
        {
            imgEdit.setImageResource(R.drawable.done);
        }

        et[2].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pop == null)
                {
                    presentPopupMenu(v);
                }
                return true;
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(!work.getEditable())
                {
                    for(int i=1; i<et.length; i++)
                    {
                        et[i].setEnabled(true);
                        et[i].setFocusable(true);
                        et[i].setFocusableInTouchMode(true);
                        et[i].setClickable(true);
                    }

                    worker.get(position).setEditable(true);
                    imgEdit.setImageResource(R.drawable.done);
                }
                else
                {
                    try
                    {
                        db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(cont,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    imgEdit.setImageResource(R.drawable.edit);

                    worker.get(position).setEditable(false);
                    worker.get(position).setSrno(Integer.parseInt(et[0].getText().toString()));
                    worker.get(position).setName(et[1].getText().toString());
                    worker.get(position).setPreset(et[2].getText().toString());
                    worker.get(position).setInTime(et[3].getText().toString());
                    worker.get(position).setOutTime(et[4].getText().toString());
                    worker.get(position).setRate(Double.parseDouble(et[5].getText().toString()));

                    String updateQuery = "update daily_atten set srno = "+worker.get(position).getSrno()+", ";
                    updateQuery+="e_name = '"+worker.get(position).getName()+"', ";
                    updateQuery+="a_status = '"+worker.get(position).getPreset()+"', ";
                    updateQuery+="in_time = '"+worker.get(position).getInTime()+"', ";
                    updateQuery+="out_time = '"+worker.get(position).getOutTime()+"', ";
                    updateQuery+="rate = "+worker.get(position).getRate()+" ";
                    updateQuery+="where id = "+work.getId();

                    //Toast.makeText(cont,updateQuery,Toast.LENGTH_LONG).show();

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

                    for(int i=0; i<et.length; i++)
                    {
                        et[i].setEnabled(false);
                        et[i].setFocusable(false);
                        et[i].setFocusableInTouchMode(false);
                        et[i].setClickable(false);
                    }
                    et[2].setClickable(true);
                }
            }
        });

        return view;
    }

    private void presentPopupMenu(View v)
    {
        pop = new PopupMenu(cont,v);
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
                }
                return true;
            }
        });
        pop.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu)
            {
                pop = null;
            }
        });
        pop.show();
    }
}
