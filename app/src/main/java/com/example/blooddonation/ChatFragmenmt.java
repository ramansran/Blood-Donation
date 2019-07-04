package com.example.blooddonation;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class ChatFragmenmt extends Fragment {
    FirebaseFirestore db ;
    FirebaseStorage firebaseStorage;
    FirebaseAuth mAuth;
    DocumentReference documentReference;
    String USER_ID,UserName,CurrentUserID;
    Bitmap UserBitmap;
    TextView textView;
    TextView headingName;
    CircularImageView circularImageView;
    ImageView imageButton,imageButton2;
    EditText editText;
    List<ChatMainData> list;
    ChatAdapter chatAdapter;
    RecyclerView recyclerView;
    Map<String,Object> chatMap;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_chat_fragmenmt, container, false);


        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.profile));

      return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle!=null){
            USER_ID = bundle.getString("IDOFUSER");
        }

        recyclerView = getActivity().findViewById(R.id.chatList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        editText = getActivity().findViewById(R.id.keyboard_edit);
        imageButton = getActivity().findViewById(R.id.sendButton);
        imageButton2 = getActivity().findViewById(R.id.back);
        textView = getActivity().findViewById(R.id.UName);
        circularImageView = getActivity().findViewById(R.id.img_profile_chat);
        db= FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        CurrentUserID = mAuth.getCurrentUser().getUid();
        chatMap = new HashMap<>();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                if (!msg.equals("")){
                    sendMessagae(CurrentUserID,USER_ID,msg);
                }
                else {
                    Toast.makeText(getActivity(),"Can't Send Empty Message",Toast.LENGTH_LONG).show();
                }
                editText.setText("");
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePage pp = new profilePage();
                getFragmentManager().beginTransaction().replace(R.id.frame,pp).commit();
            }
        });

        documentReference = db.collection("user").document(USER_ID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        UserName = documentSnapshot.getString("name");
                        textView.setText(UserName);
                        String Piclink = documentSnapshot.getString("picturelink");
                        URL url = null;
                        try {
                            url = new URL(Piclink);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        try {
                            UserBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                         circularImageView.setImageBitmap(UserBitmap);
                        readMessage(CurrentUserID,USER_ID);

                    }

                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
            }
        });

    }



      public  void sendMessagae(String sender , String reciever ,String  message) {
          chatMap.put("sender", sender);
          chatMap.put("reciever", reciever);
          chatMap.put("message", message);
          chatMap.put("latestupdatetimestamp", FieldValue.serverTimestamp());

          db.collection("Chats").add(chatMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
              @Override
              public void onSuccess(DocumentReference documentReference) {

              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {

              }
          });
      }

      public void readMessage(final String My_id, final String User_id){

        list = new ArrayList<>();

        db.collection("Chats").orderBy("latestupdatetimestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot value, @javax.annotation.Nullable FirebaseFirestoreException e) {
                list.clear();
                for (QueryDocumentSnapshot doc : value){
                   ChatMainData chatMainData = doc.toObject(ChatMainData.class);
                   if (chatMainData.getReciever().equals(My_id) &&chatMainData.getSender().equals(User_id) ||
                  chatMainData.getReciever().equals(User_id) && chatMainData.getSender().equals(My_id))
                   {
                       list.add(chatMainData);
                   }
                   else {

                   }
                   ChatAdapter chatAdapter = new ChatAdapter(getActivity(),list);
                   recyclerView.setAdapter(chatAdapter);

               }
            }
        });

        }


}
