package com.example.site_supervisor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class SiteSupervisorProfileActivity extends Activity
{
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    SQLiteDatabase db = null;
    SharedPreferences share;
    TextView tvCompany;
    String path;
    int tvId[] = {R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4,
            R.id.tv5,R.id.tv6,R.id.tv7,R.id.tv8,
            R.id.tv9,R.id.tv10,R.id.tv11,R.id.tv12,
            R.id.tv13,R.id.tv14,R.id.tv15,R.id.tv16};

    int etId[] = {R.id.et1,R.id.et2,R.id.et3,R.id.et4,
            R.id.et5,R.id.et6,R.id.et7,R.id.et8,
            R.id.et9,R.id.et10,R.id.et11,R.id.et12,
            R.id.et13,R.id.et14,R.id.et15,R.id.et16};


    TextView tv[];
    EditText et[];
    Button btUpdate, btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_supervisor_profile);

        path = dbPath+dbName;
        tv = new TextView[tvId.length];
        et = new EditText[etId.length];

        btBack = findViewById(R.id.btBack);
        btUpdate = findViewById(R.id.btUpdate);
        tvCompany = findViewById(R.id.tvCompany);

        for(int i=0; i<tvId.length; i++)
        {
            tv[i] = findViewById(tvId[i]);
            et[i] = findViewById(etId[i]);
        }

        share = getSharedPreferences("siteSupervisor",MODE_PRIVATE);
        tvCompany.setText(share.getString("company","Null"));

        try
        {
            db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Database does'nt exists",Toast.LENGTH_SHORT).show();
        }

        Cursor cur = db.rawQuery("select * from crm_user_registration where usertype = 'Site Supervisor' and officeext = "+share.getString("phone","0"),null);

        cur.moveToFirst();

        if(cur!=null)
        {
            for(int i=0; i<tvId.length; i++)
            {
                tv[i].setText(cur.getColumnName(i+1));
                et[i].setText(cur.getString(i+1));
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Empty cursor",Toast.LENGTH_LONG).show();
        }
        db.close();



        btBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Database does'nt exists",Toast.LENGTH_SHORT).show();
                }

                SharedPreferences.Editor edit = share.edit();

                edit.putString("company",et[6].getText().toString());
                tvCompany.setText(et[6].getText().toString());
                edit.apply();

                String updateQuery = "update crm_user_registration set ";

                for(int i=0; i<tvId.length; i++)
                {
                    if(!et[i].getText().toString().equals(""))
                    {
                        updateQuery+=tv[i].getText().toString()+" = '";
                        updateQuery+=et[i].getText().toString()+"' , ";
                    }
                }
                updateQuery = updateQuery.substring(0,updateQuery.length()-2);
                updateQuery+="where usertype = 'Site Supervisor' and officeext = '"+share.getString("phone","0")+"'";

                try
                {
                    db.execSQL(updateQuery);
                    db.close();
                    Toast.makeText(getApplicationContext(),"Record Updated",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


        et[5].setFocusable(false);
        et[5].setFocusableInTouchMode(false);
        et[5].setCursorVisible(false);
        et[5].setLongClickable(false);

        et[5].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                departmentPopupMenu(v);
            }
        });

        et[6].setFocusable(false);
        et[6].setFocusableInTouchMode(false);
        et[6].setCursorVisible(false);
        et[6].setLongClickable(false);

        et[6].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                companyPopupMenu(v);
            }
        });

        et[15].setFocusable(false);
        et[15].setFocusableInTouchMode(false);
        et[15].setCursorVisible(false);
        et[15].setLongClickable(false);

        et[15].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                statusPopupMenu(v);
            }
        });

    }

    private void departmentPopupMenu(View v)
    {
        PopupMenu pop = new PopupMenu(getApplicationContext(),v);
        pop.getMenuInflater().inflate(R.menu.popup_department,pop.getMenu());

        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                et[5].setText(item.getTitle().toString());
                return true;
            }
        });
        pop.show();
    }

    private void companyPopupMenu(View v)
    {
        PopupMenu pop = new PopupMenu(getApplicationContext(),v);
        pop.getMenuInflater().inflate(R.menu.popup_company,pop.getMenu());

        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                et[6].setText(item.getTitle().toString());
                return true;
            }
        });
        pop.show();
    }

    private void statusPopupMenu(View v)
    {
        PopupMenu pop = new PopupMenu(getApplicationContext(),v);
        pop.getMenuInflater().inflate(R.menu.popup_status,pop.getMenu());

        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                et[15].setText(item.getTitle().toString());
                return true;
            }
        });
        pop.show();
    }
}