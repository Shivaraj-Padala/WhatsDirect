package com.intbox.whatsdirect;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.hbb20.CountryCodePicker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText mPhoneNumber = null;
    boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout drawer;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        SharedPreferences sp = getSharedPreferences("RatingPref", Context.MODE_PRIVATE);
        if(!sp.getBoolean("hasRated",false) && sp.getBoolean("firstLaunchCompleted",false)){
            showRateDialog();
        }
        firstLaunch();
        Snackbar.make(drawer,"Hi there, Happy to see you again!",Snackbar.LENGTH_SHORT).show();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void firstLaunch() {
        SharedPreferences sp = getSharedPreferences("RatingPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("firstLaunchCompleted",true);
        editor.apply();
    }

    public void showRateDialog(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog,null);
        Button ratenow = (Button)mView.findViewById(R.id.button_ratenow);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        ratenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("RatingPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("hasRated",true);
                editor.apply();
                rateUsOnPlayStore();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void whatsAppDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.whatsapp_dialog,null);
        TextView whatsapp_btn = (TextView) mView.findViewById(R.id.whatsApp);
        TextView whatsappbusiness_btn = (TextView) mView.findViewById(R.id.whatsAppBusiness);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        whatsapp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setPackage("com.whatsapp");
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber));
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        whatsappbusiness_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setPackage("com.whatsapp.w4b");
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber));
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void Alert_Dialog_Blank_Input() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Error");
        builder.setMessage("Please enter Phone Number");
        AlertDialog alert = builder.create();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (doubleBackToExitPressedOnce) {
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Snackbar.make(drawer,"Press back again to exit",Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {

        }
        if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_feedback) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            PackageManager manager = getApplicationContext().getPackageManager();
            PackageInfo info = null;
            try {
                info = manager.getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String version = info.versionName;
            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.developer_email)});
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + version);
            i.putExtra(Intent.EXTRA_TEXT,
                    "\n" + " Device : " + getDeviceName() +
                            "\n" + " System Version : " + Build.VERSION.SDK_INT +
                            "\n" + " Display Height : " + height + "px" +
                            "\n" + " Display Width  : " + width + "px" +
                            "\n\n" + "Feedback Description" +
                            "\n");
            i.setType("message/rfc822");
            startActivity(Intent.createChooser(i, "Send Email"));
            return false;
        } else if (id == R.id.nav_privacy_policy) {
            try {
                Uri uri = Uri.parse("https://sites.google.com/view/whatsdirectprivacy/home");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (Exception ex) {
            }
        } else if (id == R.id.nav_rate) {
            rateUsOnPlayStore();
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, About.class));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }

    public void shareApp() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Try Whats Chat App which lets you send messages on WhatsApp without saving phone number into your contacts list, Get it from store now: https://play.google.com/store/apps/details?id=" + getPackageName();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Status Saver");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void rateUsOnPlayStore() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    public void msgOnWhatsApp(View view) {
        EditText phoneNumberField = (EditText) findViewById(R.id.inputField);
        if (phoneNumberField.getText().toString().isEmpty()) {
            Alert_Dialog_Blank_Input();
        } else {
            CountryCodePicker cpp = (CountryCodePicker) findViewById(R.id.cpp);
            cpp.registerCarrierNumberEditText(phoneNumberField);
            phoneNumber = cpp.getFullNumber();
            boolean whatsApp = appInstalledOrNot("com.whatsapp");
            boolean whatsAppBusiness = appInstalledOrNot("com.whatsapp.w4b");
            if (whatsApp && whatsAppBusiness) {
                whatsAppDialog();
            } else if (whatsApp) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setPackage("com.whatsapp");
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber));
                startActivity(intent);
            } else if (whatsAppBusiness) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setPackage("com.whatsapp.w4b");
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber));
                startActivity(intent);
            }
        }
    }

    public void saveNum(View view) {
        mPhoneNumber = (EditText) findViewById(R.id.inputField);
        if (mPhoneNumber.getText().toString().isEmpty()) {
            Alert_Dialog_Blank_Input();
        } else {
            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            CountryCodePicker cpp = (CountryCodePicker) findViewById(R.id.cpp);
            cpp.registerCarrierNumberEditText(mPhoneNumber);
            String phoneNumber = cpp.getFullNumberWithPlus();
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber);
            startActivity(intent);
        }
    }
}

