package com.example.foodies;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MyFriendsFragment extends Fragment {

    Button testMyF;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_my_friends, container, false);
         testMyF =view.findViewById(R.id.MyFriends_test_btn);
         testMyF.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_myFriendsFragment3_to_userName));

    return view;
    }
}