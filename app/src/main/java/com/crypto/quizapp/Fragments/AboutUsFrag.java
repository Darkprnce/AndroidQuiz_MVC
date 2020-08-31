package com.crypto.quizapp.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.crypto.quizapp.R;

public class AboutUsFrag extends Fragment {

    View mview;

    public static AboutUsFrag newInstance() {

        Bundle args = new Bundle();

        AboutUsFrag fragment = new AboutUsFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview =  inflater.inflate(R.layout.aboutus_frag, container, false);
        getActivity().setTitle("About us");
        return mview;
    }

}
