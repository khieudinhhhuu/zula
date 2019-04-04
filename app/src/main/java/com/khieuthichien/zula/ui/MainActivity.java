package com.khieuthichien.zula.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.khieuthichien.zula.R;
import com.khieuthichien.zula.fragment.MessagesFragment;
import com.khieuthichien.zula.fragment.NewsFeedFragment;
import com.khieuthichien.zula.fragment.NotificationsFragment;
import com.khieuthichien.zula.fragment.RequestsFragment;
import com.khieuthichien.zula.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagesAdapter viewPagesAdapter;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        toolbar = getSupportActionBar();

        viewPagesAdapter = new ViewPagesAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagesAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_subtitles_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_people_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_forum_black_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_black_24dp);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_person_black_24dp);

    }


    class ViewPagesAdapter extends FragmentPagerAdapter {

        public ViewPagesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new NewsFeedFragment();
                    break;
                    //return new NewsFeedFragment();
                case 1:
                    fragment = new RequestsFragment();
                    break;
                    //return new RequestsFragment();
                case 2:
                    fragment = new MessagesFragment();
                    break;
                    //return new MessagesFragment();
                case 3:
                    fragment = new NotificationsFragment();
                    break;
                    //return new NotificationsFragment();
                case 4:
                    fragment = new UserFragment();
                    break;
                    //return new UserFragment();
                default:
                    fragment = new NewsFeedFragment();
                    break;
                    //return new NewsFeedFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position) {
                case 0:
                    title = "";
                    break;
                    //return "News Feed";
                case 1:
                    title = "";
                    break;
                case 2:
                    title = "";
                    break;
                case 3:
                    title = "";
                    break;
                case 4:
                    title = "";
                    break;
                default:
                    title = "";
                    break;
                    //return "News Feed";
            }
            return title;
        }
    }


}
