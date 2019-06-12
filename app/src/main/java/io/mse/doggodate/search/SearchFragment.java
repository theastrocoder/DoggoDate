package io.mse.doggodate.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.mse.doggodate.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.ImageAdapter;
import io.mse.doggodate.adapters.SearchImageAdapter;
import io.mse.doggodate.databinding.ProfileFragmentBinding;
import io.mse.doggodate.databinding.SearchFragmentBinding;
import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.helpers.GridHelperFragment;
//https://www.androidhive.info/2012/02/android-gridview-layout-tutorial/

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    GridView gridView;
    public SearchFragment() {}

    @BindingAdapter({"bind:handler"})
    public static void bindGridViewAdapterSearch(final GridView view, final SearchFragment fragment)
    {
        MainActivity mainActivity = ((MainActivity)fragment.getActivity());
       // view.setAdapter(new SearchImageAdapter(mainActivity.getApplicationContext(), mainActivity));

       /* view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                openProfile(position, 0);
            }
        });*/
       // view.setAdapter(new ImageAdapter( ((MainActivity)fragment.getActivity()).getApplicationContext(),(AppCompatActivity) ((MainActivity)fragment.getActivity()),images));

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).invalidateOptionsMenu();

        final SearchFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false);
        final SearchViewModel searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        binding.setHandler(this);
        binding.setManager(getFragmentManager());
        View view = binding.getRoot();
        SearchFirestoreCallback searchFirestoreCallback = new SearchFirestoreCallback() {
            @Override
            public void bindData(ArrayList<Doggo> doggos) {
                binding.gridView.setAdapter(new SearchImageAdapter(((MainActivity)getActivity()).getApplicationContext(), ((MainActivity)getActivity()), doggos));
                binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {

                        ((MainActivity)getActivity()).toOtherProfile(position, 0);
                    };
                });
            }
        };
        searchViewModel.getAllDoggos(searchFirestoreCallback);


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
    private void openProfile(int position, int type) {
        ((MainActivity)getActivity()).toOtherProfile(position, type);
    }

}
