package itp341.gupta.tushar.finalproject;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MatchesActivity extends AppCompatActivity {

    MatchesFragment matchesFragment;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        fm = getSupportFragmentManager();
        matchesFragment = new MatchesFragment();

        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, matchesFragment);
        fragmentTransaction.commit();
    }
}
