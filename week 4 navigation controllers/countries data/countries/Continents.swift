//
//  Continents.swift
//  countries
//
//  Created by Aileen Pierce
//  Copyright © 2018 Aileen Pierce. All rights reserved.
//

import Foundation

struct ContinentsDataModel : Codable {
    var continent : String
    var countries : [String]
}

class ContinentsDataModelController {
    var allData = [ContinentsDataModel]()
    let fileName = "continents2"
    let datafilename = "data.plist"
    
    func getDataFile(datafile: String) -> URL {
        //get path for data file
        let dirPath = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        let docDir = dirPath[0] //documents directory
        print(docDir)
        
        // URL for our plist
        return docDir.appendingPathComponent(datafile)
    }
    
    func loadData(){
        let pathURL:URL?
        
        // URL for our plist
        let dataFileURL = getDataFile(datafile: datafilename)
        print(dataFileURL)
        
        //if the data file exists, use it
        if FileManager.default.fileExists(atPath: dataFileURL.path){
            pathURL = dataFileURL
        }
        else {
            // URL for our plist
            pathURL = Bundle.main.url(forResource: fileName, withExtension: "plist")
        }
        
            //creates a property list decoder object
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL!)
                //decodes the property list
                allData = try plistdecoder.decode([ContinentsDataModel].self, from: data)
            } catch {
                // handle error
                print(error)
            }
    }
    
    func getContinents() -> [String]{
        var continents = [String]()
        for item in allData{
            continents.append(item.continent)
        }
        return continents
    }
    
    func getCountries(index:Int) -> [String] {
        return allData[index].countries
    }
    
    func addCountry(index:Int, newCountry:String, newIndex: Int){
        allData[index].countries.insert(newCountry, at: newIndex)
    }
    
    func deleteCountry(index:Int, countryIndex: Int){
        allData[index].countries.remove(at: countryIndex)
    }
    
    func writeData(){
        // URL for our plist
        let dataFileURL = getDataFile(datafile: datafilename)
        print(dataFileURL)
        //creates a property list decoder object
        let plistencoder = PropertyListEncoder()
        plistencoder.outputFormat = .xml
        do {
            let data = try plistencoder.encode(allData.self)
            try data.write(to: dataFileURL)
        } catch {
            // handle error
            print(error)
        }
    }
    
}
