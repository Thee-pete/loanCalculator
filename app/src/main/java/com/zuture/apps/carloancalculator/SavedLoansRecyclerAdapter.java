package com.zuture.apps.carloancalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class SavedLoansRecyclerAdapter extends RecyclerView.Adapter<SavedLoansRecyclerAdapter.ViewHolder> {


    ArrayList<String> loanNameArray;
    ArrayList<String> principalArray;
    ArrayList<String>  monthlyPaymentArray;
    ArrayList<String>  annualInterestArray;
    ArrayList<String> interestRateArray;
    ArrayList<String> monthsArray;



    public SavedLoansRecyclerAdapter( ArrayList<String> loanNameArray,ArrayList<String> principalArray, ArrayList<String >  monthlyPaymentArray, ArrayList<String> interestRateArray, ArrayList<String>  annualInterestArray,ArrayList<String> monthsArray){
        this.loanNameArray=loanNameArray;
        this.principalArray=principalArray;
        this.monthlyPaymentArray=monthlyPaymentArray;
        this.annualInterestArray= annualInterestArray;
        this.interestRateArray=interestRateArray;
        this.monthsArray=monthsArray;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cardView;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            cardView= (RelativeLayout) itemView;
        }
    }


    @NonNull
    @Override
    public SavedLoansRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout cardView= (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_saved_loan,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull  SavedLoansRecyclerAdapter.ViewHolder holder, final int position) {

        final RelativeLayout cardView= holder.cardView;
        final Context context= cardView.getContext();
        final TextView nameTxt= cardView.findViewById(R.id.loanNameCard);
        nameTxt.setText(String.valueOf(loanNameArray.get(position)));

        final TextView prTxt= cardView.findViewById(R.id.principalcard);
        prTxt.setText(String.valueOf(principalArray.get(position)));

        TextView mpTxt= cardView.findViewById(R.id.monthlypaymentcard);
        mpTxt.setText(String.valueOf(monthlyPaymentArray.get(position)));


        final TextView intrTxt= cardView.findViewById(R.id.interestRateCard);
        intrTxt.setText(String.valueOf(interestRateArray.get(position)));

        TextView annualintrTxt= cardView.findViewById(R.id.annualInterestCard);
        annualintrTxt.setText(String.valueOf(annualInterestArray.get(position)));

        final TextView mnTxt= cardView.findViewById(R.id.monthsCard);
        mnTxt.setText(String.valueOf(monthsArray.get(position)));



        ImageButton viewDetailsBtn= cardView.findViewById(R.id.viewDetails);
        viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent= new Intent(context,AmortizationActivity.class);
                intent.putExtra("NAME",nameTxt.getText().toString());
                intent.putExtra("MONTHS",mnTxt.getText().toString());
                intent.putExtra("PRINCIPAL",prTxt.getText().toString());
                intent.putExtra("INTEREST",intrTxt.getText().toString());
                intent.putExtra("MONTHLYPAYMENT",mpTxt.getText().toString());
                intent.putExtra("ANNUALINTEREST", annualintrTxt.getText().toString());
                context.startActivity(intent);


            }
        });

        ImageButton setReminderBtn= cardView.findViewById(R.id.setReminderBtn);
        SQLiteOpenHelper sqLiteOpenHelper= new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase= sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor= sqLiteDatabase.query("vehicles",new String[]{"title"},"title=?",new String[]{loanNameArray.get(position)},null,null,null);
        if(cursor.getCount()>=1){
            setReminderBtn.setBackgroundResource(R.drawable.round_enabled);
            setReminderBtn.setImageResource(R.drawable.notificationson);

        }

        setReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent= new Intent(context, SetReminderActivity.class);
                    intent.putExtra("TITLE", nameTxt.getText().toString());
                    context.startActivity(intent);
               // ((Activity)context).finish();

            }
        });

        final ImageButton deleteButton= cardView.findViewById(R.id.deleteDetails);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(cardView.getContext());
                builder.setTitle("DELETE LOAN");
                builder.setMessage("Delete Saved loan?");
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try{
                        SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(context);
                        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
                        sqLiteDatabase.delete("LOANDETAILS","NAME=?",new String[]{loanNameArray.get(position)});
                        loanNameArray.remove(position);
                        principalArray.remove(position);
                        interestRateArray.remove(position);
                        monthlyPaymentArray.remove(position);

                        monthsArray.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        sqLiteDatabase.close();
                        }catch (SQLException e){

                            Toast.makeText(context,"Database Error",Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(context,"Record deleted",Toast.LENGTH_LONG).show();



                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();

            }
        });


    }

    @Override
    public int getItemCount() {

        return loanNameArray.size();
    }
}
