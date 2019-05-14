package io.mse.doggodate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ListHelperFragment extends Fragment {


    ListView list;

    public ListHelperFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        list = (ListView) view.findViewById(R.id.list);
        ArrayList<String> stringList = new ArrayList();

        stringList.add("Item 1A");
        stringList.add("Item 1B");
        stringList.add("Item 1C");
        stringList.add("Item 1D");
        Log.i("ListFragment", "created 4 items");
        CustomAdapter adapter = new CustomAdapter(stringList,(AppCompatActivity)getActivity());
        list.setAdapter(adapter);

        return view;
    }
}
