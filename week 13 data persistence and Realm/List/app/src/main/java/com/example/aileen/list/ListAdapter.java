package com.example.aileen.list;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder  {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    private List<Item> mItems;

    public ListAdapter(List<Item> items){
        mItems = items;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAdapter.ViewHolder viewHolder, int i) {
        final Item item = mItems.get(i);
        viewHolder.textView.setText(item.getName());

        //long click listener
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        //context menu
        viewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
                //set the menu title
                menu.setHeaderTitle("Delete " + item.getName());
                //add the choices to the menu
                menu.add(1, 1, 1, "Yes").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        removeItem(viewHolder.getAdapterPosition());
                        Snackbar.make(v, "Item removed", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return false;
                    }
                });
                menu.add(2, 2, 2, "No");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItem(String newItem){
        Item.myItems.add(new Item(newItem));
        notifyItemInserted(getItemCount());
    }

    public void removeItem(int itemPosition){
        Item.myItems.remove(itemPosition);
        notifyItemRemoved(itemPosition);
    }
}
