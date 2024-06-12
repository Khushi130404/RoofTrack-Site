package com.example.site_supervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
        return convetView;
    }
}
