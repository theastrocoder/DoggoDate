package io.mse.doggodate.helpers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
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
    private Doggo selectedDoggo;
    private ArrayList<Integer> images = new ArrayList<>();
    public Doggo getSelectedDoggo() {
        return selectedDoggo;
    }
    private FragmentGridHelperBinding binding;
    private ProfileViewModel profileViewModel;
    public void setSelectedDoggo(Doggo selectedDoggo) {
        this.selectedDoggo = selectedDoggo;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
    }

    private OnFragmentInteractionListener mListener;

    public GridHelperFragment() {
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
    @BindingAdapter({"bind:handler"})
    public static void bindGridViewAdapter(final GridView view, final GridHelperFragment fragment)
    {
        MainActivity mainActivity = ((MainActivity)fragment.getActivity());
        Log.i("GridViewAdapter ", "got " + fragment + " as a fragment");
        Doggo aDoggo = fragment.getSelectedDoggo();
        //aDoggo.setPhotos(fragment.getImages());
        //final ViewPagerAdapter adapter = new ViewPagerAdapter( fragment.getFragmentManager(), aDoggo);
        ArrayList<Integer> images = new ArrayList<>();
        for (int i = 0; i < mainActivity.getActiveDog().getPhotos().size(); i++) {
            images.add(mainActivity.getActiveDog().getPhotos().get(i));
        }
        view.setAdapter(new ImageAdapter( ((MainActivity)fragment.getActivity()).getApplicationContext(),(AppCompatActivity) ((MainActivity)fragment.getActivity()),images));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grid_helper, container, false);
         profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View view = binding.getRoot();

        binding.setHandler(this);
        binding.setManager(getFragmentManager());
        binding.setVariable(BR.photos, images);

        //if it is search, then show all dogs

       /* ArrayList<Integer> images = new ArrayList<>();

       if (isSearch) {
            for (int i = 0; i < ((MainActivity) getActivity()).getDefaultSearch().size(); i++) {
                images.add(((MainActivity) getActivity()).getDefaultSearch().get(i).getProfilePic());
            }
            //if its profile, show all images of the dog
        } else {

            for (int i = 0; i < ((MainActivity) getActivity()).getActiveDog().getPhotos().size(); i++) {
                images.add(((MainActivity) getActivity()).getActiveDog().getPhotos().get(i));
            }
*/
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
