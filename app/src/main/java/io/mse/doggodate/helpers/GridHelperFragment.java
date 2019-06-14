package io.mse.doggodate.helpers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.ViewPagerAdapter;
import io.mse.doggodate.databinding.FragmentGridHelperBinding;
import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.adapters.ImageAdapter;
import io.mse.doggodate.profile.ProfileFragment;
import io.mse.doggodate.profile.ProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridHelperFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GridHelperFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridHelperFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isSearch;
    private Doggo currentDoggo;
    private ArrayList<Integer> images = new ArrayList<>();
    private FragmentGridHelperBinding binding;
    private HelperViewModel helperViewModel;
    public void setCurrentDoggo(Doggo currentDoggo) {
        this.currentDoggo = currentDoggo;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
    }

    private OnFragmentInteractionListener mListener;


    public Doggo getCurrentDoggo() {
        return this.currentDoggo;
    }
    public GridHelperFragment( ) {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GridHelperFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridHelperFragment newInstance(String param1, String param2) {
        GridHelperFragment fragment = new GridHelperFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ArrayList<Integer> getImages() {
        return this.images;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grid_helper, container, false);
        helperViewModel = ViewModelProviders.of(getActivity()).get(HelperViewModel.class);

        final Doggo d = new Doggo();
        final ArrayList<String> images = new ArrayList<>();
        d.setPhotos(images);
        binding.gridView.setAdapter(new ImageAdapter( ((MainActivity)getActivity()).getApplicationContext(),(AppCompatActivity) ((MainActivity)getActivity()),d.getPhotos()));

        helperViewModel.getCurrentDoggo().observe(this,  new Observer<Doggo>() {
            @Override
            public void onChanged(@Nullable final Doggo activeDoggo) {
                // Update the UI, in this case,binding.

        binding.gridView.setAdapter(new ImageAdapter( ((MainActivity)getActivity()).getApplicationContext(),(AppCompatActivity) ((MainActivity)getActivity()),activeDoggo.getPhotos()));
            }
        });

        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
