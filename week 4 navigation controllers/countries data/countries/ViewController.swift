//
//  ViewController.swift
//  countries
//
//  Created by Aileen Pierce
//  Copyright © 2018 Aileen Pierce. All rights reserved.
//

import UIKit

class ViewController: UITableViewController {
    var continentList = [String]()
    var continentsData = ContinentsDataModelController()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        continentsData.loadData()
        continentList=continentsData.getContinents()
        
        //enables large titles
        navigationController?.navigationBar.prefersLargeTitles = true
        
        //application instance
        let app = UIApplication.shared
        //subscribe to the UIApplicationWillResignActiveNotification notification
        NotificationCenter.default.addObserver(self, selector: #selector(ViewController.applicationWillResignActive(_:)), name: UIApplication.willResignActiveNotification, object: app)
    }
    
    //Required methods for UITableViewDataSource
    //Number of rows in the section
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return continentList.count
    }
    
    // Displays table view cells
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //configure the cell
        let cell = tableView.dequeueReusableCell(withIdentifier: "CountryIdentifier", for: indexPath)
        cell.textLabel?.text = continentList[indexPath.row]
        return cell
    }
    
    //Handles segues to other view controllers
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "countrysegue" {
            let detailVC = segue.destination as! DetailViewController
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            //sets the data for the destination controller
            detailVC.title = continentList[indexPath.row]
            detailVC.continentsData = continentsData
            detailVC.selectedContinent = indexPath.row
        } //for detail disclosure 
        else if segue.identifier == "continentsegue"{
            let infoVC = segue.destination as! ContinentInfoTableViewController
            let editingCell = sender as! UITableViewCell
            let indexPath = tableView.indexPath(for: editingCell)
            infoVC.name = continentList[indexPath!.row]
            let countryList = continentsData.getCountries(index: (indexPath?.row)!)
            infoVC.number = String(countryList.count)
        }
    }
    
    //called when the UIApplicationWillResignActiveNotification notification is posted
    //all notification methods take a single NSNotification instance as their argument
    @objc func applicationWillResignActive(_ notification: NSNotification){
        continentsData.writeData()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

