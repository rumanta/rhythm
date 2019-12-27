package id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.R;

public class HomeMenuFragment extends Fragment {

    public TextView countTv;

    public HomeMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        countTv = (TextView) view.findViewById(R.id.text_dashboard);

        countTv.setText("Spotify Top Artist List!");

        return view;
    }
}
