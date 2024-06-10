package com.example.site_supervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class TodayAttendanceAdapter extends ArrayAdapter
{
    Context cont;
    int resource;
    List<WorkerAttendancePojo> worker;
    SQLiteDatabase db = null;
    public String dbPath = "/data/data/com.example.site_supervisor/databases/";
    public static String dbName= "Site_Supervisor.db";
    String path = dbPath+dbName;

    public TodayAttendanceAdapter(Context cont, int resource, List worker)
    {
        super(cont, resource, worker);
        this.cont = cont;
        this.worker = worker;
        this.resource = resource;
    }

    public View getView(final int position, View convetView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(resource,null,false);

        EditText et[] = new EditText [5];
        CheckBox cbPresent = view.findViewById(R.id.cbPresent);

        int id [] = {R.id.etSrno,R.id.etName,R.id.etInTime,R.id.etOutTime,R.id.etRate};

        for (int i=0; i<id.length; i++)
        {
            et[i] = view.findViewById(id[i]);
        }

        Button btSet = view.findViewById(R.id.btSet);
        Button btDelete = view.findViewById(R.id.btDelete);

        et[0].setText(""+worker.get(position).getSrno());
        et[1].setText(worker.get(position).getName());
        if(worker.get(position).getPreset().equalsIgnoreCase("P"))
        {
            cbPresent.setChecked(true);
        }
        else
        {
            cbPresent.setChecked(false);
        }

        et[2].setText(worker.get(position).getInTime());
        et[3].setText(worker.get(position).getOutTime());
        et[4].setText(""+worker.get(position).getRate());

        btDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog(position);
            }
        });

        btSet.setOnClickListener(new View.OnClickListener()
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
                    Toast.makeText(cont,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

                if(cbPresent.isChecked())
                {
                    worker.get(position).setPreset("P");
                }
                else
                {
                    worker.get(position).setPreset("A");
                }

                worker.get(position).setInTime(et[2].getText().toString());
                worker.get(position).setOutTime(et[3].getText().toString());
                worker.get(position).setRate(Double.parseDouble(et[4].getText().toString()));

                String updateQuery = "update daily_atten set ";
                updateQuery+="a_status = '"+worker.get(position).getPreset()+"', ";
                updateQuery+="in_time = '"+worker.get(position).getInTime()+"', ";
                updateQuery+="out_time = '"+worker.get(position).getOutTime()+"', ";
                updateQuery+="rate = "+worker.get(position).getRate()+" ";
                updateQuery+="where id = "+worker.get(position).getId();

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
            }
        });

        cbPresent.setOnClickListener(new View.OnClickListener()
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
                    Toast.makeText(cont,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

                String updateQuery="";

                if(cbPresent.isChecked())
                {
                    worker.get(position).setPreset("P");
                    updateQuery = "update daily_atten set a_status = 'P'";
                }
                else
                {
                    worker.get(position).setPreset("A");
                    updateQuery = "update daily_atten set a_status = 'A'";
                }

                updateQuery+="where id = "+worker.get(position).getId();

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

                notifyDataSetChanged();
            }
        });
        return view;
    }

//    void showDialog(int position)
//    {
//        AlertDialog.Builder al = new AlertDialog.Builder(getContext());
//        al.setTitle("Do you want to delete ???");
//
//        al.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                Toast.makeText(getContext(),"No action",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                new AsyncTask<Void,Void,String>()
//                {
//                    @Override
//                    protected String doInBackground(Void... voids)
//                    {
//                        try
//                        {
//                            db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
//                        }
//                        catch (Exception e)
//                        {
//                            Toast.makeText(cont,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
//                        }
//
//                        String deleteQuery = "delete from daily_atten where id = "+worker.get(position).getId();
//
//                        try
//                        {
//                            db.execSQL(deleteQuery);
//                            db.close();
//                            Toast.makeText(cont,"Record Deleted",Toast.LENGTH_SHORT).show();
//                        }
//                        catch (Exception e)
//                        {
//                            Toast.makeText(cont,e.getMessage(),Toast.LENGTH_SHORT).show();
//                        }
//                        worker.remove(position);
//                        notifyDataSetChanged();
//
//                        return null;
//                    }
//                };
//
//                AlertDialog alert = al.create();
//                alert.show();
//            }
//        });
//    }

    private void showDialog(final int position) {
        // Check if the context is an Activity instance
        if (cont instanceof Activity) {
            // Run on UI thread to ensure dialog is shown correctly
            ((Activity) cont).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Create and show the alert dialog
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cont);
                        alertDialogBuilder.setTitle("Do you want to delete ???");

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(cont, "No action", Toast.LENGTH_SHORT).show();
                            }
                        });

                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Execute the delete operation in a background thread
                                new AsyncTask<Void, Void, String>() {
                                    @Override
                                    protected String doInBackground(Void... voids) {
                                        try {
                                            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                                            String deleteQuery = "DELETE FROM daily_atten WHERE id = " + worker.get(position).getId();
                                            db.execSQL(deleteQuery);
                                            db.close();
                                            return "Record Deleted";
                                        } catch (Exception e) {
                                            return "Error: " + e.getMessage();
                                        }
                                    }

                                    @Override
                                    protected void onPostExecute(String result) {
                                        Toast.makeText(cont, result, Toast.LENGTH_SHORT).show();
                                        if (result.equals("Record Deleted")) {
                                            worker.remove(position);  // Remove the item from the list
                                            notifyDataSetChanged();   // Notify the adapter to refresh the UI
                                        }
                                    }
                                }.execute();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } catch (Exception e) {
                        Log.e("TodayAttendanceAdapter", "Error showing dialog", e);
                        Toast.makeText(cont, "Error showing dialog: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Log.e("TodayAttendanceAdapter", "Context is not an instance of Activity");
//            throw new IllegalStateException("Context is not an instance of Activity");
        }
    }
}
