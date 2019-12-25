package id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.fragments.Home.HomeFragment;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.fragments.MusicMenuFragment;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.fragments.Notification.NotificationsFragment;

public class NavigationMenuActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_menu_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
//                        mTextMessage.setText(R.string.title_home);
                        switchToHomeMenuFragment();
                        break;

                    case R.id.navigation_music:
//                        mTextMessage.setText(R.string.title_dashboard);
                        switchToMusicMenuFragment();
                        break;

                    case R.id.navigation_visualizer:
//                        mTextMessage.setText(R.string.title_notifications);
                        switchToVisualizerMenuFragment();
                        break;
                }

                return false;
            }
        });

//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_music, R.id.navigation_visualizer)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void switchToHomeMenuFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.navigation_home, new HomeFragment()).commit();
    }


    public void switchToMusicMenuFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.navigation_music, new MusicMenuFragment()).commit();
    }

    public void switchToVisualizerMenuFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.navigation_visualizer, new NotificationsFragment()).commit();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
