package pl.edu.agh.wm.nlp.service;

import java.util.Map;


public interface LetterService {
	
	void addLetterOccurrence(Character firstLetter, Character secondLetter);

	Map<Character,Double> getProbabilitiesForLetter(Character a);
	
}
