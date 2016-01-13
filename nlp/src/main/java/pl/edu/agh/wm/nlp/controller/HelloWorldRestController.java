package pl.edu.agh.wm.nlp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.wm.nlp.model.DoubleLetter;
import pl.edu.agh.wm.nlp.model.SingleLetter;
import pl.edu.agh.wm.nlp.service.LetterService;

import java.util.Map;

@RestController
@RequestMapping("/probability")
public class HelloWorldRestController {

	@Autowired
	LetterService letterService;  //Service which will do all data retrieval/manipulation work

	//-------------------Retrieve all probabilities--------------------------------------------------------

	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public ResponseEntity<Map<Character, Double>> listAllUsers(@RequestBody SingleLetter l1) {
		Map<Character, Double> currentLetterProbabilities = letterService.getProbabilitiesForLetter(l1.getL1().charAt(0));
		return new ResponseEntity<>(currentLetterProbabilities, HttpStatus.OK);
	}


	
	//------------------- Add probability --------------------------------------------------------

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity updateUser(@RequestBody DoubleLetter l) {
		letterService.addLetterOccurrence(l.getL1().charAt(0), l.getL2().charAt(0));
		return new ResponseEntity(HttpStatus.OK);
	}


}
