package com.zuture.apps.carloancalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class SavedLoansActivity extends AppCompatActivity {


    SQLiteOpenHelper sqLiteOpenHelper= new DatabaseHelper(this);
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_loans);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.savedloandoption);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {



                switch(item.getItemId()){

                    case R.id.homeaption:
                        Intent intenttwo= new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intenttwo);
                        finish();
                        break;

                    case R.id.calcaption:
                        Intent intent= new Intent(getApplicationContext(), CalculatorActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.savedloandoption:
                        Intent intent1=getIntent();
                        startActivity(intent1);


                        break;
                    case R.id.reminderoption:
                        Intent intentthree= new Intent(getApplicationContext(),ReminderActivity.class);
                        startActivity(intentthree);
                        finish();
                        break;


                }
                return false;
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });


        AdLoader adLoader = new AdLoader.Builder(this, getResources().getString(R.string.native_ad_unit))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {

                    ColorDrawable background= new ColorDrawable(R.style.AppTheme);
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        LinearLayout linearLayout= findViewById(R.id.ad_container);
                        linearLayout.setVisibility(View.VISIBLE);
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(background).build();

                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(unifiedNativeAd);

                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());



        populateRecyler();
    }

    public void populateRecyler(){

        RecyclerView savedLoansRecycler= findViewById(R.id.svaedLoansRecycler);




        try {
            sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
            cursor = ((DatabaseHelper) sqLiteOpenHelper).readAllLoanDetails();

            ArrayList<String> loanNameArray = new ArrayList<>();
            ArrayList<String> principalArray = new ArrayList<>();
            ArrayList<String> monthlyPaymentArray = new ArrayList<>();
            ArrayList<String> annualInterestArray = new ArrayList<>();
            ArrayList<String> interestRateArray = new ArrayList<>();
            ArrayList<String> monthsArray = new ArrayList<>();

            if (cursor.getCount() == 0) {
                try{
                    SQLiteOpenHelper sqLiteOpenHelper=  new DatabaseHelper(this);
                    Cursor cursor = ((DatabaseHelper) sqLiteOpenHelper).readAllLoanDetails();
                    if(cursor.getCount()==0) {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SavedLoansActivity.this);
                        builder.setTitle("No Saved loans");
                        builder.setMessage(getResources().getString(R.string.no_loans));
                        builder.setPositiveButton("Save Loan", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent= new Intent(SavedLoansActivity.this,CalculatorActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent= new Intent(SavedLoansActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.show();
                    }}catch (SQLException e){
                    Toast.makeText(this,"Error showing dialog",Toast.LENGTH_LONG).show();
                }
            } else {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(1);
                    String principalStr = cursor.getString(2);
                    String monthlyPaymentStr = cursor.getString(3);
                    String interestRateStr = cursor.getString(4);
                    String annualInterestStr = cursor.getString(5);
                    String monthsStr = cursor.getString(6);

                    loanNameArray.add(name);
                    principalArray.add(principalStr);
                    monthlyPaymentArray.add(monthlyPaymentStr);
                    interestRateArray.add(interestRateStr);
                    annualInterestArray.add(annualInterestStr);
                    monthsArray.add(monthsStr);

                }

            }


            SavedLoansRecyclerAdapter savedLoansRecyclerAdapter = new SavedLoansRecyclerAdapter(loanNameArray, principalArray, monthlyPaymentArray, interestRateArray, annualInterestArray, monthsArray);
            savedLoansRecycler.setAdapter(savedLoansRecyclerAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            savedLoansRecycler.setLayoutManager(linearLayoutManager);

        }catch (SQLException e){
            Toast.makeText(this,"Database Error", Toast.LENGTH_LONG).show();


    }
}


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cursor!=null){
            cursor.close();
            sqLiteDatabase.close();
        }
    }
    @Override
    protected void onRestart() {
        populateRecyler();
        super.onRestart();
    }

}