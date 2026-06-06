package org.htw.prog2.aufgabe1;

import org.apache.commons.cli.*;

public class HIVDiagnostics {

    /**
     * Parst die Kommandozeilenargumente. Gibt null zurück, falls ein Fehler beim Parsen aufgetreten ist (z.B. eins
     * der erforderlichen Argumente nicht angegeben wurde)
     * @param args Array mit Kommandozeilen-Argumenten
     * @return CommandLine-Objekt mit geparsten Optionen
     */

    public static CommandLine parseOptions(String[] args) {
        Options options = new Options();

        options.addOption(Option.builder("m")
                .hasArg(true)
                .longOpt("mutationfile")
                .required(true)
                .desc("CSV file with mutation patterns.")
                .build());

        options.addOption(Option.builder("d")
                .hasArg(true)
                .longOpt("drugname")
                .required(true)
                .desc("Drug name.")
                .build());

        options.addOption(Option.builder("r")
                .hasArg(true)
                .longOpt("reference")
                .required(true)
                .desc("Reference sequence FASTA file.")
                .build());

        options.addOption(Option.builder("p")
                .hasArg(true)
                .longOpt("patientseqs")
                .required(true)
                .desc("FASTA file with patient sequences.")
                .build());

        CommandLineParser parser = new DefaultParser();

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Error: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("HIVDiagnostics", options);
            return null;
        }
    }

    public static void main(String[] args) {
        CommandLine cli = parseOptions(args);

        if (cli == null) {
            return;
        }

        try {
            MutationPatterns patterns = new MutationPatterns(cli.getOptionValue("m"));
            SeqFile reference = new SeqFile(cli.getOptionValue("r"));
            SeqFile patientSeqs = new SeqFile(cli.getOptionValue("p"));

            System.out.print("Eingelesene Mutationen: "
                    + patterns.getNumberOfMutations() + "\n");

            System.out.print("Länge der eingelesenen Referenzsequenz: "
                    + reference.getFirstSequence().length()
                    + " Aminosäuren\n");

            System.out.print("Anzahl der eingelesenen Patientensequenzen: "
                    + patientSeqs.getNumberOfSequences() + "\n");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}