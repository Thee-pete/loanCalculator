package com.zuture.apps.carloancalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

public class AmortizationActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amortization);

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

        populateAmortizationSchedule();
    }

    public void populateAmortizationSchedule() {


        TableLayout tbl = findViewById(R.id.wizTbl);


        Intent intent = getIntent();
        String monthsStr = intent.getExtras().getString("MONTHS");
        String principalStr = intent.getExtras().getString("PRINCIPAL");
        String interestStr = intent.getExtras().getString("INTEREST");
        String monthlypaymentStr= intent.getExtras().getString("MONTHLYPAYMENT");
        String nameStr= intent.getExtras().getString("NAME");
        String annualIntStr= intent.getExtras().getString("ANNUALINTEREST");


        TextView detsTitle= findViewById(R.id.detsTitle);
        detsTitle.setText(nameStr);

        TextView detsMonthlyPayment= findViewById(R.id.detsMonthlyPayment);
        detsMonthlyPayment.setText(monthlypaymentStr);

        TextView detsPrincipal= findViewById(R.id.detsPrincipalPayment);
        detsPrincipal.setText(principalStr);

        TextView detsInterest= findViewById(R.id.detsInterest);
        detsInterest.setText(interestStr);

        TextView detsAnnualInterest= findViewById(R.id.detsAnnualInterest);
        detsAnnualInterest.setText(annualIntStr);

        TextView detsMonths= findViewById(R.id.detsMonths);
        detsMonths.setText(monthsStr);


        int months = Integer.parseInt(monthsStr);
        double interest = Double.parseDouble(interestStr);
        double principal = Double.parseDouble(principalStr);
        double monthlyPayment= Double.parseDouble(monthlypaymentStr);

        double interestPaid;
        double principalPaid;
        double newBalance = 0;

        int i;


        // tbl.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        TableRow titlerow = new TableRow(getApplicationContext());
        titlerow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView titleMonthNum = new TextView(getApplicationContext());
        titleMonthNum.setText("Month");
        titleMonthNum.setTextSize(14);
        titleMonthNum.setTextColor(getResources().getColor(R.color.colorText));
        titleMonthNum.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView titlePrincipal = new TextView(this);
        titlePrincipal.setTextSize(14);
        titlePrincipal.setTextColor(getResources().getColor(R.color.colorText));
        titlePrincipal.setText("Principal");

        TextView titleInterestpaid = new TextView(this);
        titleInterestpaid.setTextSize(14);
        titleInterestpaid.setTextColor(getResources().getColor(R.color.colorText));
        titleInterestpaid.setText("Interest Paid");

        TextView titleNewBalance = new TextView(this);
        titleNewBalance.setTextSize(14);
        titleNewBalance.setTextColor(getResources().getColor(R.color.colorText));
        titleNewBalance.setText("New Balance");


        titlerow.addView(titleMonthNum);
        titlerow.addView(titlePrincipal);
        titlerow.addView(titleInterestpaid);
        titlerow.addView(titleNewBalance);

        tbl.addView(titlerow);
        tbl.setStretchAllColumns(true);


        for (i=1; i<months; i++ ){


            interestPaid = principal * interest;
            principalPaid = monthlyPayment - interestPaid;
            newBalance = principal - principalPaid;









            TableRow row = new TableRow(getApplicationContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));



            TextView valueMonthNum = new TextView(getApplicationContext());
            valueMonthNum.setText(String.valueOf(i));
            valueMonthNum.setTextSize(14);
            valueMonthNum.setBackgroundColor(getResources().getColor(R.color.colorText));
            valueMonthNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            valueMonthNum.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView valuePrincipal = new TextView(this);
            valuePrincipal.setTextSize(14);
            valuePrincipal.setBackgroundColor(getResources().getColor(R.color.colorrow));
            valuePrincipal.setTextColor(getResources().getColor(R.color.colorPrimary));
            valuePrincipal.setText(String.format("%, .2f",principal));

            TextView valueInterestpaid = new TextView(this);
            valueInterestpaid.setTextSize(14);
            valueInterestpaid.setBackgroundColor(getResources().getColor(R.color.colorText));
            valueInterestpaid.setTextColor(getResources().getColor(R.color.colorPrimary));
            valueInterestpaid.setText(String.format("%, .2f",interestPaid));

            TextView valueNewBalance = new TextView(this);
            valueNewBalance.setTextSize(14);
            valueNewBalance.setBackgroundColor(getResources().getColor(R.color.colorrow));
            valueNewBalance.setTextColor(getResources().getColor(R.color.colorPrimary));
            valueNewBalance.setText(String.format("%, .2f",newBalance));

            row.addView(valueMonthNum);
            row.addView(valuePrincipal);
            row.addView(valueInterestpaid);
            row.addView(valueNewBalance);

            tbl.setStretchAllColumns(true);
            tbl.addView(row);


            principal=newBalance;

        }


        principalPaid= principal;
        interestPaid= principal*interest;
        newBalance= 0.0;

        int monthNum = months;

        TableRow row = new TableRow(getApplicationContext());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView valueMonthNum = new TextView(getApplicationContext());
        valueMonthNum.setText(String.valueOf(monthNum));
        valueMonthNum.setTextSize(14);
        valueMonthNum.setBackgroundColor(getResources().getColor(R.color.colorText));
        valueMonthNum.setTextColor(getResources().getColor(R.color.colorPrimary));
        valueMonthNum.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView valuePrincipal = new TextView(this);
        valuePrincipal.setTextSize(14);
        valuePrincipal.setBackgroundColor(getResources().getColor(R.color.colorrow));
        valuePrincipal.setTextColor(getResources().getColor(R.color.colorPrimary));
        valuePrincipal.setText(String.format("%, .2f",principal));

        TextView valueInterestpaid = new TextView(this);
        valueInterestpaid.setTextSize(14);
        valueInterestpaid.setBackgroundColor(getResources().getColor(R.color.colorText));
        valueInterestpaid.setTextColor(getResources().getColor(R.color.colorPrimary));
        valueInterestpaid.setText(String.format("%, .2f",interestPaid));

        TextView valueNewBalance = new TextView(this);
        valueNewBalance.setTextSize(14);
        valueNewBalance.setBackgroundColor(getResources().getColor(R.color.colorrow));
        valueNewBalance.setTextColor(getResources().getColor(R.color.colorPrimary));
        valueNewBalance.setText(String.format("%, .2f",newBalance));

        row.addView(valueMonthNum);
        row.addView(valuePrincipal);
        row.addView(valueInterestpaid);
        row.addView(valueNewBalance);

        tbl.addView(row);
        tbl.setStretchAllColumns(true);












    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }

}