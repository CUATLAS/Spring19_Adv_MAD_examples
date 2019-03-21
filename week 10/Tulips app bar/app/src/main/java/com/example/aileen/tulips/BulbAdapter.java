package com.example.aileen.tulips;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BulbAdapter extends RecyclerView.Adapter<BulbAdapter.ViewHolder>{
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bulbTextView;

        //constructor method
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bulbTextView = itemView.findViewById(R.id.textView);
            bulbTextView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            itemClickListener.onListItemClick(getAdapterPosition());
        }
    }

    private List<Bulb> mBulbs;

    public BulbAdapter(List<Bulb> bulbs, ListItemClickListener bulbClickListener){
        mBulbs = bulbs;
        itemClickListener = bulbClickListener;
    }

    @NonNull
    @Override
    public BulbAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View bulbView = inflater.inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(bulbView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BulbAdapter.ViewHolder viewHolder, int i) {
        Bulb bulb = mBulbs.get(i);
        viewHolder.bulbTextView.setText(bulb.getName());
    }

    @Override
    public int getItemCount() {
        return mBulbs.size();
    }

    public interface ListItemClickListener{
        void onListItemClick(int position);
    }

    ListItemClickListener itemClickListener;

}
