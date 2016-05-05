package itp341.gupta.tushar.finalproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {

    public InformationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View v;
    Intent i;
    TextView text_test;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_information, container, false);
        text_test = (TextView) v.findViewById(R.id.text_test);


        i = getActivity().getIntent();
        PersonInfo person = (PersonInfo) i.getSerializableExtra(Constants.INTENT_VAR_PERSON);
        text_test.setText(person.getId() + "---" + person.getName() + "---" + person.getLearn() + "---" + person.getAbout() + "---" +
                person.getMentorfor() + "---" + person.getEmail() + "---" + person.getPictures().toString());


        return v;
    }

}
