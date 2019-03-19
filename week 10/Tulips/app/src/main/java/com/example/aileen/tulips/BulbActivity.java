package com.example.aileen.tulips;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class BulbActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulb);

        //get bulb id from the intent
        int bulbnum = (Integer)getIntent().getExtras().get("bulb_id");
        Bulb bulb = Bulb.tulips.get(bulbnum);

        //populate image
        ImageView bulbImage = (ImageView)findViewById(R.id.bulbImageView);
        bulbImage.setImageResource(bulb.getImageResourceID());

        //populate name
        TextView bulbName = (TextView)findViewById(R.id.bulb_name);
        bulbName.setText(bulb.getName());

    }
}
