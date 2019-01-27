//
//  ArtistAlbumsController.swift
//  music
//
//  Created by Aileen Pierce on 1/25/19.
//  Copyright © 2019 Aileen Pierce. All rights reserved.
//

import Foundation

class ArtistAlbumsController {
    var allData = [ArtistAlbums]()
    let fileName = "artistalbums2"
        
    func loadData(){
        if let pathURL = Bundle.main.url(forResource: fileName, withExtension: "plist"){
            //creates a property list decoder object
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                //decodes the property list
                allData = try plistdecoder.decode([ArtistAlbums].self, from: data)
            } catch {
                // handle error
                print(error)
            }
        }
    }
    
    func getArtists() -> [String]{
        var artists = [String]()
        for artist in allData{
            artists.append(artist.name)
        }
        return artists
    }
    
    func getAlbums(index:Int) -> [String] {
        return allData[index].albums
    }
}


