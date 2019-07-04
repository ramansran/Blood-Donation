package com.example.blooddonation;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class become_donor_frag extends Fragment implements View.OnClickListener {
    FirebaseFirestore db;
    EditText nametxt,emailtxt;
    RadioButton radiobtn,radiobtn2,radiobtn3;
    RadioGroup radioGroup,radioGroup2;
    ImageView imageView;
    Button Ok;
    private static final int RESULT_LOAD_IMAGE = 1;
    Uri uri;
    Task<Uri> url;
    Map<String,Object> map;
    String documentId,photoLink,userId;
    TextView textView1,textView2;

    FirebaseAuth mAuth;

    String uid;
    private ProgressBar progressBar;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();









    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_become_donor_frag, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



           /* userId = FirebaseAuth.getInstance().getCurrentUser().getUid();*/
            Ok = getActivity().findViewById(R.id.become_donor_ok_btn);
            Ok.setOnClickListener(this);

            radioGroup = getActivity().findViewById(R.id.gender_radio_buttons);
            radioGroup.setOnClickListener(this);

            radioGroup2 = getActivity().findViewById(R.id.blood_group_buttons);
            radioGroup2.setOnClickListener(this);

            nametxt = getActivity().findViewById(R.id.name_edit_text);
            emailtxt = getActivity().findViewById(R.id.e_mail_edit_text);

            radiobtn3 = getActivity().findViewById(R.id.numbr_visible_txt);


            imageView = getActivity().findViewById(R.id.img_profile);
            imageView.setOnClickListener(this);

            textView1 = getActivity().findViewById(R.id.genderError);
            textView2 = getActivity().findViewById(R.id.groupError);

            progressBar = getActivity().findViewById(R.id.pbar);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                }
            });


            db = FirebaseFirestore.getInstance();
            mAuth= FirebaseAuth.getInstance();
            userId = mAuth.getCurrentUser().getUid();
            map = new  HashMap<>();
        }



    @Override
    public void onClick(View v){
              switch (v.getId()){
                  case R.id.become_donor_ok_btn :

                      if(nametxt.getText().toString().length() < 1 )
                      {
                          nametxt.setError("Not a valid name");
                      }
                      else if (!isValidEmail(emailtxt.getText().toString()))
                      {
                          emailtxt.setError("Not a valid e-mail");
                      }
                      else if (radioGroup.getCheckedRadioButtonId()==-1)
                      {
                           textView1.setError("Select one please");
                      }
                      else if (radioGroup2.getCheckedRadioButtonId()==-1)
                      {
                          textView2.setError("Select one please");
                      }
                      else {
                          sendData();
                          /*Ok.setEnabled(false);*/
                          }
              }
       }
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null) {
            uri = data.getData();
            imageView.setImageURI(uri);
        }

    }

    public void sendData(){

           String name = nametxt.getText().toString();
           String email = emailtxt.getText().toString();
           String userid = mAuth.getCurrentUser().getUid();
           boolean checked ;
           int id = radioGroup.getCheckedRadioButtonId();
           radiobtn = getActivity().findViewById(id);


           int id2 = radioGroup2.getCheckedRadioButtonId();
           radiobtn2 = getActivity().findViewById(id2);


           if (radiobtn3.isChecked()){
                checked = true;
           }
           else {
                checked = false;
           }
           String gender = radiobtn.getText().toString();
           String bloodGroup = radiobtn2.getText().toString();


           // Sending Image to FireBase Database

           imageView.setDrawingCacheEnabled(true);
           imageView.buildDrawingCache();
           Bitmap bitmap = imageView.getDrawingCache();
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
           imageView.setDrawingCacheEnabled(false);
           byte [] data = baos.toByteArray();


               final String path = "UserImage/" + userId + ".png";

               final StorageReference storageReference = firebaseStorage.getReference(path);
               final UploadTask uploadTask = storageReference.putBytes(data);

               uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Log.d("task done", path);

                       url = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                       url.addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {

                               photoLink = uri.toString();
                               Log.d("photoLink :", photoLink);


                               Map<String, String> newmap = new HashMap<>();
                               newmap.put("picturelink", photoLink);
                               db.collection("user").document(documentId).set(newmap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
                                       System.out.println(documentId);
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       System.out.println("not added");
                                   }
                               });

                               profilePage pp = new profilePage();
                               Bundle picandDocBundle = new Bundle();

                               picandDocBundle.putString("picLink", photoLink);
                               picandDocBundle.putString("Doclink", documentId);

                               pp.setArguments(picandDocBundle);
                               getFragmentManager().beginTransaction().replace(R.id.frame, pp).commit();

                           }
                       });

                   }
               });



           // ending





           map.put("name",name);
           map.put("email",email);
           map.put("gender",gender);
           map.put("group",bloodGroup);
           map.put("bool",checked);
           map.put("picturelink",url);
           map.put("muserid",userid);






           db.collection("user").document(userId).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   Log.d("document ID",userId);
                   documentId = userId;
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Log.w(TAG, "Error adding document", e);
               }
           });
       }




    }
