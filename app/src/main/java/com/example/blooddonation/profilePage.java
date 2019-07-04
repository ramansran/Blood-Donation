package com.example.blooddonation;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.UUID;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class profilePage extends Fragment implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ImageView profile_img,plus_img;
    Bitmap bitmap;
    String UserName,UserEmail,User_Blood_Group,User_Gender,UserID2;
    boolean b;
    TextView nameView,groupView;
    Task<Uri> url;
    String photoLink,OldLink,OldDocId;
    DocumentReference documentReference;
    FirebaseFirestore db;
    LinearLayout linkLayout;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private DrawerLayout drawerLayout2;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);


            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.profile));

            Toolbar toolbar = view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            drawerLayout2 = view.findViewById(R.id.drawerLayout);
            NavigationView navigationView = view.findViewById(R.id.nvView);
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout2, toolbar, R.string.openDrawer, R.string.closeDrawer);
            drawerLayout2.addDrawerListener(toggle);
            toggle.syncState();


            return view;


        }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        Bundle comingBundle = getArguments();
        if (comingBundle != null) {
            OldLink = comingBundle.getString("picLink");
            OldDocId = comingBundle.getString("Doclink");
        }
        else {

            SharedPreferences sharedPreferences3 = getActivity().getSharedPreferences("pauseprefs", MODE_PRIVATE);
            OldLink = sharedPreferences3.getString("PicLink", null);
            OldDocId = sharedPreferences3.getString("DocLink", null);
        }

            new getImage().execute();
            profile_img = getActivity().findViewById(R.id.img_profile2);
            plus_img = getActivity().findViewById(R.id.img_plus);
            plus_img.setOnClickListener(this);
            nameView = getActivity().findViewById(R.id.name);
            groupView = getActivity().findViewById(R.id.desc);
            UserID2 = FirebaseAuth.getInstance().getCurrentUser().getUid();

            getActivity().findViewById(R.id.profile_donor_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DonorFrag donorFrag = new DonorFrag();
                    getFragmentManager().beginTransaction().replace(R.id.frame, donorFrag).commit();
                }
            });


     getActivity().findViewById(R.id.profile_request_layout).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             requestFrag rf = new requestFrag();
             Bundle bundle = new Bundle();
             bundle.putString("DI",OldDocId);
             rf.setArguments(bundle);
             getFragmentManager().beginTransaction().replace(R.id.frame,rf).commit();
         }
     });

      getActivity().findViewById(R.id.your_message_layout).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Messenger messenger = new Messenger();
              getFragmentManager().beginTransaction().replace(R.id.frame,messenger).commit();
          }
      });
    }


    @Override
    public void onPause() {
        super.onPause();
         SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("pauseprefs",MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences2.edit();
         editor.putString("PicLink",OldLink);
         editor.putString("DocLink",OldDocId);
         editor.commit();

    }



    public  class   getImage extends AsyncTask<Void,Void,Void>{
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

            try {

                URL imageUrl = new URL(OldLink);
                bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }




              documentReference = db.collection("user").document(OldDocId);
              documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()) {

                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        UserName = document.getString("name");
                                        nameView.setText(UserName);

                                        User_Blood_Group = document.getString("group");
                                        groupView.setText("Your Blood Group : "+User_Blood_Group);


                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                }
          });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            profile_img.setImageBitmap(bitmap);
        }
    }









    @Override
    public void onClick(View v) {
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.profileicon:
                become_donor_frag bdf = new become_donor_frag();
                getFragmentManager().beginTransaction().replace(R.id.frame,bdf).commit();
                break;

            case R.id.chatIcon:
                Messenger messenger = new Messenger();
                getFragmentManager().beginTransaction().replace(R.id.frame,messenger).commit();
                break;

        }
        return false;
    }




}
