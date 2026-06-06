package org.htw.prog2.aufgabe1;

import org.htw.prog2.aufgabe1.exceptions.FileFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class SeqFile {
    private HashSet<String> seqs = new HashSet<>();
    private String firstSeq = "";

    public SeqFile(String filename) throws IOException, FileFormatException {
        readFile(filename);
    }

    /**
     * Reads the specified FASTA file.
     * @param filename The path to the FASTA file
     * @return void
     */
    private void readFile(String filename) throws IOException, FileFormatException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            StringBuilder seq = new StringBuilder();
            boolean hasHeader = false;
            boolean waitingForSequence = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith(">")) {
                    if (!hasHeader) {
                        hasHeader = true;
                    } else if (waitingForSequence) {
                        throw new FileFormatException("Two header lines are directly following each other.");
                    } else {
                        addSequence(seq);
                        seq = new StringBuilder();
                    }

                    waitingForSequence = true;
                } else {
                    if (!hasHeader) {
                        throw new FileFormatException("FASTA File does not start with sequence header line.");
                    }

                    seq.append(line);
                    waitingForSequence = false;
                }
            }

            if (!hasHeader) {
                throw new FileFormatException("FASTA File does not start with sequence header line.");
            }

            if (waitingForSequence) {
                throw new FileFormatException("The last line is a sequence header.");
            }

            addSequence(seq);
        }
    }

    /**
     * Adds the sequence in the passed StringBuilder to the internal list and also sets the first sequence if it
     * is still empty.
     * @param seq SequenceBuilder to get the sequence from.
     * @return The length of the added sequence.
     */
    private int addSequence(StringBuilder seq) {
        String sequence = seq.toString();

        if (!sequence.isEmpty()) {
            seqs.add(sequence);

            if (firstSeq.isEmpty()) {
                firstSeq = sequence;
            }
        }

        return sequence.length();
    }

    public int getNumberOfSequences() {
        return seqs.size();
    }

    public HashSet<String> getSequences() {
        return seqs;
    }

    public String getFirstSequence() {
        return firstSeq;
    }
}