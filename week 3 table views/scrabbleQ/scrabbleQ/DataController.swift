//
//  QnoU.swift
//  scrabbleQ
//
//  Created by Aileen Pierce on 1/29/19.
//  Copyright © 2019 Aileen Pierce. All rights reserved.
//

import Foundation

class DataController {
    var qnouWords = [String]()
    let fileName = "qwordswithoutu1"
    
    func loadData(){
        // URL for our plist
        if let pathURL = Bundle.main.url(forResource: fileName, withExtension: "plist"){
            //creates a property list decoder object
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                //decodes the property list
                qnouWords = try plistdecoder.decode([String].self, from: data)
            } catch {
                // handle error
                print(error)
            }
        }
    }
    
    func getWords()->[String]{
        return qnouWords
    }
}
