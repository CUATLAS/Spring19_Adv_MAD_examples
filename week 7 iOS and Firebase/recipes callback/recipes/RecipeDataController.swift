//
//  RecipeDataController.swift
//  recipes
//
//  Created by Aileen Pierce
//  Copyright Aileen Pierce. All rights reserved.
//

import Foundation
import Firebase

class RecipeDataController {
    var ref: DatabaseReference!
    var recipeData = [Recipe]()
    //property with a closure as its value
    //closure takes an array of Recipe as its parameter and Void as its return type
    var onDataUpdate: ((_ data: [Recipe]) -> Void)?
    
    func setupFirebaseListener(){
        ref = Database.database().reference().child("recipes")
        //set up a listener for Firebase data change events
        //this event will fire with the initial data and then all data changes
        ref.observe(DataEventType.value, with: {snapshot in
            self.recipeData.removeAll()
            //DataSnapshot represents the Firebase data at a given time
            //loop through all the child data nodes
            for snap in snapshot.children.allObjects as! [DataSnapshot]{
                //print(snap)
                let recipeID = snap.key
                if let recipeDict = snap.value as? [String: String], //get value as a Dictionary
                    let recipeName = recipeDict["name"],
                    let recipeURL = recipeDict["url"]
                {
                    let newRecipe = Recipe(id: recipeID, name: recipeName, url: recipeURL)
                    //add recipe to recipes array
                    self.recipeData.append(newRecipe)
                    //print(recipeValue)
                }
            }
            //passing the results to the onDataUpdate closure
            self.onDataUpdate?(self.recipeData)
        })
    }
    
    func getRecipes()->[Recipe]{
        return recipeData
    }
    
    func deleteRecipe(recipeID: String){
        // Delete the object from Firebase
        ref.child(recipeID).removeValue()
    }
    
    func addRecipe(name:String, url:String){
        //create Dictionary
        let newRecipeDict = ["name": name, "url": url]
        
        //create a new ID
        let reciperef = ref.childByAutoId()
        
        //write data to the new ID in Firebase
        reciperef.setValue(newRecipeDict)
    }
    
}
