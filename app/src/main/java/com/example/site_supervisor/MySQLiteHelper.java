package com.example.site_supervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MySQLiteHelper extends SQLiteOpenHelper
{
    Context cont;

    public static String dbName = "Balaji_Site_Supervisor.db";
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    SQLiteDatabase db= null;
    public MySQLiteHelper(Context cont)
    {
        super(cont,dbName,null,1);
        this.cont=cont;
    }

    public void checkDatabase()
    {
        String myPath = dbPath+dbName;
        try
        {
            db = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);
        }
        catch (Exception e)
        {
            Toast.makeText(cont.getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        if(db==null)
        {
            this.getReadableDatabase();
            this.close();


            try
            {
                InputStream is = cont.getAssets().open(dbName);
                OutputStream fos = new FileOutputStream(myPath);
                byte b[] = new byte[1024];
                int length = 0;

                while((length = is.read(b))>0)
                {
                    fos.write(b);
                }

                is.close();
                fos.close();

                Toast.makeText(cont,"DATABASE CREATED SUCCEFULLY",Toast.LENGTH_LONG).show();
            }
            catch(Exception e)
            {
                Toast.makeText(cont.getApplicationContext(), "CREATION ERROR : "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(cont.getApplicationContext(),"Database Already Exists",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public void deleteDatabase()
    {
        boolean result = cont.deleteDatabase(dbName);
        if (result)
        {
            Toast.makeText(cont.getApplicationContext(), "Database Deleted Successfully", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(cont.getApplicationContext(), "Database Deletion Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
