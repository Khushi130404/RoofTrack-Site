package com.example.site_supervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        TextView tvPosition = view.findViewById(R.id.tvPosition);
        TextView tvMark = view.findViewById(R.id.tvMark);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvWeight = view.findViewById(R.id.tvWeight);
        ImageView imgEdit = view.findViewById(R.id.imgEdit);

        tvPosition.setText(""+(position+1));
        tvMark.setText(material.get(position).getAssemblyMark());
        tvName.setText(material.get(position).getName());
        tvWeight.setText(material.get(position).getWeight().toString());

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

        return view;
    }
}
