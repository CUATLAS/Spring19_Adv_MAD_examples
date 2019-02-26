//
//  Recipe.swift
//  recipes
//
//  Created by Aileen Pierce
//  Copyright Aileen Pierce. All rights reserved.
//

import Foundation
import Firebase

struct Recipe {
    var id : String
    var name: String
    var url: String

    init(id: String, name: String, url: String){
        self.id = id
        self.name = name
        self.url = url
    }
}
