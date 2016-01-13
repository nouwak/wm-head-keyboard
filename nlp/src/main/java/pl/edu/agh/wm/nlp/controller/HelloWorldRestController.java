package pl.edu.agh.wm.nlp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.wm.nlp.service.LetterService;

import java.util.Map;

@RestController
@RequestMapping("/probability")
public class HelloWorldRestController {

	@Autowired
	LetterService letterService;  //Service which will do all data retrieval/manipulation work

	//-------------------Retrieve all probabilities--------------------------------------------------------

	@RequestMapping(value = "/get/{l}", method = RequestMethod.GET)
	public ResponseEntity<Map<Character, Double>> listAllUsers(@PathVariable("l") char l) {
		Map<Character, Double> currentLetterProbabilities = letterService.getProbabilitiesForLetter(l);
		return new ResponseEntity<>(currentLetterProbabilities, HttpStatus.OK);
	}


	
	//------------------- Add probability --------------------------------------------------------

	@RequestMapping(value = "/add/{l1}/{l2}", method = RequestMethod.POST)
	public ResponseEntity updateUser(@PathVariable("l1") char l1, @PathVariable("l2") char l2) {
		letterService.addLetterOccurrence(l1, l2);
		return new ResponseEntity(HttpStatus.OK);
	}


}
