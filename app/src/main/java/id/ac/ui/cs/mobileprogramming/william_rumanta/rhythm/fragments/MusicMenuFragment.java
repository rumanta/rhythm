package id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.fragments;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.CustomTouchListener;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.R;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.adapter.MusicListAdapter;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.models.Audio;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.onItemClickListener;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.services.MediaPlayerService;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.services.StorageUtil;

import static android.os.Build.VERSION.SDK_INT;

public class MusicMenuFragment extends Fragment {

    public static final String Broadcast_PLAY_NEW_AUDIO = "id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.PlayNewAudio";

    private MediaPlayerService player;
    boolean serviceBound = false;
    ArrayList<Audio> audioList;

    ImageView collapsingImageView;

    int imageIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_music_menu, container, false);

        loadAudioList(rootView);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        collapsingImageView = (ImageView) rootView.findViewById(R.id.collapsingImageView);

        loadCollapsingImage(imageIndex);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageIndex == 4) {
                    imageIndex = 0;
                    loadCollapsingImage(imageIndex);
                } else {
                    loadCollapsingImage(++imageIndex);
                }
            }
        });


        return rootView;
    }

    private void loadAudioList(View view) {
        loadAudio();
        initRecyclerView(view);
    }


    private void initRecyclerView(View view) {
        if (audioList != null && audioList.size() > 0) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            MusicListAdapter adapter = new MusicListAdapter(audioList, getActivity().getApplication());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addOnItemTouchListener(new CustomTouchListener(getActivity(), new onItemClickListener() {
                @Override
                public void onClick(View view, int index) {
                    playAudio(index);
                }
            }));
        }
    }

    private void loadCollapsingImage(int i) {
        TypedArray array = getResources().obtainTypedArray(R.array.images);
        collapsingImageView.setImageDrawable(array.getDrawable(i));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("serviceStatus", serviceBound);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            serviceBound = savedInstanceState.getBoolean("serviceStatus");
        }
    }

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };


    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getActivity().getApplicationContext());
            storage.storeAudio(audioList);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(getActivity(), MediaPlayerService.class);
            getActivity().startService(playerIntent);
            getActivity().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getActivity().getApplicationContext());
            storage.storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            getActivity().sendBroadcast(broadcastIntent);
        }

        System.out.println("-------------MEDIA PLAYERR----" + MediaPlayerService.getMediaPlayer());
    }

    /**
     * Load audio files using {@link ContentResolver}
     *
     * If this don't works for you, load the audio files to audioList Array your oun way
     */
    private void loadAudio() {
        ContentResolver contentResolver = getActivity().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            audioList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                // Save to audioList
                audioList.add(new Audio(data, title, album, artist));
            }
        }
        if (cursor != null)
            cursor.close();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            getActivity().unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }
}
