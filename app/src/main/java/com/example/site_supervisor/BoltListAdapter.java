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

public class BoltListAdapter extends ArrayAdapter
{
    Context cont;
    int resource;
    List<BoltListPojo> bolt;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;

    public BoltListAdapter(@NonNull Context cont, int resource, @NonNull List<BoltListPojo> bolt)
    {
        super(cont, resource, bolt);
        this.cont = cont;
        this.resource = resource;
        this.bolt = bolt;
    }

    public View getView(final int position, View convetView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(resource,null,false);

        ImageView imgEdit = view.findViewById(R.id.imgEdit);

        EditText et[] = new EditText[3];
        int id[] = {R.id.etPosition,R.id.etBolt,R.id.etQty};

        for(int i=0; i<id.length; i++)
        {
            et[i] = view.findViewById(id[i]);
        }

        et[0].setText(""+(position+1));
        et[1].setText(bolt.get(position).getType());
        et[2].setText(""+bolt.get(position).getQty());

        if(bolt.get(position).getEditable())
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
                if(bolt.get(position).getEditable())
                {
                    try
                    {
                        db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(cont,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    bolt.get(position).setEditable(false);
                    bolt.get(position).setType(et[1].getText().toString());
                    bolt.get(position).setQty(Integer.parseInt(et[2].getText().toString()));

                    String updateQuery = "update tbl_billofboltdetails set type = '"+bolt.get(position).getType()+"', ";
                    updateQuery += "qty = "+bolt.get(position).getQty();
                    updateQuery += " where id = "+bolt.get(position).getId();

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
                else
                {
                    for(int i=1; i<et.length; i++)
                    {
                        et[i].setEnabled(true);
                        et[i].setFocusable(true);
                        et[i].setFocusableInTouchMode(true);
                        et[i].setClickable(true);
                    }

                    bolt.get(position).setEditable(true);
                    imgEdit.setImageResource(R.drawable.done);
                }
            }
        });
        return view;
    }
}
