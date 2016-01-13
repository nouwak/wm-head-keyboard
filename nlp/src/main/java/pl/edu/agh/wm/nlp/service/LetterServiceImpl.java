package pl.edu.agh.wm.nlp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service("letterService")
@Transactional
public class LetterServiceImpl implements LetterService {


    private static Map<Character, Map<Character, Integer>> letterOccurrences = new HashMap<>();

    static {
        initLetterStatistics();
    }

    @Override
    public void addLetterOccurrence(Character firstLetter, Character secondLetter) {
        firstLetter=substituteLetter(firstLetter);
        secondLetter=substituteLetter(secondLetter);
        addOccurenceToMap(firstLetter, secondLetter);
    }

    @Override
    public Map<Character, Double> getProbabilitiesForLetter(Character letter) {
        letter = substituteLetter(letter);
        if (letterOccurrences.containsKey(letter)) {
            Map<Character, Integer> toLetterOccurrences = letterOccurrences.get(letter);
            return transformToProbabilityMap(toLetterOccurrences);
        } else {
            return Collections.emptyMap();
        }
    }

    private Character substituteLetter(Character letter) {
        if ('_' == letter) {
            return ' ';
        }
        return letter;
    }


    private static void initLetterStatistics() {
        try {
            URL resourceUrl = LetterServiceImpl.class.getResource("/manhunt.txt");
            File resourceFile = new File(resourceUrl.toURI());
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(resourceFile), StandardCharsets.UTF_8));
            String line;

            while ((line = reader.readLine()) != null) {
                addOccurences(line);
            }
        } catch (URISyntaxException | IOException e) {
            createDefaultMap();
        }
    }

    private Map<Character, Double> transformToProbabilityMap(Map<Character, Integer> toLetterOccurrences) {
        Map<Character, Double> newMap = new HashMap<>();
        Collection<Integer> values = toLetterOccurrences.values();
        int allValues = 0;
        for (int value : values) {
            allValues += value;
        }

        for (Map.Entry<Character, Integer> entry : toLetterOccurrences.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue() / (double) allValues);
        }
        return newMap;
    }

    private static void createDefaultMap() {
        for (char start = 'a'; start <= 'z'; start++) {
            for (char end = 'a'; end <= 'z'; end++) {
                addOccurenceToMap(start, end);
            }
        }
    }

    private static void addOccurences(String line) {
        if (!line.startsWith("#")) {
            String upperLine = line.toUpperCase();
            for (int i = 0; i < upperLine.length() - 1; i++) {
                char c1 = upperLine.charAt(i);
                char c2 = upperLine.charAt(i + 1);
                addOccurenceToMap(c1, c2);
            }
        }
    }

    private static void addOccurenceToMap(Character l1, Character l2) {
        if (!lettersValid(l1, l2)) {
            return;
        }
        if (letterOccurrences.containsKey(l1)) {
            Map<Character, Integer> mapForLetter = letterOccurrences.get(l1);
            if (mapForLetter.containsKey(l2)) {
                Integer occurrences = mapForLetter.get(l2);
                mapForLetter.put(l2, occurrences + 1);
            } else {
                mapForLetter.put(l2, 1);
            }
        } else {
            Map<Character, Integer> mapForLetter = new HashMap<>();
            mapForLetter.put(l2, 1);
            letterOccurrences.put(l1, mapForLetter);
        }
    }

    private static boolean lettersValid(Character l1, Character l2) {
        return letterValid(l1) && letterValid(l2);
    }

    private static boolean letterValid(Character l1) {
        return Character.isLetter(l1) || l1.equals(' ');
    }

}
