package com.zuture.apps.carloancalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    AdvisorAdapter advisorAdapter;
    List<Advice> adviceList;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);


        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(3)
                .session(3)
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {

                    }
                }).build();

        ratingDialog.show();


        MobileAds.initialize(this);
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



        adviceList= new ArrayList<>();
        adviceList.add(new Advice(getResources().getString(R.string.advicetitleone),getResources().getString(R.string.adviceone), R.drawable.imageseven));
        adviceList.add(new Advice(getResources().getString(R.string.advicetitletwo),getResources().getString(R.string.advicetwo), R.drawable.imagefour));
        adviceList.add(new Advice(getResources().getString(R.string.advicetitlethree),getResources().getString(R.string.advicethree), R.drawable.imagefive));
        adviceList.add(new Advice(getResources().getString(R.string.advicetitlefour),getResources().getString(R.string.advicefour), R.drawable.imagetwo));
        adviceList.add(new Advice(getResources().getString(R.string.advicetitlefive), getResources().getString(R.string.advicefive), R.drawable.imageone));


        advisorAdapter= new AdvisorAdapter(adviceList,this);

        viewPager= findViewById(R.id.viewPager);
        viewPager.setAdapter(advisorAdapter);
        viewPager.setCurrentItem(1);
        viewPager.setPadding(130,0,130,0);


       BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
       bottomNavigationView.setSelectedItemId(R.id.homeaption);
       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {



               switch(item.getItemId()){

                   case R.id.homeaption:
                       Intent intentfour= getIntent();
                       startActivity(intentfour);
                       break;

                   case R.id.calcaption:
                       Intent intent= new Intent(getApplicationContext(), CalculatorActivity.class);
                       startActivity(intent);
                       break;
                   case R.id.savedloandoption:
                       Intent intenttwo= new Intent(getApplicationContext(), SavedLoansActivity.class);
                       startActivity(intenttwo);
                       break;
                   case R.id.reminderoption:

                       if (mInterstitialAd.isLoaded())
                           mInterstitialAd.show();

                       Intent intentthree= new Intent(getApplicationContext(),ReminderActivity.class);
                       startActivity(intentthree);
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


    }








    public void onOpenCalculatorClick(View view){
        Intent intent= new Intent(this, CalculatorActivity.class);
        startActivity(intent);
    }
    public void onOpenSavedLoansClick(View view){
        Intent intent= new Intent(this, SavedLoansActivity.class);
        startActivity(intent);
    }
    public void onOpenReminderBtnClick(View view){
        Intent intent= new Intent(this,ReminderActivity.class);
        startActivity(intent);
    }


}