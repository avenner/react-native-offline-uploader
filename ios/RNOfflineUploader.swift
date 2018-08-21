//
//  RNOfflineUploader.swift
//  RNOfflineUploader
//
//  Created by Alexis Venner on 21/08/2018.
//

import Foundation

@objc(RNOfflineUploader)
class RNOfflineUploader: NSObject {
    
    @objc(startUpload:resolve:reject:)
    func startUpload(options: NSDictionary, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
        let sessionIdentifier = 0;
        
        let url = URL(string: options["url"])
        let data = options["data"]
        
        
        let sessionConfiguration = URLSessionConfiguration.background(withIdentifier: sessionIdentifier);
        if #available(iOS 11.0, *) {
            sessionConfiguration.waitsForConnectivity = true
        }
        
        let session = URLSession(configuration: sessionConfiguration, delegate: self.delegate,delegateQueue: nil)
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        
        session.downloadTask(withRequest: request)
        session.resume();
    }
    
}
