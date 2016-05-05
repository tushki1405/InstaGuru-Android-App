package itp341.gupta.tushar.finalproject;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InformationActivity extends AppCompatActivity {

    InformationFragment infoFragment;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        fm = getSupportFragmentManager();
        infoFragment = new InformationFragment();

        //((FrameLayout) findViewById(R.id.fragment_container)).removeAllViews();

        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, infoFragment);
        fragmentTransaction.commit();
    }
}
