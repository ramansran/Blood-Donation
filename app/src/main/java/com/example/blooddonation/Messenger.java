package com.example.blooddonation;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Messenger extends Fragment {
FirebaseFirestore db ;
FirebaseStorage firebaseStorage;
FirebaseAuth mAuth;
FirebaseUser firebaseUser;
DocumentReference documentReference;
ListView listView;
Bitmap bitmap;
ImageView imageViewm;
List<String> list;
List<Messenger_MainData>newList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_messenger, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db= FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        list = new ArrayList<>();
        newList = new ArrayList<>();
        listView = getActivity().findViewById(R.id.messengerList);
        imageViewm = getActivity().findViewById(R.id.img_profile_m);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            new getMessengerLiat().execute();

        }

        imageViewm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePage pp = new profilePage();
                getFragmentManager().beginTransaction().replace(R.id.frame,pp).commit();
            }
        });
    }

    public class getMessengerLiat extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Fecthing Data...");
            dialog.setMessage("please wait...");
            dialog.show();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    dialog.cancel();
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable,2000);
        }


        @Override
        protected Void doInBackground(Void... voids) {



            db.collection("Chats").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot value, @javax.annotation.Nullable FirebaseFirestoreException e) {

                    list.clear();
                    for (QueryDocumentSnapshot doc : value){
                        ChatMainData chatMainData = doc.toObject(ChatMainData.class);
                        if (chatMainData.getSender().equals(firebaseUser.getUid())){
                            list.add(chatMainData.getReciever());
                        }
                        if (chatMainData.getReciever().equals(firebaseUser.getUid())){
                            list.add(chatMainData.getSender());
                        }

                    }
                    readChats();
                }
            });


            return null;
        }
    }


    public  void  readChats(){
       newList  = new ArrayList<>();

       db.collection("user").addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@javax.annotation.Nullable QuerySnapshot value2, @javax.annotation.Nullable FirebaseFirestoreException e) {
               newList.clear();
               for (QueryDocumentSnapshot doc2 : value2){
                   Messenger_MainData mmm = doc2.toObject(Messenger_MainData.class);
                   for (String id : list){
                       if (mmm.getMuserid().equals(id) )
                       {
                         if (newList.size()!=0){
                             for (Messenger_MainData mmm1 : newList){
                               if (!mmm.getMuserid().equals(mmm1.getMuserid())){
                                   newList.add(mmm);
                               }
                             }

                         }
                         else {
                             newList.add(mmm);
                         }
                       }
                   }


               }
               messengerAdapter ma = new messengerAdapter(getActivity(),newList);
               listView.setAdapter(ma);
           }
       });


    }








}
