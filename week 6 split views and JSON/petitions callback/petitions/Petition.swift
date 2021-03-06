//
//  Petition.swift
//  petitions
//
//  Created by Aileen Pierce
//  Copyright Aileen Pierce. All rights reserved.
//

import Foundation

struct Petition: Decodable{
    let title: String
    let signatureCount : Int
    let url : String
}

struct PetitionData: Decodable {
    var results = [Petition]()
}

