package com.example.site_supervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

public class MaterialConsumptionAdapter extends ArrayAdapter
{
    Context cont;
    int resource;
    List<MaterialConsumptionPojo> material;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;

    public MaterialConsumptionAdapter(Context cont, int resource,List material)
    {
        super(cont, resource, material);
        this.cont = cont;
        this.resource = resource;
        this.material = material;
    }

    public View getView(final int position, View convetView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(resource,null,false);

        ImageView imgEdit = view.findViewById(R.id.imgEdit);

        EditText et[] = new EditText[4];
        int id[] = {R.id.etPosition,R.id.etMark,R.id.etName,R.id.etQty};

        for(int i=0; i<id.length; i++)
        {
            et[i] = view.findViewById(id[i]);
        }

        et[0].setText(""+(position+1));
        et[1].setText(material.get(position).getAssemblyMark());
        et[2].setText(material.get(position).getName());
        et[3].setText(""+material.get(position).getQty());

        if(material.get(position).getEditable())
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

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(material.get(position).getEditable())
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
                        if(et[1].getText().toString().equals("") || et[2].getText().toString().equals(""))
                        {
                            throw new EmptyStringException();
                        }
                        material.get(position).setEditable(false);
                        material.get(position).setAssemblyMark(et[1].getText().toString().toUpperCase());
                        material.get(position).setName(et[2].getText().toString().toString());
                        material.get(position).setQty(Integer.parseInt(et[3].getText().toString()));

                        String updateQuery = "update tbl_billofmaterialdetails set assembly_mark = '"+material.get(position).getAssemblyMark()+"', ";
                        updateQuery += "name = '"+material.get(position).getName()+"',";
                        updateQuery += "qty = "+material.get(position).getQty();
                        updateQuery += " where id = "+material.get(position).getId();

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
                        Toast.makeText(cont,"Qty should be Integer...!",Toast.LENGTH_SHORT).show();
                    }
                    catch (EmptyStringException ese)
                    {
                        Toast.makeText(cont,ese.toString(),Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(cont,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
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

                    material.get(position).setEditable(true);
                    imgEdit.setImageResource(R.drawable.done);
                }
            }
        });
        return view;
    }
}
