package itp341.gupta.tushar.finalproject;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import com.squareup.picasso.Picasso;


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
    ImageView image_info_main;
    ImageButton image_info_1;
    ImageButton image_info_2;
    ImageButton image_info_3;
    ImageButton image_info_4;
    PersonInfo person;
    String [] pictures;
    TextView text_name;
    TextView text_info_mentor;
    TextView text_info_learn;
    TextView text_info_aboutme;
    Button button_contact;
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_information, container, false);
        text_name = (TextView) v.findViewById(R.id.text_name);
        text_info_mentor = (TextView) v.findViewById(R.id.text_info_mentor);
        text_info_learn = (TextView) v.findViewById(R.id.text_info_learn);
        text_info_aboutme = (TextView) v.findViewById(R.id.text_info_aboutme);
        button_contact = (Button) v.findViewById(R.id.button_contact);

        image_info_main = (ImageView) v.findViewById(R.id.image_info_main);
        image_info_1 = (ImageButton) v.findViewById(R.id.image_info_1);
        image_info_2 = (ImageButton) v.findViewById(R.id.image_info_2);
        image_info_3 = (ImageButton) v.findViewById(R.id.image_info_3);
        image_info_4 = (ImageButton) v.findViewById(R.id.image_info_4);

        HashMap<Integer, ImageView> imageMap = new HashMap<Integer, ImageView>();
        imageMap.put(1, image_info_1);
        imageMap.put(2, image_info_2);
        imageMap.put(3, image_info_3);
        imageMap.put(4, image_info_4);

        prefs = this.getActivity().getSharedPreferences(Constants.PREF_FILENAME, this.getActivity().MODE_PRIVATE);

        //get person info
        i = getActivity().getIntent();
        person = (PersonInfo) i.getSerializableExtra(Constants.INTENT_VAR_PERSON);

        text_name.setText(person.getName());
        text_info_mentor.setText(person.getMentorfor());
        text_info_learn.setText(person.getLearn());
        text_info_aboutme.setText(person.getAbout());

        pictures = person.getPictures();

        boolean first = true;
        for(int i=0; i < pictures.length; i++){
            if(first){
                first = false;
                Picasso.with(getContext()).load(Constants.API_IMAGE + person.getId() + "_" + pictures[i]).into(image_info_main);
                //new DownloadImageTask(image_info_main).execute(Constants.API_IMAGE + person.getId() + "_" + pictures[i]);
            }
            Picasso.with(getContext()).load(Constants.API_IMAGE + person.getId() + "_" + pictures[i]).into(imageMap.get(i + 1));
            //new DownloadImageTask(imageMap.get(i+1)).execute(Constants.API_IMAGE + person.getId() + "_" + pictures[i]);
        }

        //hide remaining icons
        if(pictures.length < 4){
            for(int i=pictures.length; i<4; i++){
                imageMap.get(i+1).setVisibility(View.GONE);
            }
        }

        image_info_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getContext()).load(Constants.API_IMAGE + person.getId() + "_" + pictures[0]).into(image_info_main);
                //new DownloadImageTask(image_info_main).execute(Constants.API_IMAGE + person.getId() + "_" + pictures[0]);
            }
        });

        image_info_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getContext()).load(Constants.API_IMAGE + person.getId() + "_" + pictures[1]).into(image_info_main);
                //new DownloadImageTask(image_info_main).execute(Constants.API_IMAGE + person.getId() + "_" + pictures[1]);
            }
        });

        image_info_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getContext()).load(Constants.API_IMAGE + person.getId() + "_" + pictures[2]).into(image_info_main);
                //new DownloadImageTask(image_info_main).execute(Constants.API_IMAGE + person.getId() + "_" + pictures[2]);
            }
        });

        image_info_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getContext()).load(Constants.API_IMAGE + person.getId() + "_" + pictures[3]).into(image_info_main);
                //new DownloadImageTask(image_info_main).execute(Constants.API_IMAGE + person.getId() + "_" + pictures[3]);
            }
        });

        button_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String message = "Hi " + person.getName() + ",\n\n" + "I saw your profile on InstaGURU and " +
                            "would love to connect and talk more. \n\nThanks,\n" + prefs.getString(Constants.PREF_USERNAME, "") +
                            "\n\nSent via InstaGURU. Please reply to connect.";
                    SmsManager.getDefault().sendTextMessage("2132949598", null, message, null, null);
                    //person.getEmail() -> this has phone number
                    Toast.makeText(getContext(), "Text message sent.", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex){
                    //Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    /*//reference: http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        PersonInfo person;
        static final String TAG = "DownLoadImageTask";

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                Log.d(TAG, "Downloading image" + urldisplay);
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }
            catch(FileNotFoundException e){
                Log.e(TAG, "Filenotfound, setting default: " + e.getMessage());
                return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.image_upload);
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage());
                //e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Log.d(TAG, "Image download successful.");
            bmImage.setImageBitmap(result);
        }
    }*/

}
