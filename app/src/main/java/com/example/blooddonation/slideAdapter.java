package com.example.blooddonation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class slideAdapter extends PagerAdapter {

    Context context ;
    LayoutInflater inflater;

    public slideAdapter(Context context) {
        this.context = context;
    }


    public  int [] CenterImages = {

            R.drawable.happy,
            R.drawable.donor,
            R.drawable.rqst

    };


    public  int [] Bottomimages = {

            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3

    };

    public  String [] Titles = {

            "Search Donors",
            "Become A Donor",
            "Add Blood Requests"

    };

    public  String [] Descriptions = {

            "Search for donor near yours place and easily find donor",
            "Become a donor and help needy people",
            "You can also put requets for blood in any need"

    };

    @Override
    public int getCount()
    {
        return Titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (View) inflater.inflate(R.layout.slide_layout,container,false);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        ImageView img = view.findViewById(R.id.imageView1);
        ImageView img2 = view.findViewById(R.id.imageView2);
        TextView tittle= view.findViewById(R.id.title);
        TextView desc =  view.findViewById(R.id.description);

        img.setImageResource(CenterImages[position]);
        img2.setImageResource(Bottomimages[position]);
        tittle.setText(Titles[position]);
        desc.setText(Descriptions[position]);

        container.addView(view);

        return view;
    };

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
