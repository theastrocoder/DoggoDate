package io.mse.doggodate.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.ViewPagerAdapter;
import io.mse.doggodate.databinding.ProfileFragmentBinding;
import io.mse.doggodate.entity.DoggoPOJO;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private Doggo activeDoggo;
    private Doggo firebaseDoggo;
    private TabLayout tabs;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    private MainActivity mainActivity;
    private ProfileViewModel profileViewModel;
    private Observer<Doggo> nameObserver;
    public ProfileFragment() {}
    public void setActiveDoggo(Doggo activeDoggo) {
        this.activeDoggo = activeDoggo;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final ProfileFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);

        //set handler and fragment manager for view pager in tab layout
        binding.setHandler(this);
        binding.setManager(getFragmentManager());
        final Doggo temp = new Doggo();
        binding.setDoggo(temp);
        nameObserver = new Observer<Doggo>() {
            @Override
            public void onChanged(@Nullable final Doggo activeDoggo) {
                // Update the UI, in this case,binding.
                binding.setDoggo(activeDoggo);
            }
        };
        profileViewModel.getActiveDoggo().observe(this, nameObserver);
        firebaseDoggo = profileViewModel.getActiveDoggo().getValue();

        mainActivity = (MainActivity)getActivity();
        mainActivity.invalidateOptionsMenu();
        mainActivity.getSupportActionBar().setTitle("My Profile");

        activeDoggo = ((MainActivity)getActivity()).getActiveDog();
       // View view =  binding.getRoot();
       // viewPager = (ViewPager) view.findViewById(R.id.viewPager);
       // viewPagerAdapter = new ViewPagerAdapter( getChildFragmentManager(), activeDoggo);
       // viewPager.setAdapter(viewPagerAdapter);

        //tabs = (TabLayout) view.findViewById(R.id.tabLayout);
        //tabs.setupWithViewPager(viewPager);

        //FloatingActionButton button =(FloatingActionButton) view.findViewById(R.id.followButton);
        //button.hide();
        //addDoggosToDB();

        return binding.getRoot();
    }
    @BindingAdapter({"bind:pager"})
    public static void bindViewPagerTabs(final TabLayout view, final ViewPager pagerView)
    {
        view.setupWithViewPager(pagerView, true);
    }


    @BindingAdapter({"bind:handler"})
    public static void bindViewPagerAdapter(final ViewPager view, final ProfileFragment fragment)
    {
        Doggo aDoggo = fragment.getFirebaseDoggo();
        Doggo activeDoggo = ((MainActivity)fragment.getActivity()).getActiveDog();
        final ViewPagerAdapter adapter = new ViewPagerAdapter( fragment.getFragmentManager(), aDoggo);
        view.setAdapter(adapter);
    }

    private Doggo getFirebaseDoggo() {
        return this.firebaseDoggo;
    }
    private ProfileViewModel getProfileViewModel() {
        return this.profileViewModel;
    }
    private void addDoggosToDB(){
        final DoggoPOJO Bonnie = new DoggoPOJO("Bonnie", "Golden retriever", R.drawable.profile_image);
        final DoggoPOJO Alex = new DoggoPOJO("Alex", "Labrador",  R.drawable.labrador_profile);
        DoggoPOJO Chichi = new DoggoPOJO("Chichi", "Chivava", R.drawable.chivava_prof);
        DoggoPOJO Rex = new DoggoPOJO("Rex", "Wolfdog", R.drawable.wolfdog_profile);
        DoggoPOJO Akki = new DoggoPOJO("Akki", "Akita Inu", R.drawable.akita_profile);
        DoggoPOJO Alfonz = new DoggoPOJO("Alfonz", "Buldog", R.drawable.dog4);
        DoggoPOJO Nina = new DoggoPOJO("Nina", "Labrador", R.drawable.dog3);
        DoggoPOJO Bowie = new DoggoPOJO("Bowie", "Retriever", R.drawable.dog2);
        DoggoPOJO Makawa = new DoggoPOJO("Makawa", "Chivuavua", R.drawable.chivava_1);

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
        ArrayList<Integer> photos = new ArrayList<>();
        photos.add(R.drawable.golden2);
        photos.add(R.drawable.golden3);
        photos.add(R.drawable.golden4);
        photos.add(R.drawable.golden5);

        Bonnie.setPhotos(photos);
        ArrayList<DoggoPOJO> doggos = new ArrayList<DoggoPOJO>(Arrays.asList(Bonnie, Alex, Chichi, Rex, Akki, Alfonz, Bowie, Makawa));
        doggos.add(Nina);
        Nina.getPhotos().add(1234353456);

        /**-------ALEX---------*/
        ArrayList<Integer> alexPhotos = new ArrayList<>();
        alexPhotos.add(R.drawable.labrador_1);
        alexPhotos.add(R.drawable.labrador_2);
        alexPhotos.add(R.drawable.labrador_3);
        alexPhotos.add(R.drawable.labrador_profile);
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
       ArrayList<Integer> ChiChiPhotos = new ArrayList<>();
        ChiChiPhotos.add(R.drawable.chivava_1);
        ChiChiPhotos.add(R.drawable.chivava_2);
        ChiChiPhotos.add(R.drawable.chivava3);
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
        ArrayList<Integer> rexPhotos = new ArrayList<>();
        rexPhotos.add(R.drawable.wd_2);
        rexPhotos.add(R.drawable.wd_1);
        rexPhotos.add(R.drawable.wd_3);
        rexPhotos.add(R.drawable.wd_4);
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
        ArrayList<Integer> akkiPhotos = new ArrayList<>();
        akkiPhotos.add(R.drawable.akita_1);
        akkiPhotos.add(R.drawable.akita_2);
        akkiPhotos.add(R.drawable.akita_3);
        akkiPhotos.add(R.drawable.akita_4);
        akkiPhotos.add(R.drawable.akita_profile);
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
        //FirebaseFirestore.getInstance().collection("Doggo").add(Bonnie);
       // FirebaseFirestore.getInstance().collection("Doggo").add(Alex);

      // for (final DoggoPOJO doggo: doggos){
           FirebaseFirestore.getInstance().collection("Doggo").add(Bonnie).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
               @Override
               public void onSuccess(DocumentReference documentReference) {
                   Log.d("RERERERERRE", "DocumentSnapshot added with ID: " + documentReference.getId());
                   Bonnie.setId(documentReference.getId());
               }
           })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Log.w("dfgfdhvgh", "Error adding document", e);
                       }
                   });

        FirebaseFirestore.getInstance().collection("Doggo").add(Alex).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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




      //  }



    }



}