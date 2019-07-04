package com.example.blooddonation;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class DonorFrag extends Fragment {
    FirebaseStorage firebaseStorage ;
    FirebaseFirestore db;
    DocumentReference documentReference;
    FirebaseAuth mAuth;
    ListView Donor_List;
    String xx;
    List<MainData> list;
    Bitmap bitmap;
    ImageView imageView;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donor, container, false);

        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.grey));

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Donor_List = getActivity().findViewById(R.id.list_of_donors);
        xx = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("userUid ",xx);
        list = new ArrayList<MainData>();
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            new getAllDonorData().execute();

        }
        imageView = getActivity().findViewById(R.id.img_profile_go_donor);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePage pp = new profilePage();
                getFragmentManager().beginTransaction().replace(R.id.frame,pp).commit();
            }
        });

    }



    public  class  getAllDonorData extends AsyncTask<Void,Void,Void>{

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


            db.collection("user").get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                  if (task.isSuccessful()){

                      for (QueryDocumentSnapshot document : task.getResult())
                      {
                          Log.d("alldata", document.getId() + " => " + document.getData());

                          String  names = document.getString("name");
                          String  bloodGroup = document.getString("group");
                          String  picLink    = document.getString("picturelink");
                          String  userIdD     = document.getId();

                          URL url = null;
                          try {
                              url = new URL(picLink);
                          } catch (MalformedURLException e) {
                              e.printStackTrace();
                          }
                          try {
                              bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                          } catch (IOException e)
                          {
                              e.printStackTrace();
                          }

                          MainData mainData = new MainData(names,bloodGroup,bitmap,userIdD);
                          list.add(mainData);

                          DonorAdapter donorAdapter = new DonorAdapter(getActivity(),list);
                          Donor_List.setAdapter(donorAdapter);

                          Log.d("listData", list.toString());
                      }
                  } else {
                      Log.w(TAG, "Error getting documents.", task.getException());
                  }
                }
            });

            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
