package com.example.aileen.tulips;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class BulbActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulb);

        //get toolbar and set it as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu to add items to the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get the ID of the item on the action bar that was clicked
        switch (item.getItemId()){
            case R.id.create_order:
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
