package io.mse.doggodate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class ProfileActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent toHome = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(toHome);
                    break;
                case R.id.navigation_map:
                    Intent toMap = new Intent(ProfileActivity.this, MapActivity.class);
                    startActivity(toMap);
                    break;
                case R.id.navigation_doggos:
                    Intent toSearch = new Intent(ProfileActivity.this, SearchActivity.class);
                    startActivity(toSearch);
                    break;
                case R.id.navigation_profile:

                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_profile);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
}
