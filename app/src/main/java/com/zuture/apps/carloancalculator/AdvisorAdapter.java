package com.zuture.apps.carloancalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class AdvisorAdapter extends PagerAdapter {

    private List<Advice> adviceList;
    private LayoutInflater layoutInflater;
    private Context context;

    public AdvisorAdapter(List<Advice> adviceList, Context context) {
        this.adviceList = adviceList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return adviceList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater= LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.card_advisor,container,false);
        ImageView imageView;
        TextView title;
        TextView description;

        imageView= view.findViewById(R.id.imageadvisor);
        title= view.findViewById(R.id.advstitle);
        description= view.findViewById(R.id.advsdesc);

        imageView.setImageResource(adviceList.get(position).getImageRsc());
        title.setText(adviceList.get(position).getTitle());
        description.setText(adviceList.get(position).getDesc());


        container.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View)object);
    }
}

