package com.example.aileen.recipes;

/**
 * Created by aileen
 */

public class Recipe {
    private String id;
    private String name;
    private String url;

    public Recipe(){
        // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    }

    public Recipe(String newid, String newName, String newURL){
        id = newid;
        name = newName;
        url = newURL;
    }

    public String getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String geturl(){
        return url;
    }
}
