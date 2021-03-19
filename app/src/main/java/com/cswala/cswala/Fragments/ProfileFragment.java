package com.cswala.cswala.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.cswala.cswala.Activities.LoginActivity;
import com.cswala.cswala.Activities.Splashscreen;
import com.cswala.cswala.MainActivity;
import com.cswala.cswala.R;
import com.cswala.cswala.utils.IntentHelper;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    //Defining the variables
    CircleImageView profilePic;
    TextView username ;
    Button personalInfo;
    Button termsOfService;
    Button contactUs;
    Button logOut;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        // Hooking them with xml attributes
        username=view.findViewById(R.id.txtUsername);
        profilePic=view.findViewById(R.id.circleImageView);
        personalInfo=view.findViewById(R.id.btnPersonalInfo);
        termsOfService=view.findViewById(R.id.btnTerms);
        contactUs=view.findViewById(R.id.btnContact);
        logOut=view.findViewById(R.id.btnLogout);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));

            }
        });
        return  view;
    }
}