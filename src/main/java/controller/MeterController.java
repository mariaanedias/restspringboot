package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import json.entity.Profiles;

/*
Resource controller
Handle the requests
 */

@RestController
public class MeterController {
	
	//The @RequestMapping annotation ensures that HTTP requests to 
	// /profiles are mapped to the postProfiles() method.
	@RequestMapping(value = "/profiles", method = RequestMethod.POST)	 
	public ResponseEntity<Profiles> postProfiles(/*@RequestParam(value="paramValue", defaultValue="xxxx") String param1, */
			 @RequestBody Profiles profiles) {
		
/*
 * Rather than relying on a view technology to perform server-side rendering of the data to HTML, 
 * this RESTful web service controller simply populates and returns an object. 
 * The object data will be written directly to the HTTP response as JSON.
 */
		//Java 8 - Stream (Collections) and forEach
		profiles.getProfiles().stream().forEach(p -> System.out.println(p.getProfile())); //Print the profile in the console
		System.out.println("HEREa");
	    return new ResponseEntity<Profiles>(profiles, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/testGet", method = RequestMethod.GET)
	public ResponseEntity<String> getProfiles() {
		System.out.println("HEREb");
	    return new ResponseEntity<String>("GET OK", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/testPostParam", method = RequestMethod.POST)
	public ResponseEntity<String> getProfiles(@RequestParam(value="paramValue", defaultValue="xxxx") String param1) {
		System.out.println("HEREc");
		return new ResponseEntity<String>(param1, HttpStatus.OK);
	}
	
	// gradle bootRun
}