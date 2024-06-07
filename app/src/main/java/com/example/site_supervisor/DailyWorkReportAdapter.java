package com.example.site_supervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

public class DailyWorkReportAdapter extends ArrayAdapter
{
    Context cont;
    int resource;
    List<DailyWorkReportPojo> report;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Balaji_Site_Supervisor.db";
    String path = dbPath+dbName;

    public DailyWorkReportAdapter(Context cont, int resource, List report)
    {
        super(cont, resource, report);
        this.cont = cont;
        this.report = report;
        this.resource = resource;
    }

    public View getView(final int position, View convetView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(resource,null,false);

        EditText et[] = new EditText [6];
        int id [] = {R.id.etSrno,R.id.etItemDetails,R.id.etUnit,R.id.etProjectQty,R.id.etQtyWork,R.id.etRemark};

        for (int i=0; i<id.length; i++)
        {
            et[i] = view.findViewById(id[i]);
        }

        DailyWorkReportPojo work = report.get(position);
        Button btUpdate = view.findViewById(R.id.btUpdate);

        et[0].setText(""+work.getSrno());
        et[1].setText(work.getItemDetails());
        et[2].setText(work.getUnit());
        et[3].setText(work.getQty());
        et[4].setText(work.getWorkQty());
        et[5].setText(work.getRemark());

        for(int i=0; i<et.length; i++)
        {
            et[i].setEnabled(work.getEditable());
            et[i].setFocusable(work.getEditable());
            et[i].setFocusableInTouchMode(work.getEditable());
            et[i].setClickable(work.getEditable());
        }

        if(work.getEditable())
        {
            btUpdate.setText("Done");
        }

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btUpdate.getText().toString().equals("Update"))
                {
                    for(int i=0; i<et.length; i++)
                    {
                        et[i].setEnabled(true);
                        et[i].setFocusable(true);
                        et[i].setFocusableInTouchMode(true);
                        et[i].setClickable(true);
                    }

                    report.get(position).setEditable(true);
                    btUpdate.setText("Done");
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
                    btUpdate.setText("Update");

                    report.get(position).setEditable(false);
                    report.get(position).setSrno(Integer.parseInt(et[0].getText().toString()));
                    report.get(position).setItemDetails(et[1].getText().toString());
                    report.get(position).setUnit(et[2].getText().toString());
                    report.get(position).setQty(et[3].getText().toString());
                    report.get(position).setWorkQty(et[4].getText().toString());
                    report.get(position).setRemark(et[5].getText().toString());

                    String updateQuery = "update tbl_ProjectSite_Details_dailyReport set srno = "+report.get(position).getSrno()+", ";
                    updateQuery+="ItemDetails = '"+report.get(position).getItemDetails()+"', ";
                    updateQuery+="units = '"+report.get(position).getUnit()+"', ";
                    updateQuery+="qty = '"+report.get(position).getQty()+"', ";
                    updateQuery+="workqty = '"+report.get(position).getWorkQty()+"', ";
                    updateQuery+="Remarks = '"+report.get(position).getRemark()+"' ";
                    updateQuery+="where id = "+report.get(position).getId();

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
                }
            }
        });

        return view;
    }
}
