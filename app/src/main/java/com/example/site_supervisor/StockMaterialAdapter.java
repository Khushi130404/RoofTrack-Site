package com.example.site_supervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

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

    public View getView(final int position, View convetView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(resource,null,false);
        int id[] = {R.id.etPosition,R.id.etCode,R.id.etItemName,R.id.etDCQty,R.id.etUsedQty,R.id.etStockQty};
        EditText et[] = new EditText[6];

        for(int i=0; i<id.length; i++)
        {
            et[i] = view.findViewById(id[i]);
        }

        et[0].setText(""+position+1);
        et[1].setText(stock.get(position).getCode());
        et[2].setText(stock.get(position).getItemname());
        et[3].setText(""+stock.get(position).getDcQty());
        et[4].setText(""+stock.get(position).getUsedQty());
        et[5].setText(""+stock.get(position).getStockQty());

        return view;
    }
}
