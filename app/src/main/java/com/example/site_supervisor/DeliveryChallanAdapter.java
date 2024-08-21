package com.example.site_supervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

public class DeliveryChallanAdapter extends ArrayAdapter
{
    Context cont;
    int resource;
    List<DeliveryChallanPojo> challan;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;

    public DeliveryChallanAdapter(@NonNull Context cont, int resource, @NonNull List<DeliveryChallanPojo> challan)
    {
        super(cont, resource, challan);
        this.cont = cont;
        this.resource = resource;
        this.challan = challan;
    }

    public View getView(final int position, View convetView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(resource,null,false);

        ImageView imgEdit = view.findViewById(R.id.imgEdit);

        EditText et[] = new EditText[4];
        int id[] = {R.id.etPosition,R.id.etCode,R.id.etItemName,R.id.etUnit,R.id.etQty};

        for(int i=0; i<id.length; i++)
        {
            et[i] = view.findViewById(id[i]);
        }

        et[0].setText(""+(position+1));
        et[1].setText(challan.get(position).getCode());
        et[2].setText(challan.get(position).getItemName());
        et[3].setText(challan.get(position).getUnit());
        et[4].setText(""+challan.get(position).getQty());

        if(challan.get(position).getEditable())
        {
            for(int i=1; i<et.length; i++)
            {
                et[i].setEnabled(true);
                et[i].setFocusable(true);
                et[i].setFocusableInTouchMode(true);
                et[i].setClickable(true);
            }
            imgEdit.setImageResource(R.drawable.done);
        }
        else
        {
            for(int i=1; i<et.length; i++)
            {
                et[i].setEnabled(false);
                et[i].setFocusable(false);
                et[i].setFocusableInTouchMode(false);
                et[i].setClickable(false);
            }

            imgEdit.setImageResource(R.drawable.edit);
        }

        imgEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(challan.get(position).getEditable())
                {
                    try
                    {
                        db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(cont,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    try
                    {
                        challan.get(position).setEditable(false);
                        if(et[1].getText().toString().equals(""))
                        {
                            throw new EmptyStringException();
                        }
                        challan.get(position).setCode(et[1].getText().toString().toUpperCase());
                        challan.get(position).setItemName(et[2].getText().toString().toUpperCase());

                        challan.get(position).setQty(Float.parseFloat(et[4].getText().toString()));

                        String updateQuery = "update tbl_dc_details set itemcode = '"+challan.get(position).getCode()+"', ";
                        updateQuery += "itemname = "+challan.get(position).getItemName();
                        updateQuery += "uom = "+challan.get(position).getUnit();
                        updateQuery += "qty = "+challan.get(position).getQty();
                        updateQuery += " where id = "+challan.get(position).getId();

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

                        for(int i=1; i<et.length; i++)
                        {
                            et[i].setEnabled(false);
                            et[i].setFocusable(false);
                            et[i].setFocusableInTouchMode(false);
                            et[i].setClickable(false);
                        }
                        imgEdit.setImageResource(R.drawable.edit);
                    }
                    catch (NumberFormatException nfe)
                    {
                        Toast.makeText(cont,"Qty should be Numeric...!",Toast.LENGTH_SHORT).show();
                    }
                    catch (EmptyStringException ese)
                    {
                        Toast.makeText(cont,ese.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    for(int i=1; i<et.length; i++)
                    {
                        et[i].setEnabled(true);
                        et[i].setFocusable(true);
                        et[i].setFocusableInTouchMode(true);
                        et[i].setClickable(true);
                    }

                    challan.get(position).setEditable(true);
                    imgEdit.setImageResource(R.drawable.done);
                }
            }
        });
        return view;
    }
}
