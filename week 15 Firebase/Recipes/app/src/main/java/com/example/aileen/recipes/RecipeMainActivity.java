package com.example.aileen.recipes;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class RecipeMainActivity extends AppCompatActivity {
    //array list of recipes
    List<Recipe> recipes = new ArrayList<>();

    //Firebase database recipe node reference
    DatabaseReference reciperef = FirebaseDatabase.getInstance().getReference("recipes");

    //define an adapter
    FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //define a query
        Query query = FirebaseDatabase.getInstance().getReference().child("recipes");

        //define a parser
        SnapshotParser<Recipe> parser = new SnapshotParser<Recipe>() {
            @NonNull
            @Override
            public Recipe parseSnapshot(@NonNull DataSnapshot snapshot) {
                Recipe newRecipe = new Recipe(snapshot.getKey(),
                        snapshot.child("name").getValue().toString(),
                        snapshot.child("url").getValue().toString());
                recipes.add(newRecipe);
                return newRecipe;
            }
        };

        //define adapter options
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query, parser)
                .build();

        //create an adapter
        adapter = new FirebaseRecyclerAdapter<Recipe, RecipeViewHolder>(options) {

            @NonNull
            @Override
            public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
                return new RecipeViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position, @NonNull final Recipe model) {
                holder.setRecipeName(model.getName());

                //long click listener
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });

                //onclick listener
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get recipe that was pressed
                        int position = holder.getAdapterPosition();
                        //get recipe id
                        String recipeURL = recipes.get(position).geturl();
                        //create new intent
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        //add url to intent
                        intent.setData(Uri.parse(recipeURL));
                        //start intent
                        startActivity(intent);
                    }
                });

                //context menu
                holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
                        //set the menu title
                        menu.setHeaderTitle("Delete " + model.getName());
                        //add the choices to the menu
                        menu.add(1, 1, 1, "Yes").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                //get recipe that was pressed
                                int position = holder.getAdapterPosition();
                                //get recipe id
                                String recipeid = recipes.get(position).getId();
                                //delete from Firebase
                                reciperef.child(recipeid).removeValue();
                                Snackbar.make(v, "Item removed", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                return false;
                            }
                        });
                        menu.add(2, 2, 2, "No");
                    }
                });
            }
        };

        // get the recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //divider line between rows
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //assign the adapter to the recycle view
        recyclerView.setAdapter(adapter);

        //set a layout manager on the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //add items
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a vertical linear layout to hold edit texts
                LinearLayout layout = new LinearLayout(RecipeMainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                //create edit texts and add to layout
                final EditText nameEditText = new EditText(RecipeMainActivity.this);
                nameEditText.setHint("Recipe name");
                layout.addView(nameEditText);
                final EditText urlEditText = new EditText(RecipeMainActivity.this);
                urlEditText.setHint("URL");
                layout.addView(urlEditText);

                //create alert dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(RecipeMainActivity.this);
                dialog.setTitle("Add Recipe");
                dialog.setView(layout);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get entered data
                        String recipeName = nameEditText.getText().toString();
                        String recipeURL = urlEditText.getText().toString();
                        if (recipeName.trim().length() > 0) {
                            //get new id from firebase
                            String key = reciperef.push().getKey();
                            //create new recipe item
                            Recipe newRecipe = new Recipe(key, recipeName, recipeURL);
                            //add to Firebase
                            reciperef.child(key).child("name").setValue(newRecipe.getName());
                            reciperef.child(key).child("url").setValue(newRecipe.geturl());
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
