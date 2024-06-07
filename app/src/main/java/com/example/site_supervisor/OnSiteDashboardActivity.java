package com.example.site_supervisor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OnSiteDashboardActivity extends Activity
{
    ImageView imgProfile;
    LinearLayout dailyReport, siteInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_site_dashboard);

        imgProfile = findViewById(R.id.imgProfile);
        dailyReport = findViewById(R.id.dailyReport);
        siteInventory = findViewById(R.id.siteInventory);

        imgProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),SiteSupervisorProfileActivity.class);
                startActivity(i);
            }
        });

        dailyReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(),DailyProjectDetailsActivity.class);
                startActivity(i);
            }
        });
    }
}