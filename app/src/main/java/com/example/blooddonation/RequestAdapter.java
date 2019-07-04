package com.example.blooddonation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RequestAdapter extends BaseAdapter {

    FirebaseUser firebaseUser;
    Activity activity;
    List<MainData2> list;
    public RequestAdapter(FragmentActivity activity, List<MainData2> list) {
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

    public  class Viewholder{
        ImageView imageView;
        TextView Name;
        TextView Place;
        TextView For;
        TextView Group;
        Button button;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder viewholder = null;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = (View) inflater.inflate(R.layout.request_design,null);

            viewholder = new Viewholder();

            viewholder.imageView = convertView.findViewById(R.id.first_profile_request_img);
            viewholder.Name      = convertView.findViewById(R.id.first_profile_txt_name);
            viewholder.Place     = convertView.findViewById(R.id.desc_txt_desc);
            viewholder.For       = convertView.findViewById(R.id.desc_txt_desc);
            viewholder.Group     = convertView.findViewById(R.id.nB);
            viewholder.button    = convertView.findViewById(R.id.first_offer);

            MainData2 mainData2 = list.get(position);

            viewholder.imageView.setImageBitmap(mainData2.getBitmap());
            viewholder.Name.setText(mainData2.getName());
            viewholder.For.setText("Need For "+mainData2.getForwohom()+" In "+mainData2.getPlace());
            viewholder.Group.setText(mainData2.getBloodGroup());

            convertView.setTag(viewholder);


            final String UserID = mainData2.getUid();

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String mid= firebaseUser.getUid();
            if (mid.equals(UserID)){
                viewholder.button.setEnabled(false);
            }
            else {
                viewholder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        ChatFragmenmt chatFragmenmt = new ChatFragmenmt();
                        Bundle bundle = new Bundle();
                        bundle.putString("IDOFUSER", UserID);
                        chatFragmenmt.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, chatFragmenmt).commit();
                    }
                });
            }

        }
        else {
            viewholder = (Viewholder) convertView.getTag();
        }
        return convertView;
    }
}
