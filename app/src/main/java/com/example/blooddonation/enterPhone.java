package com.example.blooddonation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;


public class enterPhone extends Fragment implements View.OnClickListener {
Button get_otp;
EditText enter_phone_field;
FirebaseAuth mAuth;
String CodeSent;
String phoneNumber;
String countryCodeAndroid = "null";
CountryCodePicker ccp;




    public enterPhone() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter_phone, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        get_otp = getActivity().findViewById(R.id.get_otp_btn);
        get_otp.setOnClickListener(this);

        enter_phone_field = getActivity().findViewById(R.id.number_edit_text);

        mAuth = FirebaseAuth.getInstance();
        ccp = getActivity().findViewById(R.id.ccp);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeAndroid = ccp.getSelectedCountryCodeWithPlus();
                Log.d("Country Code", countryCodeAndroid);
            }
        });
    }

    @Override
    public void onClick(View v) {
               switch (v.getId()){
                   case R.id.get_otp_btn :
                       phoneNumber = ( countryCodeAndroid+enter_phone_field.getText().toString());
                       verifyphone(phoneNumber);
                       GetOtp getOtp = new GetOtp();
                       Bundle args = new Bundle();
                       args.putString("phone",phoneNumber);
                       getOtp.setArguments(args);
                       getFragmentManager().beginTransaction().replace(R.id.frame,getOtp).commit();


               }
    }



    public void verifyphone(String phoneNumber){


               if (phoneNumber.isEmpty()){
                    enter_phone_field.setError("please enter phone number");
                    enter_phone_field.requestFocus();
                    return;
                }

                if (phoneNumber.length()>12){
                    enter_phone_field.setError("enter valid phone number");
                    enter_phone_field.requestFocus();
                    return;
                }
    }

}
