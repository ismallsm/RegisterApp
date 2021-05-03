package com.ismaelsouza.registerapp;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.ismaelsouza.registerapp.fragment.ListFragment;
import com.ismaelsouza.registerapp.fragment.RegisterBookFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(new ListFragment());
                    return true;
                case R.id.logout:
                    deslogar();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference("Livros");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        if(getIntent()!= null && getIntent().getStringExtra("fragment") != null) {

            String toFragment = getIntent().getStringExtra("fragment");
            if(toFragment.equalsIgnoreCase("registerBook")){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);

                changeFragment(new RegisterBookFragment());
            }else if(toFragment.equalsIgnoreCase("listBooks")){
                changeFragment(new ListFragment());
            }
        }

    }

    public void deslogar() {
        auth.signOut();
    }

    private void changeFragment(Fragment fragment){
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(new ListFragment());
        ft.remove(new RegisterBookFragment());
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public boolean onSupportNavigateUp() {

        if(getIntent()!= null && getIntent().getStringExtra("fragment") != null) {
            String toFragment = getIntent().getStringExtra("fragment");
            if(toFragment.equalsIgnoreCase("registerBook")){
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                changeFragment(new ListFragment());
            }
        }


        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
