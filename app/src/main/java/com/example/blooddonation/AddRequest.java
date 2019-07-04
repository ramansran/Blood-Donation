package com.example.blooddonation;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRequest extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    FirebaseFirestore db;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    Spinner spinner1,spinner2;
    List<String> spinnerList1,spinnerrList2;
    String bloodFor,neededCity;
    RadioGroup blood_group_btns;
    RadioButton radioButton;
    int id;
    String group_needed,LID;
    CircularImageView circularImageView;
    Button submit;





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_request, container, false);

        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.grey));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        Bundle LastB = getArguments();
        LID = LastB.getString("DII");

        submit = getActivity().findViewById(R.id.Submit_btn);
        submit.setOnClickListener(this);

        blood_group_btns = getActivity().findViewById(R.id.blood_group_buttons_of_add_request);

        spinner1   = getActivity().findViewById(R.id.for_spinner);
        spinner2   = getActivity().findViewById(R.id.City_spinner);

        spinnerList1 = new ArrayList<String>();
        spinnerList1.add("Freind");
        spinnerList1.add("Father");
        spinnerList1.add("Mother");
        spinnerList1.add("Brother");
        spinnerList1.add("Sister");
        spinnerList1.add("Relative");

        ArrayAdapter<String> firstSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerList1);
        firstSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(firstSpinnerAdapter);
        spinner1.setOnItemSelectedListener(this);



        spinnerrList2 = new ArrayList<String>();
        spinnerrList2.add("Delhi");
        spinnerrList2.add("Mumbai");
        spinnerrList2.add("Dehradoon");
        spinnerrList2.add("Chandigarh");
        spinnerrList2.add("Mohali");
        spinnerrList2.add("Patna");
        spinnerrList2.add("Pune");
        spinnerrList2.add("Shimla");
        spinnerrList2.add("Manali");
        spinnerrList2.add("Solan");


        ArrayAdapter<String> secondSpinnerAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinnerrList2);
        secondSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(secondSpinnerAdapter);
        spinner2.setOnItemSelectedListener(this);

        circularImageView = getActivity().findViewById(R.id.img_profile_add_request);
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePage pp = new profilePage();
                getFragmentManager().beginTransaction().replace(R.id.frame,pp).commit();
            }
        });





    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.for_spinner){
            bloodFor  = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), "Selected: " + bloodFor, Toast.LENGTH_LONG).show();
        }


        else if (parent.getId() == R.id.City_spinner){
            neededCity = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), "Selected: " + neededCity, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Submit_btn:
                  sendRequestDat();

        }
    }


    public  void  sendRequestDat(){

        id = blood_group_btns.getCheckedRadioButtonId();
        radioButton = getActivity().findViewById(id);
        String group = radioButton.getText().toString();


        Map<String,Object> secondMap = new HashMap<>();
        secondMap.put("for",bloodFor);
        secondMap.put("where",neededCity);
        secondMap.put("neededgroup",group);

        db.collection("user").document(LID).set(secondMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Request Added", Toast.LENGTH_LONG).show();
                profilePage PP = new profilePage();
                getFragmentManager().beginTransaction().replace(R.id.frame,PP).commit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "data merged Unsuccesfully", Toast.LENGTH_LONG).show();
            }
        });

    }



}






