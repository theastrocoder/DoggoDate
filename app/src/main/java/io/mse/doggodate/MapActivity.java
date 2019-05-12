package io.mse.doggodate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MapActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent toMain = new Intent(MapActivity.this,MainActivity.class);
                    startActivity(toMain);
                    return true;
                case R.id.navigation_map:
                    return true;
                case R.id.navigation_doggos:
                    Intent toSearch = new Intent(MapActivity.this,SearchActivity.class);
                    startActivity(toSearch);
                    return true;
                case R.id.navigation_profile:
                    Intent toProfile = new Intent(MapActivity.this,ProfileActivity.class);
                    startActivity(toProfile);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_map);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
}
