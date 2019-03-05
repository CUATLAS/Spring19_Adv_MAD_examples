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
    var myRealm1 : Realm!  //Realm database instance
    var groceryData: Results<Grocery> //collection of Objects
    {
        get {
            return myRealm1.objects(Grocery.self) //returns all Grocery objects from Realm
        }
    }
    
    func dbSetup(){
        //initialize the Realm database
        do {
            myRealm1 = try Realm()
        } catch let error {
            print(error.localizedDescription)
        }
        print(Realm.Configuration.defaultConfiguration.fileURL!) //prints location of Realm database
    }
    
    func getGroceries()->[Grocery]{
        return Array(groceryData)
    }
    
    func addItem(newItem:Grocery){
        do {
            try self.myRealm1.write({
                self.myRealm1.add(newItem) //add to realm database
            })
        } catch let error{
            print(error.localizedDescription)
        }
    }
    
    func boughtItem(item: Grocery){
        do {
            try self.myRealm1.write ({
                item.bought = !item.bought
            })
        }catch let error{
            print(error.localizedDescription)
        }
    }
    
    func deleteItem(item: Grocery){
        do {
            try self.myRealm1.write ({
                self.myRealm1.delete(item) //delete from realm database
            })
        } catch let error{
            print(error.localizedDescription)
        }
    }
}
