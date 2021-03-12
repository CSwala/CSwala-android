package com.cswala.cswala;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    ChipNavigationBar bt;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.bottom_navigation);
        bt.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;

                switch(i) {

                    case R.id.explore:
                        //explore Fragment
                        break;

                    case R.id.saved:
                        //job hunt fragment
                        break;

                    case R.id.news:
                        //hack feed Fragment
                        break;

                    case R.id.inbox:
                        //community fragment
                        break;

                    case R.id.account:
                        fragment = new ProfileFragment();
                        break;
                }
                if(fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                }
            }
        });

        TextView tv = findViewById(R.id.logout);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });
    }
}