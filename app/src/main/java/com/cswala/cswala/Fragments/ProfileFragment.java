package com.cswala.cswala.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cswala.cswala.Activities.LoginActivity;
import com.cswala.cswala.Activities.EditProfile;
import com.cswala.cswala.Activities.PersonalInformation;
import com.cswala.cswala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

public class ProfileFragment extends Fragment {


    String userid = FirebaseAuth.getInstance().getUid();
    private ImageView imageView;
    private TextView username;

    ProgressBar mProgressBar;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    private ExpandableLayout expandableLayout0;
    private ExpandableLayout expandableLayout1;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(view.VISIBLE);
        /*final FragmentActivity fragmentActivity = (FragmentActivity) getContext();
        fragmentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/


        final Button tv = view.findViewById(R.id.btnLogout);
        Button info = view.findViewById(R.id.btnPersonalInfo);
        Button edit = view.findViewById(R.id.btneditprofile);
        imageView = view.findViewById(R.id.circleImageView);
        username = view.findViewById(R.id.txtUsername);


        final String[] user_val = new String[6];
        final Uri[] imageLink = new Uri[1];


        final DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("User").child(userid).exists())
                {
                    user_val[0] = dataSnapshot.child("User").child(userid).child("name").getValue().toString();
                    user_val[1] = dataSnapshot.child("User").child(userid).child("email").getValue().toString();
                    user_val[2] = dataSnapshot.child("User").child(userid).child("phone").getValue().toString();
                    user_val[3] = dataSnapshot.child("User").child(userid).child("gender").getValue().toString();
                    user_val[4] = dataSnapshot.child("User").child(userid).child("dob").getValue().toString();
                }
                else
                {
                    mProgressBar.setVisibility(View.GONE);
//                    fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }


                    StorageReference storageRef;
                    storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference sr = storageRef.child(userid);
                    sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(@NonNull Uri uri) {
                            Picasso.get().load(uri).into(imageView);
                            imageLink[0] = uri;

                            mProgressBar.setVisibility(view.GONE);



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed to get profile image", Toast.LENGTH_SHORT).show();
                        }
                    });



                if (TextUtils.isEmpty(user_val[0]))
                    user_val[0]="Name";
                else
                    username.setText(user_val[0]);

                if (TextUtils.isEmpty(user_val[1]))
                    user_val[1]="Email";
                if (TextUtils.isEmpty(user_val[2]))
                    user_val[2]="0";
                if (TextUtils.isEmpty(user_val[3]))
                    user_val[3]="Male";
                if (TextUtils.isEmpty(user_val[4]))
                    user_val[4]="0";





            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        expandableLayout0 = view.findViewById(R.id.expandable_layout_0);
        expandableLayout1 = view.findViewById(R.id.expandable_layout_01);
        expandableLayout0.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout0", "State: " + state);
            }
        });

        expandableLayout1.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout1", "State: " + state);
            }
        });

        view.findViewById(R.id.expand_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expandableLayout0.isExpanded()) {
                    expandableLayout0.collapse();
                } else {
                    expandableLayout0.expand();
                }
            }
        });
        view.findViewById(R.id.expand_button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expandableLayout1.isExpanded()) {
                    expandableLayout1.collapse();
                } else {
                    expandableLayout1.expand();
                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mProgressBar.getVisibility()==View.GONE) {
                    Intent i = new Intent(getContext(), PersonalInformation.class);
                    i.putExtra("name", user_val[0]);
                    i.putExtra("email", user_val[1]);
                    i.putExtra("phone", user_val[2]);
                    i.putExtra("gender", user_val[3]);
                    i.putExtra("dob", user_val[4]);
                    String str = "none";
                    if (imageLink[0] != null) {
                        str = imageLink[0].toString();
                    }
                    i.putExtra("image", str);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getContext(), "Preparing...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mProgressBar.getVisibility()==View.GONE) {
                    Intent i = new Intent(getActivity(), EditProfile.class);
                    i.putExtra("name", user_val[0]);
                    i.putExtra("email", user_val[1]);
                    i.putExtra("phone", user_val[2]);
                    i.putExtra("gender", user_val[3]);
                    i.putExtra("dob", user_val[4]);
                    String str = "none";
                    if (imageLink[0] != null) {
                        str = imageLink[0].toString();
                    }
                    i.putExtra("image", str);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getContext(), "Preparing...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(getContext(), "back pressed", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                FragmentActivity fragmentActivity = (FragmentActivity) getContext();
                fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

}
