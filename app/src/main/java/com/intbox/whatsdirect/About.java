package com.intbox.whatsdirect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        PackageInfo info = null;
        try{
            info = getPackageManager().getPackageInfo(getPackageName(),0);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        String vName = info.versionName;
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("About");
        TextView textView = findViewById(R.id.app_version);
        textView.setText("Version " +vName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
