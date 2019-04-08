//
//  PetitionDataController.swift
//  petitions
//
//  Created by Aileen Pierce
//  Copyright Aileen Pierce. All rights reserved.
//

import Foundation

class PetitionDataController {
    var petitionData = PetitionData()
    //property with a closure as its value
    //closure takes an array of Petition as its parameter and Void as its return type
    var onDataUpdate: ((_ data: [Petition]) -> Void)?
    
    func loadjson(){
        let urlPath = "https://api.whitehouse.gov/v1/petitions.json?limit=50"
        guard let url = URL(string: urlPath)
            else {
                print("url error")
                return
        }
        
        let session = URLSession.shared.dataTask(with: url, completionHandler: {(data, response, error) in
            let httpResponse = response as! HTTPURLResponse
            let statusCode = httpResponse.statusCode
            guard statusCode == 200
                else {
                    print("file download error")
                    return
            }
            //download successful
            print("download complete")
            //parse json asynchronously
            DispatchQueue.main.async {self.parsejson(data!)}
        })
        //must call resume to run session
        session.resume()
    }
    
    func parsejson(_ data: Data){
        do
        {
            let api = try JSONDecoder().decode(PetitionData.self, from: data)
            //print(api)
            for petition in api.results
            {
                petitionData.results.append(petition)
            }
        }
        catch let jsonErr
        {
            print("json error")
            print(jsonErr.localizedDescription)
            return
        }
        print("parsejson done")
        //passing the results to the onDataUpdate closure
        onDataUpdate?(petitionData.results)
    }
    
    func getPetitions()->[Petition]{
        return petitionData.results
    }

}

