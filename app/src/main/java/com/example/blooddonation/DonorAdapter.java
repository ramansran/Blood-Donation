package com.example.blooddonation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class DonorAdapter extends BaseAdapter {

    FirebaseUser firebaseUser ;
    Activity activity;
    List<MainData> list;

    public DonorAdapter(FragmentActivity activity, List<MainData> list) {
       this.activity = activity;
       this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  class  Viewholder {
        TextView textView ;
        TextView textView1;
        ImageView imageView;
        Button   button;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Viewholder viewholder = null;
        if (convertView == null ){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = (View) inflater.inflate(R.layout.donordesign,null);

            viewholder = new Viewholder();

            viewholder.textView = convertView.findViewById(R.id.first_profile_txt);
            viewholder.textView1 = convertView.findViewById(R.id.group_txt);
            viewholder.imageView = convertView.findViewById(R.id.first_profile_img);
            viewholder.button   = convertView.findViewById(R.id.become_donor_ok_btn_ask);

            MainData mainData = list.get(position);

            viewholder.textView.setText(mainData.getName());
            viewholder.textView1.setText(mainData.getBloodGroup());
            viewholder.imageView.setImageBitmap(mainData.getBitmap1());


            final String UserID = mainData.getUserid();

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String mid= firebaseUser.getUid();

            if (mid.equals(UserID)){
                viewholder.button.setEnabled(false);
            }
            else {
                viewholder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "user id :" + UserID, Toast.LENGTH_LONG).show();

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        ChatFragmenmt chatFragmenmt = new ChatFragmenmt();
                        Bundle bundle = new Bundle();
                        bundle.putString("IDOFUSER", UserID);
                        chatFragmenmt.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, chatFragmenmt).commit();
                    }
                });

            }
            convertView.setTag(viewholder);

        }
        else {
            viewholder= (Viewholder) convertView.getTag();
        }
        return convertView;
    }


}
