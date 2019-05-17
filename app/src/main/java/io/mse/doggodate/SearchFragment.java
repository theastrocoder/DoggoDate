package io.mse.doggodate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
//https://www.androidhive.info/2012/02/android-gridview-layout-tutorial/

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    GridView gridView;
    public SearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        //SET GRIDVIEW WITH DEFAULT IMAGES
        gridView = (GridView) view.findViewById(R.id.grid_view);
        // Instance of ImageAdapter Class
        ArrayList<Integer> images = new ArrayList<>();
        for (int i=0;i<((MainActivity)getActivity()).getDefaultSearch().size();i++){
            images.add(((MainActivity)getActivity()).getDefaultSearch().get(i).getProfilePic());
        }
        gridView.setAdapter(new SearchImageAdapter(getActivity().getApplicationContext(), (MainActivity) getActivity()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

               openProfile(position);
            }
        });

        return view;
    }

   /* private void toOtherProfileFragment() {


    FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
    transaction.replace(R.id.main_container,((MainActivity)getActivity()).getFragment("5"));

    transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();

        ((MainActivity)getActivity()).setActive(((MainActivity)getActivity()).getFragment("5"), this);
    }
    */
    private void openProfile(int position) {
        ((MainActivity)getActivity()).toOtherProfile(position);
    }

}
