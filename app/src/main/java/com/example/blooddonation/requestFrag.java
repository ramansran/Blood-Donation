package com.example.blooddonation;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class requestFrag extends Fragment implements View.OnClickListener {

ImageView goToProfile;
Button AddRequest;
String DI;
FirebaseFirestore db;
ListView listView;
List<MainData2> list;
Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_request, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle GB = getArguments();
        DI = GB.getString("DI");

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        goToProfile = getActivity().findViewById(R.id.img_profile2);
        AddRequest = getActivity().findViewById(R.id.add_resquest);

        listView = getActivity().findViewById(R.id.request_List);


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            new getRequests().execute();

        }


        goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePage pp = new profilePage();
                getFragmentManager().beginTransaction().replace(R.id.frame,pp).commit();
            }
        });

        AddRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRequest addRequest = new AddRequest();
                Bundle bundle = new Bundle();
                bundle.putString("DII",DI);
                addRequest.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frame,addRequest).commit();
            }
        });

    }


public  class getRequests extends AsyncTask<Void,Void,Void>{

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

        db.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot :task.getResult()){
                        Log.d("alldata", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                        String Name = documentSnapshot.getString("name");
                        String Place = documentSnapshot.getString("where");
                        String ForWhom = documentSnapshot.getString("for");
                        String NeededGroup = documentSnapshot.getString("neededgroup");
                        String PicLink = documentSnapshot.getString("picturelink");
                        String  uid     = documentSnapshot.getId();

                        URL url = null;
                        try {
                            url = new URL(PicLink);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        try {
                            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (ForWhom != null) {
                            MainData2 mainData2 = new MainData2(Name, Place, ForWhom, NeededGroup, bitmap, uid);
                            list.add(mainData2);


                            RequestAdapter requestAdapter = new RequestAdapter(getActivity(), list);
                            listView.setAdapter(requestAdapter);
                        }
                        else {
                            Toast.makeText(getActivity(),"",Toast.LENGTH_LONG).show();
                        }

                        Log.d("listData", list.toString());
                    }
                }
            }
        });


        return null;
    }

}



    @Override
    public void onClick(View v) {
    }






}
