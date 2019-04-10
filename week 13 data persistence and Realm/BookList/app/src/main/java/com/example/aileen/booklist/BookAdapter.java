package com.example.aileen.booklist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class BookAdapter extends RealmRecyclerViewAdapter<Book, BookAdapter.ViewHolder> {

    private MainActivity activity;

    public BookAdapter(RealmResults<Book> data, MainActivity activity){
        super(data, true);
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BookAdapter.ViewHolder viewHolder, int i) {
        Book book = getItem(i);
        viewHolder.bookName.setText(book.getBook_name());
        viewHolder.authorName.setText(book.getAuthor_name());
        viewHolder.hasRead.setChecked(book.getRead());
        viewHolder.hasRead.setTag(i);

        viewHolder.hasRead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //makes sure this is only called for the current checkbox
                //solves the problem of this being called too many times
                if (buttonView.isShown()){
                    int position = (Integer) viewHolder.hasRead.getTag();
                    Book book = getItem(position);
                    activity.changeBookRead(book.getId());
                }
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) viewHolder.hasRead.getTag();
                Book book = getItem(position);
                activity.editBook(book.getId());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookName;
        TextView authorName;
        CheckBox hasRead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookTextView);
            authorName = itemView.findViewById(R.id.authorTextView);
            hasRead = itemView.findViewById(R.id.checkBox);
        }
    }
}
