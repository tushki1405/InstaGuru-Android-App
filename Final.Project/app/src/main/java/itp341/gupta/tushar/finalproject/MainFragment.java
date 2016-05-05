package itp341.gupta.tushar.finalproject;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View v;
    EditText edit_name;
    EditText edit_dob;
    EditText edit_email;
    EditText edit_mentor;
    EditText edit_learn;
    EditText edit_about;
    Button button_register;
    ImageButton button_image_1;
    ImageButton button_image_2;
    ImageButton button_image_3;
    ImageButton button_image_4;
    HashMap<String, File> imageURI;
    Context mContext;
    static final String TAG= "MainFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_main, container, false);

        Log.d(TAG, "Starting activity.");

        //declare all fields on the register activity
        edit_name = (EditText) v.findViewById(R.id.edit_name);
        edit_dob = (EditText) v.findViewById(R.id.edit_dob);
        edit_email = (EditText) v.findViewById(R.id.edit_email);
        edit_mentor = (EditText) v.findViewById(R.id.edit_mentor);
        edit_learn = (EditText) v.findViewById(R.id.edit_learn);
        edit_about = (EditText) v.findViewById(R.id.edit_about);
        button_register = (Button) v.findViewById(R.id.button_register);
        button_image_1 = (ImageButton) v.findViewById(R.id.image_1);
        button_image_2 = (ImageButton) v.findViewById(R.id.image_2);
        button_image_3 = (ImageButton) v.findViewById(R.id.image_3);
        button_image_4 = (ImageButton) v.findViewById(R.id.image_4);
        imageURI = new HashMap<String, File>();
        mContext = v.getContext();


        //add action for register
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked button. Start processing.");

                HashMap<String, String> data = new HashMap<String, String>();
                data.put(Constants.API_NAME, edit_name.getText().toString());
                data.put(Constants.API_DOB, edit_dob.getText().toString());
                data.put(Constants.API_EMAIL, edit_email.getText().toString());
                data.put(Constants.API_MENTOR, edit_mentor.getText().toString());
                data.put(Constants.API_LEARN, edit_learn.getText().toString());
                data.put(Constants.API_ABOUT, edit_about.getText().toString());

                Log.d(TAG, "Hashmap ready. Begin asynctask.");
                PostData post = new PostData(v.getContext());
                post.execute(data);
                Log.d(TAG, "Asynctask command executed.");
            }
        });

        button_image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunImageIntent(Constants.IMAGE_1);
            }
        });

        button_image_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunImageIntent(Constants.IMAGE_2);
            }
        });

        button_image_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunImageIntent(Constants.IMAGE_3);
            }
        });

        button_image_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunImageIntent(Constants.IMAGE_4);
            }
        });

        return v;
    }

    //Method to select image from the gallery
    private void RunImageIntent(int forImage) {
        //reference: http://codetheory.in/android-pick-select-image-from-gallery-with-intents/
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), forImage);
    }

    //reference: http://developer.android.com/training/basics/data-storage/files.html
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                //set filename
                String filename = "";
                switch (requestCode) {
                    case Constants.IMAGE_1:
                        filename = "image1.png";
                        break;
                    case Constants.IMAGE_2:
                        filename = "image2.png";
                        break;
                    case Constants.IMAGE_3:
                        filename = "image3.png";
                        break;
                    case Constants.IMAGE_4:
                        filename = "image4.png";
                        break;
                }
                File file = new File(mContext.getFilesDir(), filename);
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.close();

                //save image path in hashmap for upload
                if (requestCode == Constants.IMAGE_1) {
                    imageURI.put("file1", file);
                    ImageView imageView = (ImageView) v.findViewById(R.id.image_1);
                    imageView.setImageBitmap(bitmap);
                } else if (requestCode == Constants.IMAGE_2) {
                    imageURI.put("file2", file);
                    ImageView imageView = (ImageView) v.findViewById(R.id.image_2);
                    imageView.setImageBitmap(bitmap);
                } else if (requestCode == Constants.IMAGE_3) {
                    imageURI.put("file3", file);
                    ImageView imageView = (ImageView) v.findViewById(R.id.image_3);
                    imageView.setImageBitmap(bitmap);
                } else if (requestCode == Constants.IMAGE_4) {
                    imageURI.put("file4", file);
                    ImageView imageView = (ImageView) v.findViewById(R.id.image_4);
                    imageView.setImageBitmap(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //class for async request to post data
    private class PostData extends AsyncTask<HashMap<String, String>, Void, String> {

        private Context mContext;

        public PostData(Context context) {
            mContext = context;
        }
        static final String TAG = "PostData";
        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            Log.d(TAG, "Starting asynctask...");

            String charset = "UTF-8";
            String requestURL = Constants.API_URL + Constants.URL_ADDUSER;

            try {

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField(Constants.API_NAME, params[0].get(Constants.API_NAME));
                multipart.addFormField(Constants.API_DOB, params[0].get(Constants.API_DOB));
                multipart.addFormField(Constants.API_EMAIL, params[0].get(Constants.API_EMAIL));
                multipart.addFormField(Constants.API_MENTOR, params[0].get(Constants.API_MENTOR));
                multipart.addFormField(Constants.API_LEARN, params[0].get(Constants.API_LEARN));
                multipart.addFormField(Constants.API_ABOUT, params[0].get(Constants.API_ABOUT));
                for (String s : imageURI.keySet()) {
                    multipart.addFilePart(s, imageURI.get(s));
                }


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
                SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREF_FILENAME, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor prefedit = prefs.edit();

                try {
                    Log.d(TAG, result);
                    //parse json data and store userid
                    JSONObject obj = new JSONObject(result);
                    if (obj.getInt(Constants.API_RESPONSECODE) == 200) {
                        prefedit.putInt(Constants.PREF_USERID, obj.getInt(Constants.API_USERID));
                        prefedit.commit();
                        Toast.makeText(mContext, "Registration successful!", Toast.LENGTH_SHORT).show();

                        //start next activity
                        Intent i = new Intent(getActivity().getApplicationContext(), MatchesActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        Toast.makeText(mContext, "Error in creating new account.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error in creating new account." + obj.getString(Constants.API_RESPONSEMSG));
                    }
                } catch (Exception ex) {
                    Toast.makeText(mContext, "Error in parsing json data.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error in parsing json data" + ex.getMessage());
                }
            } else {
                Toast.makeText(mContext, "Error posting data to server.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error in posting data to server");
            }
        }
    }
}
