package id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.R;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.models.Audio;

/**
 * Created by Valdio Veliu on 16-07-08.
 */
public class MusicListAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<Audio> list;
    Context context;

    public MusicListAdapter(List<Audio> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

class ViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    ImageView play_pause;

    ViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        play_pause = (ImageView) itemView.findViewById(R.id.play_pause);
    }
}
