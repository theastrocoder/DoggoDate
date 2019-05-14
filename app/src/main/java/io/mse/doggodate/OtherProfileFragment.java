package io.mse.doggodate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherProfileFragment extends Fragment implements MainActivity.OnBackPressedListener{

    private ImageView imageView;

    public OtherProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        imageView = (ImageView) view.findViewById(R.id.profile_image);
        imageView.setImageResource(R.drawable.profile_image);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       //Button button = view.findViewById(R.id.followButton);
       // button.setVisibility(View.VISIBLE);
        return view;
    }


 @Override
    public void doBack() {
            // I'm viewing Fragment C
            getFragmentManager().popBackStack();
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
     ((MainActivity)getActivity()).setActive(new SearchFragment(), this);

        }

    }

