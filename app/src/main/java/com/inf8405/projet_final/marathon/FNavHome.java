package com.inf8405.projet_final.marathon;

import android.app.Activity;
import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class FNavHome extends Fragment {
    private View homeView;
    private ArrayList<String> fMarathons;
    /* Marathon fList */
    private ListView fListView;
    /* Cursor to load marathons list */
    private String fMarathonName, fMarathonDistance;

    /* Pop up */
    ContentResolver fResolver;
    private SearchView fSearchView;
    private ArrayAdapter<String> fMarathonAdapter;
    private ArrayList<String> fArrayList;

    Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        mActivity = getActivity();

        homeView = inflater.inflate(R.layout.activity_nav_home, container, false);

        /* <Just for test> */
        Marathon m1 = new Marathon();
        m1.setNom("Boston Maraton");
        m1.setDistance(110.0);
        Marathon m2 = new Marathon();
        m2.setNom("New York Maraton");
        m2.setDistance(120.0);
        Marathon m3 = new Marathon();
        m3.setNom("Montreal Maraton");
        m3.setDistance(130.0);
        Marathon m4 = new Marathon();
        m4.setNom("Vancouver Maraton");
        m4.setDistance(140.0);
        Marathon m5 = new Marathon();
        m5.setNom("Tokyo Maraton");
        m5.setDistance(150.0);

        fMarathons = new ArrayList<>();
        fMarathons.add(m1.getNom());
        fMarathons.add(m2.getNom());
        fMarathons.add(m3.getNom());
        fMarathons.add(m4.getNom());
        fMarathons.add(m5.getNom());

        this.fArrayList = new ArrayList<String>();
        fArrayList.addAll(fMarathons);

        /* </Just for test> */

//        Map<String,Marathon> myMap=DBContent.getInstance().GetMarathonMap(DBContent.getInstance().getActualParticipant().getId());
        fListView = (ListView) homeView.findViewById(R.id.lstVw_Marathons_FNH);
//        for(Map.Entry<String, Marathon> entry : myMap.entrySet())
//        {
//            fMarathons.add(entry.getValue());
//
//        }

        //fResolver = this.getContentResolver();


        if (fMarathons != null) {
            Log.e("count", "" + fMarathons.size());
            if (fMarathons.size() == 0) {
                Toast.makeText(getContext(), "No contacts in your contact list.", Toast.LENGTH_LONG).show();
            }

            fMarathonAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fMarathons);
            fListView.setAdapter(fMarathonAdapter);

            // Select item on listclick
            fListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Log.e("search", "here---------------- listener");

                    //DBContent dbContent=DBContent.getInstance();
                    //Marathon marathonData = fMarathons.get(i);
                    Toast.makeText(getContext(), "You've selected: " + fMarathons.get(i), Toast.LENGTH_LONG).show();
                    ShowMarathonPath(view, i);
                }
            });

            fListView.setFastScrollEnabled(true);

        } else {
            Log.e("Cursor close 1", "----------------");
        }
        fSearchView = (SearchView) homeView.findViewById(R.id.srchViewSearchContacts);

        fSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter((newText));
                return false;
            }
        });

        return homeView;
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        fMarathons.clear();
        if (charText.length() == 0) {
            fMarathons.addAll(fArrayList);
        } else {
            for (String wp : fArrayList) {
                if (wp.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    fMarathons.add(wp);
                }
            }
        }
        fMarathonAdapter.notifyDataSetChanged();
    }
    public void ShowMarathonPath (View view, int index) {
        Intent i = new Intent(getActivity(), IDisplayMarathon.class);
        Bundle marathonName = new Bundle();
        marathonName.putString("marathonName", fMarathons.get(index));
        i.putExtras(marathonName);
        startActivity(i);

    }

}
