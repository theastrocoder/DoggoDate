package io.mse.doggodate;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.entity.DoggoPOJO;
import io.mse.doggodate.entity.DoggoZone;
import io.mse.doggodate.helpers.HelperViewModel;
import io.mse.doggodate.home.HomeFragment;
import io.mse.doggodate.map.MapFragment;
import io.mse.doggodate.profile.ProfileViewModel;
import io.mse.doggodate.search.FirestoreCallback;
import io.mse.doggodate.search.FirestoreEventCallback;
import io.mse.doggodate.search.FirestoreFollowersCallback;
import io.mse.doggodate.search.FirestoreFollowingsCallback;
import io.mse.doggodate.search.SearchViewModel;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


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
                    getSupportActionBar().show();
                    if(active!=fragment2) {
                        Log.i(TAG, "NOT DOuble");
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
                    getSupportActionBar().show();
                    searchItem.setVisible(true);
                    favoritesItem.setVisible(false);
                    navController.navigate(R.id.navigation_doggos);
                    Log.i(TAG, "doggos opened");
                    break;
                case R.id.navigation_profile:
                    Log.i(TAG, "profile opened");
                    getSupportActionBar().show();
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

    private void addDoggosToDB(){
        Log.i("adDoggosToDB", "got to the method");
        DoggoPOJO Bonnie = new DoggoPOJO("Bonnie", "Golden retriever", "profile_image");
         DoggoPOJO Alex = new DoggoPOJO("Alex", "Labrador",  "labrador_profile");
        DoggoPOJO Chichi = new DoggoPOJO("Chichi", "Chivava", "chivava_prof");
        DoggoPOJO Rex = new DoggoPOJO("Rex", "Wolfdog", "wolfdog_profile");
        DoggoPOJO Akki = new DoggoPOJO("Akki", "Akita Inu", "akita_profile");
       /* DoggoPOJO Alfonz = new DoggoPOJO("Alfonz", "Buldog", R.drawable.dog4);
        DoggoPOJO Nina = new DoggoPOJO("Nina", "Labrador", R.drawable.dog3);
        DoggoPOJO Bowie = new DoggoPOJO("Bowie", "Retriever", R.drawable.dog2);
        DoggoPOJO Makawa = new DoggoPOJO("Makawa", "Chivuavua", R.drawable.chivava_1);*/

        /* SETTING ACTIVE DOG ATTRIBUTES*/
        ArrayList<DoggoPOJO> followers = new ArrayList<>();
        followers.add(Alex);
        //followers.add(Chichi);
        // followers.add(Alfonz);
        // followers.add(Nina);
        // followers.add(Bowie);
        // followers.add(Makawa);
        // Bonnie.setFollowers(followers);

       /* ArrayList<Doggo> followings = new ArrayList<>();
        followings.add(Rex);
        //followings.add(Akki);
        //followers.add(Bowie);
        Bonnie.setFollowings(followings);
*/
        ArrayList<String> photos = new ArrayList<>();
        String a = "golden2";
        Resources res = this.getResources();
        int resID = res.getIdentifier(a, "drawable", "io.mse.doggodate");

        photos.add("golden2");
        photos.add("golden3");
        photos.add("golden4");
        photos.add("golden5");

        Bonnie.setPhotos(photos);

        /**-------ALEX---------*/
        ArrayList<String> alexPhotos = new ArrayList<>();
        alexPhotos.add("labrador_1");
        alexPhotos.add("labrador_2");
        alexPhotos.add("labrador_3");
        alexPhotos.add("labrador_profile");
        Alex.setPhotos(alexPhotos);
        final ArrayList<DoggoPOJO> followingsA = new ArrayList<>();
        followingsA.add(Bonnie);
        //Alex.setFollowings(followingsA);
        /*ArrayList<Doggo> followersA = new ArrayList<>();
        followersA.add(Chichi);
        followersA.add(Alfonz);
        followersA.add(Nina);
        followersA.add(Bowie);
        followersA.add(Makawa);
        Alex.setFollowers(followersA);
        /**-------CHICHI---------*/
        ArrayList<String> ChiChiPhotos = new ArrayList<>();
        ChiChiPhotos.add("chivava_1");
        ChiChiPhotos.add("chivava_2");
        ChiChiPhotos.add("chivava3");
        Chichi.setPhotos(ChiChiPhotos);/*
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
        ArrayList<String> rexPhotos = new ArrayList<>();
        rexPhotos.add("wd_2");
        rexPhotos.add("wd_1");
        rexPhotos.add("wd_3");
        rexPhotos.add("wd_4");
        Rex.setPhotos(rexPhotos);/*
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
        ArrayList<String> akkiPhotos = new ArrayList<>();
        akkiPhotos.add(".akita_1");
        akkiPhotos.add("akita_2");
        akkiPhotos.add("akita_3");
        akkiPhotos.add("akita_4");
        akkiPhotos.add("akita_profile");
        Akki.setPhotos(akkiPhotos);/*
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
*/
       /* Log.i("adDoggosToDB", "got to the firestore cmd");
        FirebaseFirestore.getInstance().collection("Doggo").add(Bonnie).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Could not save the doggo ", e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.i("Doggo Saved!!!!! ", "congrats");

            }
        });*/
        // FirebaseFirestore.getInstance().collection("Doggo").add(Alex);
       ArrayList<DoggoPOJO> doggos = new ArrayList<DoggoPOJO>(Arrays.asList(Bonnie, Alex, Chichi, Rex, Akki ));

       FirebaseFirestore fb =  FirebaseFirestore.getInstance();
        for (final DoggoPOJO doggo: doggos) {
           fb.collection("Doggo").add(doggo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("RERERERERRE", "DocumentSnapshot added with ID: " + documentReference.getId());
                    doggo.setId(documentReference.getId());
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("dfgfdhvgh", "Error adding document", e);
                        }
                    });
        }

      /*  FirebaseFirestore.getInstance().collection("Doggo").add(Alex).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("RERERERERRE", "DocumentSnapshot added with ID: " + documentReference.getId());
                Alex.setId(documentReference.getId());

                Map<String, Object> data = new HashMap<>();
                //data.put("followings", followingsA);
                for ( DoggoPOJO doggo : followingsA) {
                    FirebaseFirestore.getInstance().collection("Followings").document(Alex.getId()).collection("followings")
                            .document(doggo.getId()).set(data);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dfgfdhvgh", "Error adding document", e);
                    }
                });

*/


        //  }



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        //set context so doggo can get ressources
            Doggo.setContextStatic(getApplicationContext());
//addDoggosToDB();
        navController = Navigation.findNavController(this,R.id.main_container);
        NavigationUI.setupWithNavController(navView,navController);

        active = homeFragment;
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Doggo Bonnie = new Doggo("Bonnie", "Golden retriever","profile_image");
        Doggo Alex = new Doggo("Alex", "Labrador",  "labrador_profile");
        Doggo Chichi = new Doggo("Chichi", "Chivava", "chivava_prof");
        Doggo Rex = new Doggo("Rex", "Wolfdog", "wolfdog_profile");
        Doggo Akki = new Doggo("Akki", "Akita Inu", "akita_profile");
        Doggo Alfonz = new Doggo("Alfonz", "Buldog", "dog4");
        Doggo Nina = new Doggo("Nina", "Labrador", "dog3");
        Doggo Bowie = new Doggo("Bowie", "Retriever", "dog2");
        Doggo Makawa = new Doggo("Makawa", "Chivuavua", "chivava_1");

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

        ArrayList<String> photos = new ArrayList<>();
        photos.add("golden2");
        photos.add("golden3");
        photos.add("golden4");
        photos.add("golden5");

        Bonnie.setPhotos(photos);
        Bonnie.setProfilePic("profile_image");

        /**-------ALEX---------*/
        ArrayList<String> alexPhotos = new ArrayList<>();
        alexPhotos.add("labrador_1");
        alexPhotos.add("labrador_2");
        alexPhotos.add("labrador_3");
        alexPhotos.add("labrador_profile");
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
        ArrayList<String> ChiChiPhotos = new ArrayList<>();
        ChiChiPhotos.add("chivava_1");
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
        activeDog = Chichi;

        /**-------REX---------*/
        ArrayList<String> rexPhotos = new ArrayList<>();
        rexPhotos.add("wd_2");
        rexPhotos.add("wd_1");
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
        ArrayList<String> akkiPhotos = new ArrayList<>();
        akkiPhotos.add("akita_1");

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
        park1 = new DoggoZone("Hugo-Wolf-Park" ,"8169 m2", "Nein", "HundenZone", false);
        park2 = new DoggoZone("GA Aspernstraße","323 m2", "Ja", "Hundenzone", false);
        park3 = new DoggoZone("Reithofferpark" ,"671", "true", "HZ", false);
        park4 = new DoggoZone("Hernalser Gürtel","672", "true", "HZ", false);
        park5 = new DoggoZone("PA Blériotgasse" ,"1850", "true" ," HZ", false);
        park6 = new DoggoZone("PA Gitlbauergasse","1983", "true","HZ", false);
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
                getSupportActionBar().show();
                getSupportActionBar().setTitle("DoggoZones");
                searchItem.setVisible(true);
                favoritesItem.setVisible(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case "search_fragment":
                getSupportActionBar().show();
                getSupportActionBar().setTitle("Doggos");
                searchItem.setVisible(true);
                favoritesItem.setVisible(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                final SearchView mSearchView = (SearchView) searchItem.getActionView();

                mSearchView.setQueryHint("Search");
                final SearchViewModel searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

                mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        Log.i(TAG, "reseting search");
                        searchViewModel.resetShownDoggos();
                        return false;
                    }
                });

                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.i(TAG, "submitting search");

                        FirestoreCallback whenSearchDone = new FirestoreCallback() {
                            @Override
                            public void onDataRetrieved(ArrayList<Doggo> doggos) {
                                searchViewModel.setShownDoggos(doggos);
                            }
                        };
                        searchViewModel.searchDoggos(whenSearchDone, mSearchView.getQuery().toString());
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
                break;
            case "profile_fragment":
                getSupportActionBar().show();
                getSupportActionBar().setTitle("My Profile");
                searchItem.setVisible(false);
                favoritesItem.setVisible(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case "doggozone_fragment":
                getSupportActionBar().show();
                getSupportActionBar().setTitle("DoggoZones");
                navView.getMenu().getItem(1).setChecked(true);
                searchItem.setVisible(false);
                favoritesItem.setVisible(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case "other_profile_fragment":
                getSupportActionBar().show();
                getSupportActionBar().setTitle("Doggo");
                navView.getMenu().getItem(2).setChecked(true);
                searchItem.setVisible(false);
                favoritesItem.setVisible(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
                default:
                    getSupportActionBar().show();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setTitle("DoggoDate");
                    searchItem.setVisible(false);
                    favoritesItem.setVisible(false);
                break;

        }

        return super.onCreateOptionsMenu(menu);
    }

    public MenuItem getSearchItem(){
        return this.searchItem;
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

    public void toOtherProfile(final int position, final int type) {
        Log.i(TAG, "toOtherProfileOnClick");
        final HelperViewModel hw = ViewModelProviders.of(this).get(HelperViewModel.class);

        final SearchViewModel searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel.getAllDoggos(new FirestoreCallback() {
                                         //retrieve current doggo
                                         @Override
                                         public void onDataRetrieved(ArrayList<Doggo> doggos) {
                                             selectedDog = doggos.get(position);
                                             hw.setCurrentDoggo(selectedDog);
                                             Log.i(TAG, "current doggo id " + selectedDog.getId());

                                             //get events
                                             searchViewModel.getEventForDoggo(selectedDog.getId(), new FirestoreEventCallback() {
                                                 //retrieve current doggos events
                                                 @Override
                                                 public void onDataRetrieved(ArrayList<DoggoEvent> events) {
                                                     hw.setCurrentDoggoEvents(events);
                                                     Log.i(TAG, "current doggo events " + events.size());

                                                     //when events done, get followings
                                                     searchViewModel.loadMyFollowings(new FirestoreFollowingsCallback() {
                                                                 @Override
                                                                 public void onDataRetrievedFollowings(ArrayList<Doggo> myFollowings) {
                                                                     Log.i(TAG, "current doggo followings " + myFollowings.size());
                                                                     hw.setCurrentDoggoFollowings(myFollowings);

                                                                   /*  if (type == 0) {
                                                                         navController.navigate(R.id.from_search_toOtherProfile);
                                                                         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                                                     } else if (type == 1) {
                                                                         navController.navigate(R.id.from_myProfile_to_otherProfile);
                                                                         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                                                                     }*/
                                                                     //when followings donne, get followers
                                                                    searchViewModel.loadMyFollowers(new FirestoreFollowersCallback() {
                                                                         @Override
                                                                         public void onDataRetrievedFollowers(ArrayList<Doggo> followers) {
                                                                             hw.setCurrentDoggoFollowers(followers);
                                                                             if (type == 0) {
                                                                                 navController.navigate(R.id.from_search_toOtherProfile);
                                                                                 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                                                             } else if (type == 1) {
                                                                                 navController.navigate(R.id.from_myProfile_to_otherProfile);
                                                                                 getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                                                                             }else if(type == 2){
                                                                                 navController.navigate(R.id.from_doggoZoneFragment_to_otherProfileFragment);
                                                                                 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                                                             }
                                                                         }
                                                                     }, selectedDog.getId());


                                                                         }
                                                                     }, selectedDog.getId());

                                                                 };

                                                         });

                                                     }});
                                                 }

      public Doggo getSelectedDog(){
            return this.selectedDog; }

    public ArrayList<DoggoEvent> getActiveDoggoEvents() {
        return activeDoggoEvents;
    }

    public void setActiveDoggoEvents(ArrayList<DoggoEvent> activeDoggoEvents) {
        this.activeDoggoEvents = activeDoggoEvents;
    }

     /*public void goToDoggoZone(View view){
        navController.navigate(R.id.toDoggoZone);
        searchItem.setVisible(false);
        favoritesItem.setVisible(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(TAG,"Go to Doggo Zone " + selectedDoggoZone.getName());
        doggoZoneFragment = new DoggozoneFragment();
        ((DoggozoneFragment)doggoZoneFragment).setSelectedDogoZone(selectedDoggoZone);
        active=doggoZoneFragment;

    }*/

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
