package itp341.gupta.tushar.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Tushar on 5/5/2016.
 */
public class MatchesAdapter extends ArrayAdapter<PersonInfo> {

    ArrayList<PersonInfo> mArray;

    public MatchesAdapter(Context context, ArrayList<PersonInfo> persons) {
        super(context, 0, persons);
        mArray = persons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
// Get the data item for this position
        PersonInfo person = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.match_item, parent, false);
        }
        // Lookup view for data population
        TextView text_about = (TextView) convertView.findViewById(R.id.text_about);
        TextView text_mentorlearn = (TextView) convertView.findViewById(R.id.text_mentorlearn);
        ImageView image_1 = (ImageView) convertView.findViewById(R.id.image_1);
        // Populate the data into the template view using the data object
        text_about.setText(person.getAbout());
        text_mentorlearn.setText("Mentor For: " + person.getMentorfor() + "\nLearn: " + person.getLearn());

        if(person.getPictures()[0] != null && !person.getPictures()[0].equals("") && !person.isImageset()){
            new DownloadImageTask(image_1, person)
                    .execute(Constants.API_IMAGE + person.getId() + "_" + person.getPictures()[0]);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    public void updateAdapter(ArrayList<PersonInfo> arrylst) {
        this.mArray= arrylst;
        //and call notifyDataSetChanged
        notifyDataSetChanged();
    }

    //reference: http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        PersonInfo person;
        static final String TAG = "DownLoadImageTask";

        public DownloadImageTask(ImageView bmImage, PersonInfo person) {
            this.person = person;
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
            bmImage.setImageBitmap(result);
            person.setImageset(true);
        }
    }
}

