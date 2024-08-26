package com.example.site_supervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class StockMaterialAdapter extends ArrayAdapter
{
    Context cont;
    int resource;
    List<StockMaterialPojo> stock;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;

    public StockMaterialAdapter(@NonNull Context context, int resource, @NonNull List stock) {
        super(context, resource, stock);
        this.cont = cont;
        this.resource = resource;
        this.stock = stock;
    }
}
