package io.mse.doggodate.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.SearchImageAdapter;
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
        ((MainActivity)getActivity()).invalidateOptionsMenu();
        //SET GRIDVIEW WITH DEFAULT IMAGES
        gridView = (GridView) view.findViewById(R.id.grid_view);
        // Instance of ImageAdapter Class
        ArrayList<Integer> images = new ArrayList<>();
        for (int i = 0; i<((MainActivity)getActivity()).getDefaultSearch().size(); i++){
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
