package com.example.blooddonation;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Context.MODE_PRIVATE;

public class FirstFrag extends Fragment {

  ViewPager     slideViewPager;
  LinearLayout  dotsPlace;
  slideAdapter  sA;
  TextView  []  dots;
  Context context;
  Button nxtbtn,prevbtn;
  int curentpage;
  int pos = 0;
 FirebaseAuth mauth;



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        mauth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs2", MODE_PRIVATE);
        boolean b = preferences.getBoolean("ramans2", false);

       if (b == true) {

            profilePage bdf = new profilePage();
            getFragmentManager().beginTransaction().replace(R.id.frame, bdf).commit();

        }
            slideViewPager = view.findViewById(R.id.viewpager);
            dotsPlace = view.findViewById(R.id.liner);
            nxtbtn = view.findViewById(R.id.next);
            prevbtn = view.findViewById(R.id.back);


            sA = new slideAdapter(getActivity());
            slideViewPager.setAdapter(sA);


            slideViewPager.addOnPageChangeListener(viewListner);
            addDotindicator(0);


            nxtbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pos = slideViewPager.getCurrentItem();
                    if (pos == dots.length - 1) {
                        Fragment fragment = new MainScreen();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame, fragment);
                        ft.commit();
                        //   savepref();

                    } else {

                        slideViewPager.setCurrentItem(curentpage + 1);
                    }
                }
            });


            prevbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slideViewPager.setCurrentItem(curentpage - 1);
                }
            });

            return view;
        }



        public void addDotindicator ( int position){

            dots = new TextView[3];
            dotsPlace.removeAllViews();

            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(getActivity());
                dots[i].setText(Html.fromHtml("&#8226"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(getResources().getColor(R.color.transparent));

                dotsPlace.addView(dots[i]);
            }


            if (dots.length > 0) {
                dots[position].setTextColor(getResources().getColor(R.color.White));
            }
        }


        ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                addDotindicator(i);
                curentpage = i;
                if (i == 0) {
                    nxtbtn.setEnabled(true);
                    prevbtn.setEnabled(false);
                    prevbtn.setVisibility(View.INVISIBLE);

                    nxtbtn.setText("NEXT");
                    prevbtn.setText("");
                } else if (i == dots.length - 1) {
                    nxtbtn.setEnabled(true);
                    prevbtn.setEnabled(true);
                    prevbtn.setVisibility(View.VISIBLE);

                    nxtbtn.setText("FINISH");
                    prevbtn.setText("BACK");
                } else {
                    nxtbtn.setEnabled(true);
                    prevbtn.setEnabled(true);
                    prevbtn.setVisibility(View.VISIBLE);

                    nxtbtn.setText("NEXT");
                    prevbtn.setText("BACK");
                }


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        };



}
