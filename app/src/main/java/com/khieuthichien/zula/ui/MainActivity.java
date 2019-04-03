package com.khieuthichien.zula.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.khieuthichien.zula.R;
import com.khieuthichien.zula.fragment.MessagesFragment;
import com.khieuthichien.zula.fragment.NewsFeedFragment;
import com.khieuthichien.zula.fragment.NotificationsFragment;
import com.khieuthichien.zula.fragment.RequestsFragment;
import com.khieuthichien.zula.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = getSupportActionBar();
        //toolbar.setTitle("News Feed");
        loadFragment(new NewsFeedFragment());
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.itemNewsFeed:
                    fragment = new NewsFeedFragment();
                    loadFragment(fragment);
                    //toolbar.setTitle("News Feed");
                    return true;
                case R.id.itemRequests:
                    fragment = new RequestsFragment();
                    loadFragment(fragment);
                    //toolbar.setTitle("Requests");
                    return true;
                case R.id.itemMessages:
                    fragment = new MessagesFragment();
                    loadFragment(fragment);
                    //toolbar.setTitle("Messages");
                    return true;
                case R.id.itemNotifications:
                    fragment = new NotificationsFragment();
                    loadFragment(fragment);
                    //toolbar.setTitle("Notifications");
                    return true;
                case R.id.itemUser:
                    fragment = new UserFragment();
                    loadFragment(fragment);
                    //toolbar.setTitle("User");
                    return true;
            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment){
        //loadFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
