package id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.fragments.Dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.R;

import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    public TextView countTv;
    public Button countBtn;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        countTv = (TextView) view.findViewById(R.id.text_dashboard);
        countTv.setText("Dashboard bro!");
        return view;
    }

    private void increaseCount() {
        int current = Integer.parseInt((String) countTv.getText());
        countTv.setText(String.valueOf(current+1));

    }

}
