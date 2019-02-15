//
//  Character.swift
//  harrypotter
//
//  Created by Aileen Pierce on 2/13/19.
//  Copyright © 2019 Aileen Pierce. All rights reserved.
//

import Foundation

struct Character: Decodable{
    let name : String
    let url : String
}

class CharacterDataModelController{
    var allData = [Character]()
    let fileName = "harrypotter2"
    
    func loadData(){
        if let pathURL = Bundle.main.url(forResource: fileName, withExtension: "plist"){
            //creates a property list decoder object
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                //decodes the property list
                allData = try plistdecoder.decode([Character].self, from: data)
            } catch {
                // handle error
                print(error)
            }
        }
    }
    
    func getCharacters() -> [String]{
        var characters = [String]()
        for character in allData{
            characters.append(character.name)
        }
        return characters
    }
    
    func getURL(index:Int) -> String {
        return allData[index].url
    }
}
