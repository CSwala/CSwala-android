package com.cswala.cswala;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ExploreTag";
    ChipNavigationBar bt;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = findViewById(R.id.bottom_navigation);
        bt.setItemSelected(R.id.explore, true);
        showFragment(ExploreFragment.class);

        bt.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                launchRequiredFragmentById(i);
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

    private void launchRequiredFragmentById(int id) {
        switch (id) {
            case R.id.explore:
                // start ExploreFragment
                Log.d(TAG, "onItemSelected: explore clicked!");
                showFragment(ExploreFragment.class);
                break;
            case R.id.account:
                // start account fragment
                break;
            case R.id.saved:
                // start jobhunt fragment
                break;
            case R.id.inbox:
                // start community fragment
                break;
            case R.id.news:
                // start hackfeed fragment
                break;
        }
    }

    /**
     * Close previous Fragment and show another Fragment
     *
     * @param fragmentClass extending Fragment class that we need to show
     */
    private <T extends Fragment> void showFragment(Class<T> fragmentClass) {
        Log.d(TAG, "startMenuFragment");
        getSupportFragmentManager().popBackStack(); // remove previous fragment from back stack
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container, fragmentClass, null)
                .addToBackStack(null)
                .commit();

    }

}