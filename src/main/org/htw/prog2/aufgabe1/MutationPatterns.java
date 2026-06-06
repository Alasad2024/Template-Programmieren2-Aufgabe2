package org.htw.prog2.aufgabe1;

import org.htw.prog2.aufgabe1.exceptions.FileFormatException;

import java.io.*;
import java.util.*;

public class MutationPatterns {

    private int numberOfMutations = 0;

    /**
     * Contructor für MutationPatterns. Liest die CSV-Datei infile ein.
     * @param infile Pfad zu CSV-Datei zum Einlesen
     * @throws IOException bei allgemeinen IO-Fehlern
     * @throws FileNotFoundException falls die Datei nicht gefunden wurde
     * @throws FileFormatException falls das Format der Definitionszeile inkorrekt ist oder die Anzahl der Spalten
     * nicht in jeder Zeile gleich ist
     */
    public MutationPatterns(String infile) throws IOException, FileNotFoundException, FileFormatException {
        try (BufferedReader reader = new BufferedReader(new FileReader(infile))) {
            String line;
            boolean headerFound = false;
            int numberOfColumns = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("#")) {
                    continue;
                }

                if (line.length() == 0) {
                    continue;
                }

                if (!headerFound) {
                    parseDrugs(line);
                    numberOfColumns = line.split(";").length;
                    headerFound = true;
                } else {
                    String[] elements = line.split(";");

                    if (elements.length != numberOfColumns) {
                        throw new FileFormatException("All lines in a CSV file must have the same number of elements");
                    }

                    numberOfMutations++;
                }
            }

            if (!headerFound) {
                throw new FileFormatException("First line of mutation pattern CSV file must be a header");
            }
        }
    }

    /**
     * Gibt die Anzahl der eingelesenen Mutationspattern zurück.
     * @return Anzahl der eingelesenen Mutationspattern
     */
    public int getNumberOfMutations() {
        return numberOfMutations;
    }

    /**
     * Parst die Definitionszeile.
     * @param line Definitionszeile aus der CSV-Datei
     * @return Liste der Medikamentennamen aus der Definitionszeile
     */
    public static List<String> parseDrugs(String line) throws FileFormatException {
        String[] elements = line.split(";");
        List<String> drugs = new LinkedList<>();

        if (elements.length < 3) {
            throw new FileFormatException("First line of mutation pattern CSV file must be a header");
        }

        String firstElement = cleanElement(elements[0]);
        String secondElement = cleanElement(elements[1]);

        if (!firstElement.equals("Mutation Patterns")) {
            throw new FileFormatException("First line of mutation pattern CSV file must be a header");
        }

        if (!secondElement.equals("Number of Sequences")) {
            throw new FileFormatException("First line of mutation pattern CSV file must be a header");
        }

        for (int i = 2; i < elements.length; i++) {
            String element = cleanElement(elements[i]);

            if (!element.endsWith(" foldn")) {
                throw new FileFormatException("First line of mutation pattern CSV file must be a header");
            }

            String drugName = element.substring(0, element.length() - " foldn".length());
            drugs.add(drugName);
        }

        return drugs;
    }

    private static String cleanElement(String element) {
        element = element.trim();

        if (element.startsWith("\"") && element.endsWith("\"") && element.length() >= 2) {
            element = element.substring(1, element.length() - 1);
        }

        return element;
    }
}