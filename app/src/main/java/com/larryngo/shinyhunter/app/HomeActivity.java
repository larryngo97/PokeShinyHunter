package com.larryngo.shinyhunter.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.larryngo.shinyhunter.HomeCompletedFragment;
import com.larryngo.shinyhunter.HomeHuntingFragment;
import com.larryngo.shinyhunter.HomeStatisticsFragment;
import com.larryngo.shinyhunter.R;
import com.larryngo.shinyhunter.util.Settings;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class HomeActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final static int REQUEST_CODE_RESTART = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean nightMode = Settings.isDarkModeOn();
        if(nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.HomeThemeDark);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.HomeTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.home_tabLayout);
        ViewPager viewPager = findViewById(R.id.home_viewPager);

        HomeHuntingFragment homeHuntingFragment = new HomeHuntingFragment();
        HomeCompletedFragment homeCompletedFragment = new HomeCompletedFragment();
        HomeStatisticsFragment homeStatisticsFragment = new HomeStatisticsFragment();

        tabLayout.setupWithViewPager(viewPager);

        SectionsPageAdapter viewPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(homeHuntingFragment, getResources().getString(R.string.home_hunting_title));
        viewPagerAdapter.addFragment(homeCompletedFragment, getResources().getString(R.string.home_completed_title));
        viewPagerAdapter.addFragment(homeStatisticsFragment, getResources().getString(R.string.home_statistics_title));
        viewPager.setAdapter(viewPagerAdapter);

        if(tabLayout.getTabAt(0) != null) {
            tabLayout.getTabAt(0).setIcon(R.drawable.icon_hunting);
        }
        if(tabLayout.getTabAt(1) != null) {
            tabLayout.getTabAt(1).setIcon(R.drawable.icon_completed);
        }
        if(tabLayout.getTabAt(2) != null) {
            tabLayout.getTabAt(2).setIcon(R.drawable.icon_statistics);
        }


    }

    public static class SectionsPageAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitle = new ArrayList<>();
        public SectionsPageAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment (Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = getMenuInflater();
        //menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home_menu_settings) {
            Intent intent = new Intent(HomeActivity.this, Settings.class);
            startActivityForResult(intent, REQUEST_CODE_RESTART); //home should be reset every time user exits settings

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESTART) { //restart code
            recreate(); //restart
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        App.getTinyDB().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.getTinyDB().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
