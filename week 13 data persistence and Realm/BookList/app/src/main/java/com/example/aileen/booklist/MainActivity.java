package com.example.aileen.booklist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private Realm realm;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get realm instance
        realm = Realm.getDefaultInstance();

        //get all saved Book objects
        RealmResults<Book> books = realm.where(Book.class).findAll();

        bookAdapter = new BookAdapter(books, this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //assign the adapter to the recycle view
        recyclerView.setAdapter(bookAdapter);

        //set a layout manager on the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //divider line between rows
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //Add items
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a vertical linear layout to hold edit texts
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                //create edit texts and add to layout
                final EditText bookEditText = new EditText(MainActivity.this);
                bookEditText.setHint("Book name");
                layout.addView(bookEditText);
                final EditText authorEditText = new EditText(MainActivity.this);
                authorEditText.setHint("Author name");
                layout.addView(authorEditText);

                //create alert dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Add Book");
                dialog.setView(layout);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get book name entered
                        final String newBookName = bookEditText.getText().toString();
                        final String newAuthorName = authorEditText.getText().toString();
                        if (!newBookName.isEmpty()) {
                            addBook(UUID.randomUUID().toString(), newBookName, newAuthorName);
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });
    }

    public void addBook(final String newId, final String newBook, final String newAuthor){
        //start realm write transaction
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //create a realm object
                Book newbook = realm.createObject(Book.class, newId);
                newbook.setBook_name(newBook);
                newbook.setAuthor_name(newAuthor);
            }
        });
    }

    public void changeBookRead(final String bookId) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book book = realm.where(Book.class).equalTo("id", bookId).findFirst();
                book.setRead(!book.getRead());
            }
        });
    }

    public void editBook(final String bookId){
        //create a vertical linear layout to hold edit texts
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Book book = realm.where(Book.class).equalTo("id", bookId).findFirst();

        //create edit texts and add to layout
        final EditText bookEditText = new EditText(MainActivity.this);
        bookEditText.setText(book.getBook_name());
        layout.addView(bookEditText);
        final EditText authorEditText = new EditText(MainActivity.this);
        authorEditText.setText(book.getAuthor_name());
        layout.addView(authorEditText);

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Edit Book");
        dialog.setView(layout);
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //save edited book
                //get updated book and author names
                final String updatedBookName = bookEditText.getText().toString();
                final String updatedAuthorName = authorEditText.getText().toString();
                if(!updatedBookName.isEmpty()) {
                    changeBook(bookId, updatedBookName, updatedAuthorName);
                }
            }
        });
        dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //delete book
                deleteBook(bookId);
            }
        });
        dialog.create();
        dialog.show();

    }

    private void changeBook(final String bookId, final String book_name, final String author_name) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book book = realm.where(Book.class).equalTo("id", bookId).findFirst();
                book.setBook_name(book_name);
                book.setAuthor_name(author_name);
            }
        });
    }

    private void deleteBook(final String bookId) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Book.class).equalTo("id", bookId)
                        .findFirst()
                        .deleteFromRealm();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //close the Realm instance when the Activity exits
        realm.close();
    }

}
