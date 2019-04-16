package com.example.aileen.social;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MainActivity extends AppCompatActivity {

    private EditText emailText;
    private TextView responseView;
    private TextView nameView;
    private ProgressBar progressBar;
    private ImageView photoImageView;
    private static final String API_KEY = "8ea9fa44fe430665";
    private static final String API_URL = "https://api.fullcontact.com/v2/person.json?";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseView = findViewById(R.id.responseView);
        nameView = findViewById(R.id.nameView);
        emailText = findViewById(R.id.emailText);
        progressBar = findViewById(R.id.progressBar);
        photoImageView = findViewById(R.id.imageView);

        Button queryButton = findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                requestData(email);
            }
        });
    }

    private void requestData(String email){
        progressBar.setVisibility(View.VISIBLE);
        responseView.setText("");
        nameView.setText("");
        photoImageView.setImageResource(android.R.color.transparent);

        queue = Volley.newRequestQueue(this);
        //create URL for request
        String url = API_URL + "email=" + email + "&apiKey=" + API_KEY;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.getMessage(), error);
            }
        });

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }

    private void parseJSON(String response){
        //response should be a String with JSON objects
        if (response == null) {
            response = "THERE WAS AN ERROR";
        }
        progressBar.setVisibility(View.GONE);

        //parse JSON object
        try {
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
            JSONObject contact = object.getJSONObject("contactInfo");
            String name = contact.getString("fullName");
            nameView.setText(name);

            JSONArray photoarray = object.getJSONArray("photos");
            JSONObject photos = photoarray.getJSONObject(0);
            String imageURL = photos.getString("url");
            requestImage(imageURL);

            JSONArray socialprofiles = object.getJSONArray("socialProfiles");
            for (int i = 0; i < socialprofiles.length(); i++) {
                JSONObject socialprofile = socialprofiles.getJSONObject(i);
                String social = socialprofile.getString("type");
                String url = socialprofile.getString("url");
                responseView.append(social + " \t" + url + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestImage(String imageURL){
        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(imageURL, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        Log.d("IMAGE", "in onresponse");
                        photoImageView.setImageBitmap(response);
                    }
                },
                0,
                0,
                null,
                null,
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.getMessage(), error);
                    }
                }
        );

        // Add ImageRequest to the RequestQueue
        queue.add(imageRequest);
    }
}
