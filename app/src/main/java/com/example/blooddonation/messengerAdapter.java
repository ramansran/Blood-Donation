package com.example.blooddonation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;



public class messengerAdapter extends BaseAdapter {
    Bitmap bit;
    Activity activity;
    List<Messenger_MainData> list;

    public messengerAdapter(FragmentActivity activity, List<Messenger_MainData> list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderM viewHolder = null;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.messenger_design,null);

            viewHolder = new ViewHolderM();

            viewHolder.imageView = convertView.findViewById(R.id.first_profile_img_m);
            viewHolder.textView = convertView.findViewById(R.id.first_profile_txt_m);

            Messenger_MainData mm = list.get(position);




            viewHolder.textView.setText(mm.getName());

            final String UserID = mm.getMuserid();

            final String path = mm.getPicturelink();
            URL url = null;
            try {
                url = new URL(path);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                bit = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            viewHolder.imageView.setImageBitmap(bit);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity,"user id :" +UserID ,Toast.LENGTH_LONG).show();

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    ChatFragmenmt chatFragmenmt = new ChatFragmenmt();
                    Bundle bundle = new Bundle();
                    bundle.putString("IDOFUSER",UserID);
                    chatFragmenmt.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,chatFragmenmt).commit();
                }
            });

            convertView.setTag(viewHolder);


        }
        else {
         viewHolder= (ViewHolderM) convertView.getTag();
        }


        return convertView;
    }

    public  class  ViewHolderM {
        ImageView imageView;
        TextView textView;
    }


}
