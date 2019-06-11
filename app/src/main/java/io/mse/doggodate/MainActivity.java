package io.mse.doggodate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.ArrayList;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoPOJO;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.home.HomeFragment;
import io.mse.doggodate.map.MapFragment;
import io.mse.doggodate.profile.OtherProfileFragment;
import io.mse.doggodate.profile.ProfileViewModel;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ProfileViewModel profileViewModel;

    Doggo activeDog;
    Doggo selectedDog;

    ArrayList<Doggo> defaultSearchDoggos = new ArrayList<>();

    private DoggoZone park1;
    private DoggoZone park2;
    private DoggoZone park3;
    private DoggoZone park4;
    private DoggoZone park5;
    private DoggoZone park6;

    private DoggoZone selectedDoggoZone;

    private MenuItem searchItem;

    private MenuItem favoritesItem;

    private BottomNavigationView navView;

    private ArrayList<DoggoEvent> activeDoggoEvents = new ArrayList<>();

    final Fragment homeFragment = new HomeFragment();
    final Fragment fragment2 = new MapFragment();
    NavController navController;

    //final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;

    public Fragment getActive() {
        return active;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.i(TAG,"Active " + active);
                    if(!active.equals(homeFragment)) {
                        Log.i(TAG, "NOT DOuble");
                        //fm.beginTransaction().replace(R.id.main_container, homeFragment, "1").commit();
                        //fm.beginTransaction().hide(active).show(homeFragment).addToBackStack("1").commit();
                    }
                    item.setChecked(true);
                    ActionBar ab = getSupportActionBar();
                    ab.show();
                    ab.setHomeButtonEnabled(false);
                    ab.setDisplayHomeAsUpEnabled(false);
                    ab.setTitle("DoggoDate");
                    searchItem.setVisible(false);
                    favoritesItem.setVisible(false);
                    navController.navigate(R.id.navigation_home);
                    Log.i(TAG, "homescreen opened");
                    break;
                case R.id.navigation_map:
                    item.setChecked(true);
                    if(active!=fragment2) {
                        Log.i(TAG, "NOT DOuble");


                        //fm.beginTransaction().add(R.id.main_container, fragment2, "2").commit();

                        //fm.beginTransaction().hide(active).show(fragment2).addToBackStack("2").commit();
                    }
                    setActive(fragment2,active);
                    getSupportActionBar().setTitle("DoggoZones");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    searchItem.setVisible(true);
                    favoritesItem.setVisible(true);
                    Log.i(TAG, "navigation opened");
                    navController.navigate(R.id.navigation_map);
                    break;
                case R.id.navigation_doggos:
                    item.setChecked(true);
                    getSupportActionBar().setTitle("Doggos");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    searchItem.setVisible(true);
                    favoritesItem.setVisible(false);
                    navController.navigate(R.id.navigation_doggos);
                    Log.i(TAG, "doggos opened");
                    break;
                case R.id.navigation_profile:
                    Log.i(TAG, "profile opened");
                    item.setChecked(true);
                    getSupportActionBar().setTitle("My Profile");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    searchItem.setVisible(false);
                    favoritesItem.setVisible(false);
                    navController.navigate(R.id.navigation_profile);
                    break;
            }
            return false;
        }
    };

    public ArrayList<Doggo> getDefaultSearch() {
        return defaultSearchDoggos;
    }

    public void setDefaultSearch(ArrayList<Doggo> defaultSearch) {
        this.defaultSearchDoggos = defaultSearch;
    }

    public void setActive(Fragment fragment, Fragment old) {
        Log.i(TAG, "Setting the active fragment other profile from search fragment");
        //fm.beginTransaction().hide(old).show(fragment).commit();
        active = fragment;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        // Create the observer which updates the UI.


        navController = Navigation.findNavController(this,R.id.main_container);
        NavigationUI.setupWithNavController(navView,navController);

        active = homeFragment;
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Doggo Bonnie = new Doggo("Bonnie", "Golden retriever", R.drawable.profile_image);
        Doggo Alex = new Doggo("Alex", "Labrador",  R.drawable.labrador_profile);
        Doggo Chichi = new Doggo("Chichi", "Chivava", R.drawable.chivava_prof);
        Doggo Rex = new Doggo("Rex", "Wolfdog", R.drawable.wolfdog_profile);
        Doggo Akki = new Doggo("Akki", "Akita Inu", R.drawable.akita_profile);
        Doggo Alfonz = new Doggo("Alfonz", "Buldog", R.drawable.dog4);
        Doggo Nina = new Doggo("Nina", "Labrador", R.drawable.dog3);
        Doggo Bowie = new Doggo("Bowie", "Retriever", R.drawable.dog2);
        Doggo Makawa = new Doggo("Makawa", "Chivuavua", R.drawable.chivava_1);

        /* SETTING ACTIVE DOG ATTRIBUTES*/
        ArrayList<Doggo> followers = new ArrayList<>();
        followers.add(Alex);
        followers.add(Chichi);
        followers.add(Alfonz);
        followers.add(Nina);
        followers.add(Bowie);
        followers.add(Makawa);
        Bonnie.setFollowers(followers);

        ArrayList<Doggo> followings = new ArrayList<>();
        followings.add(Rex);
        //followings.add(Akki);
        //followers.add(Bowie);
        Bonnie.setFollowings(followings);

        ArrayList<Integer> photos = new ArrayList<>();
        photos.add(R.drawable.golden2);
        photos.add(R.drawable.golden3);
        photos.add(R.drawable.golden4);
        photos.add(R.drawable.golden5);

        Bonnie.setPhotos(photos);
        Bonnie.setProfilePic(R.drawable.profile_image);
        activeDog = Bonnie;

        /**-------ALEX---------*/
        ArrayList<Integer> alexPhotos = new ArrayList<>();
        alexPhotos.add(R.drawable.labrador_1);
        alexPhotos.add(R.drawable.labrador_2);
        alexPhotos.add(R.drawable.labrador_3);
        alexPhotos.add(R.drawable.labrador_profile);
        Alex.setPhotos(alexPhotos);
        ArrayList<Doggo> followingsA = new ArrayList<>();
        followingsA.add(Rex);
        ArrayList<Doggo> followersA = new ArrayList<>();
        followersA.add(Chichi);
        followersA.add(Alfonz);
        followersA.add(Nina);
        followersA.add(Bowie);
        followersA.add(Makawa);
        Alex.setFollowers(followersA);
        Alex.setFollowings(followingsA);
        /**-------CHICHI---------*/
        ArrayList<Integer> ChiChiPhotos = new ArrayList<>();
        ChiChiPhotos.add(R.drawable.chivava_1);
        ChiChiPhotos.add(R.drawable.chivava_2);
        ChiChiPhotos.add(R.drawable.chivava3);
        Chichi.setPhotos(ChiChiPhotos);
        ArrayList<Doggo> followingsC = new ArrayList<>();
        followingsC.add(Rex);
        ArrayList<Doggo> followersC = new ArrayList<>();
        followersC.add(Chichi);
        followersC.add(Alfonz);
        followersC.add(Nina);
        followersC.add(Bowie);
        followersC.add(Makawa);
        Chichi.setFollowers(followersC);
        Chichi.setFollowings(followingsC);
        /**-------REX---------*/
        ArrayList<Integer> rexPhotos = new ArrayList<>();
        rexPhotos.add(R.drawable.wd_2);
        rexPhotos.add(R.drawable.wd_1);
        rexPhotos.add(R.drawable.wd_3);
        rexPhotos.add(R.drawable.wd_4);
        Rex.setPhotos(rexPhotos);
        ArrayList<Doggo> followingsR = new ArrayList<>();
        followingsR.add(Alfonz);
        ArrayList<Doggo> followersR = new ArrayList<>();
        followersR.add(Alex);
        followersR.add(Chichi);
        followersR.add(Alfonz);
        followersR.add(Nina);
        followersR.add(Bowie);
        followersR.add(Makawa);
        Rex.setFollowers(followersR);
        Rex.setFollowings(followingsR);

        /**-------AKKI---------*/
        ArrayList<Integer> akkiPhotos = new ArrayList<>();
        akkiPhotos.add(R.drawable.akita_1);
        akkiPhotos.add(R.drawable.akita_2);
        akkiPhotos.add(R.drawable.akita_3);
        akkiPhotos.add(R.drawable.akita_4);
        akkiPhotos.add(R.drawable.akita_profile);
        Akki.setPhotos(akkiPhotos);
        Akki.setFollowers(followers);
        Akki.setFollowings(followers);
        ArrayList<Doggo> followingsAk = new ArrayList<>();
        followingsAk.add(Alfonz);
        followingsAk.add(Nina);
        followingsAk.add(Makawa);
        followingsAk.add(Bowie);
        ArrayList<Doggo> followersAk = new ArrayList<>();
        followersAk.add(Alex);
        followersAk.add(Chichi);
        followersAk.add(Alfonz);
        followersAk.add(Nina);
        followersAk.add(Bowie);
        followersAk.add(Makawa);
        Rex.setFollowers(followersAk);
        Rex.setFollowings(followingsAk);


        /** Creating Doggo Zones*/
        park1 = new DoggoZone(48.239539376028745, 16.333220189004898,
                "Hugo-Wolf-Park" ,8169, false);
        park2 = new DoggoZone(48.22027912193935, 16.480529082068596,
                "GA Aspernstraße",323, true);
        park3 = new DoggoZone(48.19845005423561, 16.330519111538322,
                "Reithofferpark" ,671, true);
        park4 = new DoggoZone(48.212801596627564, 16.339838642282885,
                "Hernalser Gürtel",672, true);
        park5 = new DoggoZone(48.16948327975529, 16.428887264076906,
                "PA Blériotgasse" ,1850, true);
        park6 = new DoggoZone(48.27240496690655, 16.447774667101427,
                "PA Gitlbauergasse",1983, true);
        DoggoEvent e1 = new DoggoEvent(LocalDateTime.of(2019, 6, 15, 14,30),park3, Nina);
        DoggoEvent e2 = new DoggoEvent(LocalDateTime.of(2019, 5, 17, 10,30),park2, Chichi);
        DoggoEvent e3 = new DoggoEvent(LocalDateTime.of(2019, 5, 20, 11,30),park1, Bonnie);
        DoggoEvent e4 = new DoggoEvent(LocalDateTime.of(2019, 5, 19, 10,50),park4, Alex);
        DoggoEvent e5 = new DoggoEvent(LocalDateTime.of(2019, 5, 21, 19,30),park5, Alfonz);
        DoggoEvent e6 = new DoggoEvent(LocalDateTime.of(2019, 5, 23, 9,35),park6, Rex);
        DoggoEvent e7 = new DoggoEvent(LocalDateTime.of(2019, 5, 24, 10,20),park4, Bowie);

        activeDoggoEvents.add(e1);
        activeDoggoEvents.add(e2);
        activeDoggoEvents.add(e3);
        activeDoggoEvents.add(e4);
        activeDoggoEvents.add(e5);
        activeDoggoEvents.add(e6);
        activeDoggoEvents.add(e7);

        /** SETTING ALL DOGS LIST**/
        //defaultSearchDoggos.add(Bonnie);
        defaultSearchDoggos.add(Alfonz);
        defaultSearchDoggos.add(Nina);
        defaultSearchDoggos.add(Makawa);
        defaultSearchDoggos.add(Bowie);
        defaultSearchDoggos.add(Akki);
        defaultSearchDoggos.add(Rex);
        defaultSearchDoggos.add(Chichi);
        defaultSearchDoggos.add(Alex);


    }

    public Doggo getActiveDog() {
        return activeDog;
    }

    protected OnBackPressedListener onBackPressedListener;

    public DoggoZone getPark2() {
        return park2;
    }

    public DoggoZone getPark1() {
        return park1;
    }

    public void setSelectedDoggoZone(DoggoZone selectedDoggoZone) {
        this.selectedDoggoZone = selectedDoggoZone;
    }

    public void addToFavorites(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("You can add DoggoZones to your favorites for easy search");
        alertDialogBuilder.setTitle("Wanna add this DoggoZone to Favorites? ");

        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this, "Added to Favorites", Toast.LENGTH_LONG).show();
                ((MapFragment)fragment2).setFavorites();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this, "Add to Favorites rejected!", Toast.LENGTH_LONG).show();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public DoggoZone getPark4() {
        return park4;
    }

    public DoggoZone getPark3() {
        return park3;
    }

    public DoggoZone getPark5() {
        return park5;
    }

    public DoggoZone getPark6() {
        return park6;
    }

    public interface OnBackPressedListener {
        void doBack();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG,"OnCreateMENU");
        getMenuInflater().inflate(R.menu.search_menu, menu);
        searchItem = menu.findItem(R.id.search);
        favoritesItem = menu.findItem(R.id.favorites);

        switch (navController.getCurrentDestination().getLabel().toString()){
            case "maps_fragment":
                getSupportActionBar().setTitle("DoggoZones");
                searchItem.setVisible(true);
                favoritesItem.setVisible(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case "search_fragment":
                getSupportActionBar().setTitle("Doggos");
                searchItem.setVisible(true);
                favoritesItem.setVisible(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case "profile_fragment":
                getSupportActionBar().setTitle("My Profile");
                searchItem.setVisible(false);
                favoritesItem.setVisible(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case "doggozone_fragment":
                getSupportActionBar().setTitle("DoggoZones");
                navView.getMenu().getItem(1).setChecked(true);
                searchItem.setVisible(false);
                favoritesItem.setVisible(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case "other_profile_fragment":
                getSupportActionBar().setTitle("Doggo");
                navView.getMenu().getItem(2).setChecked(true);
                searchItem.setVisible(false);
                favoritesItem.setVisible(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
                default:
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setTitle("DoggoDate");
                    searchItem.setVisible(false);
                    favoritesItem.setVisible(false);
                break;

        }

        return super.onCreateOptionsMenu(menu);
    }

    public MenuItem getSearchItem(){
        return searchItem;
    }
    public MenuItem getFavoritesItem(){
        return favoritesItem;
    }

    public BottomNavigationView getNavView(){
        return navView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toOtherProfile(int position, int type) {
        //navController.getCurrentDestination().getLabel().equals()
        //((OtherProfileFragment)otherProfileFragment).setSelectedDoggo(defaultSearchDoggos.get(position));
        selectedDog = defaultSearchDoggos.get(position);

        if (type == 0) {
            navController.navigate(R.id.from_search_toOtherProfile);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (type ==1) {
            navController.navigate(R.id.from_myProfile_to_otherProfile);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    public Doggo getSelectedDog(){
        return selectedDog;
    }
    /*public void goToDoggoZone(View view){
        navController.navigate(R.id.toDoggoZone);
        searchItem.setVisible(false);
        favoritesItem.setVisible(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(TAG,"Go to Doggo Zone " + selectedDoggoZone.getName());
        doggoZoneFragment = new DoggoZoneFragment();
        ((DoggoZoneFragment)doggoZoneFragment).setSelectedDogoZone(selectedDoggoZone);
        active=doggoZoneFragment;

    }*/


    public ArrayList<DoggoEvent> getActiveDoggoEvents() {
        return activeDoggoEvents;
    }

    public void setActiveDoggoEvents(ArrayList<DoggoEvent> activeDoggoEvents) {
        this.activeDoggoEvents = activeDoggoEvents;
    }

    public Doggo dbQueryActiveDoggo() {
      /*  FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("HOME", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("HOME", "Error adding document", e);
                    }
                });*/
      return null;
    }
}
