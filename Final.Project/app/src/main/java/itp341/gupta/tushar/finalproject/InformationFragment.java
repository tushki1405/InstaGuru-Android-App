package itp341.gupta.tushar.finalproject;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
    TextView text_info;
    PersonInfo person;
    String [] pictures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_information, container, false);
        text_info = (TextView) v.findViewById(R.id.text_info);
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

        //get person info
        i = getActivity().getIntent();
        person = (PersonInfo) i.getSerializableExtra(Constants.INTENT_VAR_PERSON);

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

        text_info.setText("Name: " + person.getName() + "\nCan be a mentor for: " + person.getMentorfor() +
                "\nWant to Learn: " + person.getLearn() + "\nAbout Me: " + person.getAbout());
        //person.getEmail()

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
