//
//  DataController.swift
//  scrabbleQgrouped
//
//  Created by Aileen Pierce on 1/29/19.
//  Copyright © 2019 Aileen Pierce. All rights reserved.
//

import Foundation

struct QnoU : Decodable {
    let letter : String
    let words : [String]
}

class DataController {
    var allData = [QnoU]()
    let fileName = "qwordswithoutu3"
    
    func loadData(){
        if let pathURL = Bundle.main.url(forResource: fileName, withExtension: "plist"){
            //creates a property list decoder object
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                //decodes the property list
                allData = try plistdecoder.decode([QnoU].self, from: data)
            } catch {
                // handle error
                print(error)
            }
        }
    }
    
    func getQnoUWords()->[QnoU]{
        return allData
    }
    
    func getLetters()->[String]{
        var letters = [String]()
        for firstLetter in allData{
            letters.append(firstLetter.letter)
        }
        // sorts the array
        letters.sort(by: {$0 < $1})
        return letters
    }
}
