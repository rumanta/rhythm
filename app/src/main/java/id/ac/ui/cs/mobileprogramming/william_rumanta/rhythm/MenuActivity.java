package id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG_READ_STORAGE = "READ STORAGE";
    private static final int READ_STORAGE_REQUEST_CODE = 1;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        Intent intent = new Intent(this, MusicDetailActivity.class);
        startActivity(intent);
        finish();

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG_READ_STORAGE, "onRequestPermissionsResult: " + requestCode);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG_READ_STORAGE, "onRequestPermissionsResult: request");

                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Pleaseeeeee", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "HI! Welcome to the app", Toast.LENGTH_SHORT).show();

        } else {
            askForPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_REQUEST_CODE);
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MenuActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MenuActivity.this, new String[]{permission}, requestCode);

            } else {

                Log.d(TAG_READ_STORAGE, "askForPermission: " + permission);

                ActivityCompat.requestPermissions(MenuActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }
}
