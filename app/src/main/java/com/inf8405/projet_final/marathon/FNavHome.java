package com.inf8405.projet_final.marathon;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class FNavHome extends Fragment {
    private View homeView;
    private ArrayList<String> fHistoricMarathonsList;
    private ArrayList<String> fActalMarathonsList;
    private ArrayList<String> fHistoricMarathonsListZapas;
    private ArrayList<String> fActalMarathonsListZapas;
    /* Marathon fList */
    private ListView fListView;
    /* Cursor to load marathons list */
    private String fMarathonName, fMarathonDistance;

    /* Pop up */
    ContentResolver fResolver;
    private SearchView fSearchView;
    private ArrayAdapter<String> fMarathonAdapter;
    private ArrayList<String> fArrayList;
    private RadioGroup mRadioGroup = null;
    private RadioButton radioButtonItem;
    private int mIdGroupe;
    private boolean isHistoricMarathonListSelected = true;
    private Map<String,Marathon> fHistoricMarathonMap;
    private Map<String,Marathon> fActualMarathonMap;
    private Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        mActivity = getActivity();
        fHistoricMarathonsList = (ArrayList<String>) new ArrayList<String>();
        fActalMarathonsList = (ArrayList<String>) new ArrayList<String>();
        fHistoricMarathonsListZapas = (ArrayList<String>) new ArrayList<String>();
        fActalMarathonsListZapas = (ArrayList<String>) new ArrayList<String>();
        homeView = inflater.inflate(R.layout.activity_nav_home, container, false);

        //fMarathons = DBContent.getInstance().getListHistoricMarathon();
        /* <Just for test> */

        /* Get historic marathon map */
        fHistoricMarathonMap = DBContent.getInstance().getListHistoricMarathon();
        fListView = (ListView) homeView.findViewById(R.id.lstVw_Marathons_FNH);
        for(Map.Entry<String, Marathon> entry : fHistoricMarathonMap.entrySet())
        {
            fHistoricMarathonsList.add(entry.getValue().getNom());
            fHistoricMarathonsListZapas.add(entry.getValue().getNom());

        }
        /* Get actual marathon map */
        fActualMarathonMap = DBContent.getInstance().getListActualMarathon();
        fListView = (ListView) homeView.findViewById(R.id.lstVw_Marathons_FNH);
        for(Map.Entry<String, Marathon> entry : fActualMarathonMap.entrySet())
        {
            fActalMarathonsList.add(entry.getValue().getNom());
            fActalMarathonsListZapas.add(entry.getValue().getNom());

        }

        /* Manage radio group */
        mRadioGroup = (RadioGroup) homeView.findViewById(R.id.rdGrp_ListMarthon);
        mRadioGroup.check(R.id.rdBtnLMH);
        mIdGroupe = mRadioGroup.getCheckedRadioButtonId();
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdBtnLMH:
                        //fMarathons = DBContent.getInstance().getListHistoricMarathon();
                        isHistoricMarathonListSelected = true;
                        if (fHistoricMarathonsList != null) {
                            Log.e("count", "" + fHistoricMarathonsList.size());
                            if (fHistoricMarathonsList.size() == 0) {
                                Toast.makeText(getContext(), "No historic marathon in your list.", Toast.LENGTH_LONG).show();
                            }

                            fMarathonAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fHistoricMarathonsList);
                            fListView.setAdapter(fMarathonAdapter);
                            fListView.setFastScrollEnabled(true);

                        } else {
                            Log.e("Cursor close 1", "----------------");
                        }

                        break;
                    case R.id.rdBtnLMA:
                        //fMarathons = DBContent.getInstance().getListActualMarathon();
                        isHistoricMarathonListSelected = false;
                        if (fActalMarathonsList != null) {
                            Log.e("count", "" + fActalMarathonsList.size());
                            if (fActalMarathonsList.size() == 0) {
                                Toast.makeText(getContext(), "No historic marathon in your list.", Toast.LENGTH_LONG).show();
                            }

                            fMarathonAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fActalMarathonsList);
                            fListView.setAdapter(fMarathonAdapter);
                            fListView.setFastScrollEnabled(true);

                        } else {
                            Log.e("Cursor close 1", "----------------");
                        }
                        break;
                }
            }
        });

        //fResolver = this.getContentResolver();


        if (fHistoricMarathonsList != null) {
            Log.e("count", "" + fHistoricMarathonsList.size());
            if (fHistoricMarathonsList.size() == 0) {
                Toast.makeText(getContext(), "No contacts in your contact list.", Toast.LENGTH_LONG).show();
            }

            fMarathonAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fHistoricMarathonsList);
            fListView.setAdapter(fMarathonAdapter);

            // Select item on listclick
            fListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (isHistoricMarathonListSelected) {
                        //DBContent dbContent=DBContent.getInstance();
                        //Marathon marathonData = fMarathons.get(i);
                        Toast.makeText(getContext(), "You've selected: " + fHistoricMarathonsList.get(i), Toast.LENGTH_LONG).show();
                        ShowMarathonPath(view, i);
                    }
                    else{
                        //DBContent dbContent=DBContent.getInstance();
                        //Marathon marathonData = fMarathons.get(i);
                        Toast.makeText(getContext(), "You've selected: " + fActalMarathonsList.get(i), Toast.LENGTH_LONG).show();
                        ShowMarathonPath(view, i);
                    }
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
        if(isHistoricMarathonListSelected) {
            charText = charText.toLowerCase(Locale.getDefault());
            fHistoricMarathonsList.clear();
            if (charText.length() == 0) {
                fHistoricMarathonsList.addAll(fHistoricMarathonsListZapas);
            } else {
                for (String wp : fHistoricMarathonsListZapas) {
                    if (wp.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        fHistoricMarathonsList.add(wp);
                    }
                }
            }
            fMarathonAdapter.notifyDataSetChanged();
        }
        else{
            charText = charText.toLowerCase(Locale.getDefault());
            fActalMarathonsList.clear();
            if (charText.length() == 0) {
                fActalMarathonsList.addAll(fActalMarathonsListZapas);
            } else {
                for (String wp : fActalMarathonsListZapas) {
                    if (wp.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        fActalMarathonsList.add(wp);
                    }
                }
            }
            fMarathonAdapter.notifyDataSetChanged();
        }
    }
    public void ShowMarathonPath (View view, int index) {

        /* Display historic ou actual marathon in view list */
        if(isHistoricMarathonListSelected) {
            Toast.makeText(getContext(), "You've selected: " + fHistoricMarathonsList.get(index), Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), IDisplayHistoricMarathon.class);
            Bundle marathonName = new Bundle();
            marathonName.putString("marathonName", fHistoricMarathonsList.get(index));
            i.putExtras(marathonName);
            startActivity(i);
        }else{
            Toast.makeText(getContext(), "You've selected: " + fActalMarathonsList.get(index), Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), IDisplayCurrentMarathon.class);
            Bundle marathonName = new Bundle();
            marathonName.putString("marathonName", fActalMarathonsList.get(index));
            i.putExtras(marathonName);
            startActivity(i);

        }

    }

}
