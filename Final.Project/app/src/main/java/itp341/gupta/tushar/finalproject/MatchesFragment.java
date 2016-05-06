package itp341.gupta.tushar.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends Fragment {

    public MatchesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View v;
    int userid = -1;
    ListView listview;
    MatchesAdapter adapter;
    ArrayList<PersonInfo> persons;
    EditText edit_search;
    ImageButton button_search;
    ProgressBar progress_search;
    static final String TAG = "MatchesFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v =  inflater.inflate(R.layout.fragment_matches, container, false);
        persons = new ArrayList<PersonInfo>();
        edit_search = (EditText) v.findViewById(R.id.edit_search);
        button_search = (ImageButton) v.findViewById(R.id.button_search);
        listview = (ListView) v.findViewById(R.id.list_matches);
        progress_search = (ProgressBar) v.findViewById(R.id.progress_search);

        //set userid from shared preference
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREF_FILENAME, getActivity().MODE_PRIVATE);
        if(prefs.contains(Constants.PREF_USERID)) {
            userid = prefs.getInt(Constants.PREF_USERID, -1);
        }


        if(userid != -1){
            PostData post = new PostData(v.getContext());
            post.execute(userid);
        }
        else{
            Toast.makeText(getContext(), "Error in application.", Toast.LENGTH_SHORT).show();
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), InformationActivity.class);
                i.putExtra(Constants.INTENT_VAR_PERSON, persons.get(position));
                startActivityForResult(i, Constants.INTENT_INFO);
                //Toast.makeText(getContext(), persons.get(position).getId() + "", Toast.LENGTH_SHORT).show();
            }
        });

        //search from database and update the listview
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Search initiated...");
                String searchterm = edit_search.getText().toString();
                if(searchterm != null && !searchterm.equals("")){
                    PostDataSearch post = new PostDataSearch(v.getContext());
                    button_search.setVisibility(View.GONE);
                    progress_search.setVisibility(View.VISIBLE);
                    post.execute(searchterm);
                }
            }
        });

        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.INTENT_INFO){
            //add code here if plan to do something specific
            //else change for result to normal intent call
        }
    }

    //class for async request to post data
    private class PostData extends AsyncTask<Integer, Void, String> {

        private Context mContext;

        public PostData(Context context) {
            mContext = context;
        }
        static final String TAG = "PostDataMatchesFragment";

        @Override
        protected String doInBackground(Integer... params) {
            Log.d(TAG, "Starting asynctask...");

            String charset = "UTF-8";
            String requestURL = Constants.API_URL + Constants.URL_GETMATCHES + params[0];

            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                Log.d(TAG, "Sending post request.");
                List<String> response = multipart.finish();
                Log.d(TAG, "Post request completed.");

                StringBuffer b = new StringBuffer();
                for (String s : response) {
                    b.append(s);
                }
                return b.toString();
            } catch (Exception ex) {
                Log.e(TAG, "Error in posting data" + ex.getMessage());
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "Begin onPostExecute.");
            if (!result.equals("error")) {
                Log.d(TAG, result);
                try {
                    JSONObject obj = new JSONObject(result);
                    persons.clear();
                    if(obj.getInt(Constants.API_RESPONSECODE) == 200){
                        JSONArray arr = obj.getJSONArray(Constants.API_MATCHDATA);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jsonobject = arr.getJSONObject(i);
                            PersonInfo person = new PersonInfo();
                            person.setId(jsonobject.getInt(Constants.API_ID));
                            person.setName(jsonobject.getString(Constants.API_NAME));
                            person.setEmail(jsonobject.getString(Constants.API_EMAIL));
                            person.setMentorfor(jsonobject.getString(Constants.API_MENTOR));
                            person.setLearn(jsonobject.getString(Constants.API_LEARN));
                            person.setAbout(jsonobject.getString(Constants.API_ABOUT));
                            //person.setDob(sdf.format(new Date(jsonobject.getString(Constants.API_DOB))));

                            //process pictures string
                            String picString = jsonobject.getString(Constants.API_PICTURES);
                            if(picString != null && !picString.equals("") && !picString.equals("[]")){
                                picString = picString.split("\\[")[1].split("\\]")[0];
                                String[] pics = picString.split(",");
                                for(int j=0; j<pics.length; j++){
                                    pics[j] = pics[j].trim().replace("'","");
                                }
                                person.setPictures(pics);
                            }
                            persons.add(person);
                        }

                        adapter = new MatchesAdapter(getActivity().getApplicationContext(), persons);
                        listview = (ListView) v.findViewById(R.id.list_matches);
                        listview.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(mContext, "Error in fetching information.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error in fetching information." + obj.getString(Constants.API_RESPONSEMSG));
                    }
                }
                catch(Exception ex){
                    Toast.makeText(mContext, "Error in parsing json data.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error in parsing json data. " + ex.getMessage());
                }
            } else {
                Toast.makeText(mContext, "Error posting data to server.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Please check internet connection.");
            }
        }
    }


    //class for async request to post data for searching
    private class PostDataSearch extends AsyncTask<String, Void, String> {

        private Context mContext;

        public PostDataSearch(Context context) {
            mContext = context;
        }
        static final String TAG = "PostDataSearch";

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "Starting asynctask...");

            String charset = "UTF-8";
            String requestURL = Constants.API_URL + Constants.URL_GETSEARCH + params[0];

            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                Log.d(TAG, "Sending post request.");
                List<String> response = multipart.finish();
                Log.d(TAG, "Post request completed.");

                StringBuffer b = new StringBuffer();
                for (String s : response) {
                    b.append(s);
                }
                return b.toString();
            } catch (Exception ex) {
                Log.e(TAG, "Error in posting data" + ex.getMessage());
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "Begin onPostExecute.");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            progress_search.setVisibility(View.GONE);
            button_search.setVisibility(View.VISIBLE);
            if (!result.equals("error")) {
                Log.d(TAG, result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if(obj.getInt(Constants.API_RESPONSECODE) == 200){
                        JSONArray arr = obj.getJSONArray(Constants.API_MATCHDATA);
                        if(arr.length() == 0){
                            Toast.makeText(getActivity().getApplicationContext(), "No results found. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                        persons.clear();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jsonobject = arr.getJSONObject(i);
                            PersonInfo person = new PersonInfo();
                            person.setId(jsonobject.getInt(Constants.API_ID));
                            person.setName(jsonobject.getString(Constants.API_NAME));
                            person.setEmail(jsonobject.getString(Constants.API_EMAIL));
                            person.setMentorfor(jsonobject.getString(Constants.API_MENTOR));
                            person.setLearn(jsonobject.getString(Constants.API_LEARN));
                            person.setAbout(jsonobject.getString(Constants.API_ABOUT));
                            //person.setDob(sdf.format(new Date(jsonobject.getString(Constants.API_DOB))));

                            //process pictures string
                            String picString = jsonobject.getString(Constants.API_PICTURES);
                            if(picString != null && !picString.equals("") && !picString.equals("[]")){
                                picString = picString.split("\\[")[1].split("\\]")[0];
                                String[] pics = picString.split(",");
                                for(int j=0; j<pics.length; j++){
                                    pics[j] = pics[j].trim().replace("'","");
                                }
                                person.setPictures(pics);
                            }
                            persons.add(person);
                        }

                        adapter = new MatchesAdapter(getActivity().getApplicationContext(), persons);
                        listview = (ListView) v.findViewById(R.id.list_matches);
                        listview.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(mContext, "Error in creating new account.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error in creating new account." + obj.getString(Constants.API_RESPONSEMSG));
                    }
                }
                catch(Exception ex){
                    Toast.makeText(mContext, "Error in parsing json data.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error in parsing json data. " + ex.getMessage());
                }
            } else {
                Toast.makeText(mContext, "Error posting data to server.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error in posting data to server");
            }
        }
    }

}
