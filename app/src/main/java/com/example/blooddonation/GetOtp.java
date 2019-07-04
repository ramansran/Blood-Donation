package com.example.blooddonation;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Observable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static android.support.constraint.Constraints.TAG;

public class GetOtp extends Fragment implements View.OnClickListener {
    Button vrify_btn;
    EditText tone;
    String CodeSent;
    FirebaseAuth mAuth;
    String phoneNumber;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_get_otp, container, false);

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vrify_btn = getActivity().findViewById(R.id.verify_btn);
        vrify_btn.setOnClickListener(this);
        tone = getActivity().findViewById(R.id.one);
        progressBar = getActivity().findViewById(R.id.pbar);
        mAuth = FirebaseAuth.getInstance();
        SignIn();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_btn:
            verifyotp();

        }
    }

    public void SignIn() {

        phoneNumber = getArguments().getString("phone");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, getActivity(), mCallbacks);


    }

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
            CodeSent = verificationId;
        }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                progressBar.setVisibility(View.VISIBLE);
                Log.d("code", String.valueOf(phoneAuthCredential));

             if (phoneAuthCredential!=null)
             {
                 signInWithPhoneAuthCredential(phoneAuthCredential);
             }

            }


            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException)
                {
                    tone.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException)
                {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        };













    private void verifyotp(){
        try {
            String code =tone.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeSent,code);
            signInWithPhoneAuthCredential(credential);
        }

        catch (Exception e){
            Toast toast = Toast.makeText(getActivity(), "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs2", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("ramans2",true);
                            editor.commit();

                            become_donor_frag pp = new become_donor_frag();
                            getFragmentManager().beginTransaction().replace(R.id.frame,pp).commit();

                        }
                        else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getActivity(),"unsuccesfull",Toast.LENGTH_LONG).show();
                            }
                            }
                        }

                });
    }

    }




