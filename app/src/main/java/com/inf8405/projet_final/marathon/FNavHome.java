package com.inf8405.projet_final.marathon;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FNavHome extends Fragment {
    View homeView;


    private Button mBtnFindPark = null;
    private Button mBtnQuitPark = null;

    Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle){
        mActivity = getActivity();

        homeView = inflater.inflate(R.layout.activity_nav_home, container, false);

        /* Manage the preferences button */
        mBtnFindPark = (Button) homeView.findViewById(R.id.btn0H);
        mBtnFindPark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FindParkingClicked(v);
            }
        });

        /* Manage the preferences button */
        mBtnQuitPark = (Button) homeView.findViewById(R.id.btn1H);
        mBtnQuitPark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                QuitParkingClicked(v);
            }
        });

        return homeView;
    }

    public void QuitParkingClicked (View view) {
        //Intent i = new Intent(getActivity(), IDisplayRoute.class);
        //startActivity(i);

    }

    public void FindParkingClicked(View view) {
        //Intent i = new Intent(getActivity(), MainActivity.class);
        //startActivity(i);

    }

}
