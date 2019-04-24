package com.example.aileen.recipes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RecipeViewHolder extends RecyclerView.ViewHolder {
    private TextView recipename;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        recipename = itemView.findViewById(R.id.recipeTextView);
    }

    public void setRecipeName(String name){
        recipename.setText(name);
    }
}
