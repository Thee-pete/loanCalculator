 package com.zuture.apps.carloancalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class CalculatorActivity extends AppCompatActivity {

    double amount;
    double principal;
    double deposit;
    double interestPerYear;
    double interestPerMonth;
    int numberOfYears;
    int numberOfMonths;
    double monthlyPayment;
    double interest;
    double annual;
    int months;
    private InterstitialAd mInterstitialAd;


    SQLiteOpenHelper sqLiteOpenHelper= new DatabaseHelper(this);
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





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

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.inter_ad_unit));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.calcaption);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.homeaption:

                        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.calcaption:
                        Intent intentfour= getIntent();
                        startActivity(intentfour);
                        break;
                    case R.id.savedloandoption:
                        Intent intenttwo= new Intent(getApplicationContext(), SavedLoansActivity.class);
                        startActivity(intenttwo);
                        finish();
                        break;
                    case R.id.reminderoption:
                        Intent intentthree= new Intent(getApplicationContext(),ReminderActivity.class);
                        startActivity(intentthree);
                        finish();
                        break;


                }
                return true;

            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {


            }
        });




    }
    public void onCalculateBtnClick(View view){

        calculateMonthlyPayment();

        RelativeLayout layout = findViewById(R.id.container_results);
        layout.setVisibility(View.VISIBLE);


        TextView resultMonthlyPayment= findViewById(R.id.resultMonthlyPayment);
        resultMonthlyPayment.setText(String.format("% .2f",monthlyPayment));

        TextView resultPrincipal= findViewById(R.id.resultPrincipalPayment);
        resultPrincipal.setText(String.format("% .2f",principal));

        TextView resultInterest= findViewById(R.id.resultInterest);
        resultInterest.setText(String.format("% .6f",interest));


        TextView resultMonths= findViewById(R.id.resultMonths);
        resultMonths.setText(String.valueOf( months));

        TextView resultAnnual= findViewById(R.id.resultAnnual);
        resultAnnual.setText(String.format("% .6f",annual));

    }

    public void computeData(){

        EditText amountTxt = findViewById(R.id.amount);
        EditText depositTxt= findViewById(R.id.deposit);
        EditText interestMonthlyTxt= findViewById(R.id.monthlyinterest);
        EditText interestYearlyTxt= findViewById(R.id.yearlyintereset);
        EditText numberOfYearsTxt= findViewById(R.id.numberofyears);
        EditText numberOfMonthsTxt= findViewById(R.id.numberofmonths);




        if(amountTxt.getText().toString().isEmpty())
            amountTxt.setText(String.valueOf(0));
        if(depositTxt.getText().toString().isEmpty())
            depositTxt.setText(String.valueOf(0));
        if(interestYearlyTxt.getText().toString().isEmpty())
            interestYearlyTxt.setText(String.valueOf(0));
        if(interestMonthlyTxt.getText().toString().isEmpty())
            interestMonthlyTxt.setText(String.valueOf(0));
        if( numberOfYearsTxt.getText().toString().isEmpty())
            numberOfYearsTxt.setText(String.valueOf(0));
        if(numberOfMonthsTxt.getText().toString().isEmpty())
            numberOfMonthsTxt.setText(String.valueOf(0));

        amount= Double.parseDouble(amountTxt.getText().toString());
        deposit= Double.parseDouble(depositTxt.getText().toString());
        interestPerYear= Double.parseDouble(interestYearlyTxt.getText().toString());
        interestPerMonth= Double.parseDouble(interestMonthlyTxt.getText().toString());
        numberOfYears= Integer.parseInt(numberOfYearsTxt.getText().toString());
        numberOfMonths= Integer.parseInt(numberOfMonthsTxt.getText().toString());



        if(deposit!=0)
            principal = amount - deposit;

        if(deposit==0 )
            principal= amount;


        if(interestPerMonth!=0 && interestPerYear==0) {
            interest = interestPerMonth / 100;
            annual= (Math.pow(1+ interestPerMonth/100,12)-1 );

        }

        if(interestPerYear==0) {
            annual= (Math.pow(1+ interestPerMonth/100,12)-1 );
            interest = interestPerMonth / 100;
        }

        if(interestPerYear!=0 && interestPerMonth==0) {
            interest = interestPerYear / 12 / 100;
            annual= interestPerYear/100;

        }

        if(interestPerMonth==0) {
            interest = interestPerYear / 12 / 100;
            annual= interestPerYear/100;
        }
        if(numberOfMonths==0 && numberOfYears==0)
            months= 0;

        if(numberOfMonths!=0 && numberOfYears==0)
            months= numberOfMonths;

        if(numberOfMonths==0 && numberOfYears!=0)
            months= numberOfYears*12;

        if(numberOfMonths!=0 && numberOfYears!=0)
            months= numberOfMonths+ numberOfYears*12;




    }
    public void calculateMonthlyPayment(){

        computeData();
        monthlyPayment= principal*interest*Math.pow(1+interest, months)/ (Math.pow(1+interest, months)-1);

    }


    public void onResetButtonClick(View view){
        mInterstitialAd.show();
        clearForm((ViewGroup) findViewById(R.id.container_details));
        RelativeLayout layout= findViewById(R.id.container_results);
        layout.setVisibility(View.INVISIBLE);
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }


    public void onSaveLoanBtnClick(View view){


        EditText title= findViewById(R.id.loanName);
        if(title.getText().toString().isEmpty()){

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CalculatorActivity.this);
            builder.setMessage("To save a loan please input a loan title");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();

        }else {

            TextView loanName = findViewById(R.id.loanName);
            TextView resultPrincipal = findViewById(R.id.resultPrincipalPayment);
            TextView resultMonthlyPayment = findViewById(R.id.resultMonthlyPayment);
            TextView resultInterest = findViewById(R.id.resultInterest);
            TextView resultAnnual = findViewById(R.id.resultAnnual);
            TextView resultMonths = findViewById(R.id.resultMonths);


            sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

            ContentValues detailsValue = new ContentValues();
            detailsValue.put("NAME", loanName.getText().toString());
            detailsValue.put("PRINCIPAL", resultPrincipal.getText().toString());
            detailsValue.put("MONTHLYPAYMENT", resultMonthlyPayment.getText().toString());
            detailsValue.put("INTERESTRATE", resultInterest.getText().toString());
            detailsValue.put("ANNUALINTEREST", resultAnnual.getText().toString());
            detailsValue.put("MONTHS", resultMonths.getText().toString());


            sqLiteDatabase.insert("LOANDETAILS", null, detailsValue);
            sqLiteDatabase.close();

            Intent intent = new Intent(this, SavedLoansActivity.class);
            startActivity(intent);
            finish();
        }



    }



    public void onAmortizationBtnClick(View view){

        mInterstitialAd.show();
        computeData();
        TextView resultName= findViewById(R.id.loanName);
        TextView resultPrincipal= findViewById(R.id.resultPrincipalPayment);
        TextView resultInterest= findViewById(R.id.resultInterest);
        TextView resultAnnual= findViewById(R.id.resultAnnual);
        TextView resultMonths= findViewById(R.id.resultMonths);
        TextView resultMonthlyPayment= findViewById(R.id.resultMonthlyPayment);

        Intent intent= new Intent(this, AmortizationActivity.class);
        intent.putExtra("NAME",resultName.getText().toString());
        intent.putExtra("MONTHS", resultMonths.getText().toString());
        intent.putExtra("INTEREST", resultInterest.getText().toString());
        intent.putExtra("PRINCIPAL", resultPrincipal.getText().toString());
        intent.putExtra("MONTHLYPAYMENT", resultMonthlyPayment.getText().toString());
        intent.putExtra("ANNUALINTEREST",resultAnnual.getText().toString());


        startActivity(intent);
    }




}