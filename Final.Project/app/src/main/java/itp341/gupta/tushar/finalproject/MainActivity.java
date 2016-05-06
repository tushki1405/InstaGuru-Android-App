package itp341.gupta.tushar.finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    MainFragment mainFragment;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if we have userid, if we do, then it means we have logged in before.

        SharedPreferences prefs = getSharedPreferences(Constants.PREF_FILENAME, MODE_PRIVATE);

        /*Code for testing*/
        SharedPreferences.Editor prefedit = prefs.edit();
        //prefedit.putInt(Constants.PREF_USERID, 23);
        prefedit.putString(Constants.PREF_USERNAME, "Tushar Gupta");
        prefedit.commit();

        if(prefs.contains(Constants.PREF_USERID)) {
            Log.d(TAG, "Shared preference found. Going to MatchesActivity");
            Intent i = new Intent(getApplicationContext(), MatchesActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        Log.d(TAG, "Shared preference not found. showing register page.");

        //add below for deploying on phone
        /*PackageManager pm = this.getPackageManager();
        int hasPerm = pm.checkPermission(
                Manifest.permission.SEND_SMS,
                this.getPackageName());
        if (hasPerm != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
        }*/

        fm = getSupportFragmentManager();
        mainFragment = new MainFragment();

        //((FrameLayout) findViewById(R.id.fragment_container)).removeAllViews();

        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mainFragment);
        fragmentTransaction.commit();

    }
}
