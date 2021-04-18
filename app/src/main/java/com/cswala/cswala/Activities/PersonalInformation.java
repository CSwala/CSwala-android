package com.cswala.cswala.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cswala.cswala.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInformation extends AppCompatActivity {
    CircleImageView profilePic;
    TextView nameView,emailView,phoneView,dobView,genderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        Intent data=getIntent();
        String name=data.getStringExtra("name");
        String email=data.getStringExtra("email");
        String phone=data.getStringExtra("phone");
        String image=data.getStringExtra("image");
        String dob=data.getStringExtra("dob");
        String gender=data.getStringExtra("gender");
        setView();
        setData(image,name,email,phone,gender,dob);
    }

    private void setView() {
        profilePic=(CircleImageView) findViewById(R.id.profile_picture);
        nameView=(TextView) findViewById(R.id.name_info);
        emailView=(TextView) findViewById(R.id.email_info);
        phoneView=(TextView) findViewById(R.id.phone_num_info);
        dobView=(TextView) findViewById(R.id.dob_info);
        genderView=(TextView) findViewById(R.id.gender_info);

    }

    private void setData(String image, String name, String email, String phone, String gender, String dob) {
        Glide.with(this).load(image).centerCrop().placeholder(R.drawable.avatar).into(profilePic);
        nameView.setText(name);
        emailView.setText(email);
        phoneView.setText(phone);
        genderView.setText(gender);
        dobView.setText(dob);
    }
}