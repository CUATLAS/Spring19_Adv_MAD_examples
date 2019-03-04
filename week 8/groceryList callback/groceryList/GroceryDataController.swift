//
//  GroceryDataController.swift
//  groceryList
//
//  Created by Aileen Pierce
//  Copyright Aileen Pierce. All rights reserved.
//

import Foundation
import RealmSwift

class GroceryDataController {
    var myRealm : Realm!  //Realm database instance
    var groceryData: Results<Grocery> //collection of Objects
    {
        get {
            return myRealm.objects(Grocery.self) //returns all Grocery objects from Realm
        }
    }
    //property with a closure as its value
    //closure takes an array of Grocery as its parameter and Void as its return type
    var onDataUpdate: ((_ data: [Grocery]) -> Void)?
    
    func dbSetup(){
        //initialize the Realm database
        do {
            myRealm = try Realm()
        } catch let error {
            print(error.localizedDescription)
        }
        onDataUpdate?(Array(groceryData)) //converts collection of Objects to an Array
        print(Realm.Configuration.defaultConfiguration.fileURL!) //prints location of Realm database
    }
    
    func getGroceries()->[Grocery]{
        return Array(groceryData)
    }
    
    func deleteItem(item: Grocery){
        try! self.myRealm.write {
            self.myRealm.delete(item) //delete from realm database
        }
        onDataUpdate?(Array(groceryData))
    }

    func addItem(newItem:Grocery){
        do {
            try self.myRealm.write({
                self.myRealm.add(newItem) //add to realm database
            })
        } catch let error{
            print(error.localizedDescription)
        }
        onDataUpdate?(Array(groceryData))
    }
    
    func boughtItem(item: Grocery){
        try! self.myRealm.write {
            item.bought = !item.bought
        }
        onDataUpdate?(Array(groceryData))
    }
    
}
