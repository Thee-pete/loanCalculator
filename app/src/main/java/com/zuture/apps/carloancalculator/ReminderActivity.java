package com.zuture.apps.carloancalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.SQLException;

import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;

import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class ReminderActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {


    AlarmCursorAdapter mCursorAdapter;
    DatabaseHelper alarmReminderDbHelper = new DatabaseHelper(this);
    ListView reminderListView;
    ProgressDialog prgDialog;
    private InterstitialAd mInterstitialAd;

    private static final int VEHICLE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);



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
        bottomNavigationView.setSelectedItemId(R.id.reminderoption);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {



                switch(item.getItemId()){

                    case R.id.homeaption:
                        Intent intentthree= new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intentthree);
                        finish();
                        break;

                    case R.id.calcaption:
                        Intent intent= new Intent(getApplicationContext(), CalculatorActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.savedloandoption:
                        Intent intenttwo= new Intent(getApplicationContext(), SavedLoansActivity.class);
                        startActivity(intenttwo);
                        finish();
                        break;
                    case R.id.reminderoption:
                       Intent intent1=getIntent();
                       startActivity(intent1);
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



        try{
            SQLiteOpenHelper sqLiteOpenHelper=  new DatabaseHelper(this);
            Cursor cursor = ((DatabaseHelper) sqLiteOpenHelper).readAllReminderetails();
            if(cursor.getCount()==0) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ReminderActivity.this);
                builder.setTitle("No Reminders");
                builder.setMessage(getResources().getString(R.string.no_remider));
                builder.setPositiveButton("Add Reminder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent= new Intent(ReminderActivity.this,SavedLoansActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent= new Intent(ReminderActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
            }}catch (SQLException e){
            Toast.makeText(this,"Error showing dialog",Toast.LENGTH_LONG).show();
        }


        reminderListView = (ListView) findViewById(R.id.list);

       // View emptyView = findViewById(R.id.empty_view);
        //reminderListView.setEmptyView(emptyView);

        mCursorAdapter = new AlarmCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);


        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                mInterstitialAd.show();
                Intent intent = new Intent(ReminderActivity.this, SetReminderActivity.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);
                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);


                startActivity(intent);

            }
        });




        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
}