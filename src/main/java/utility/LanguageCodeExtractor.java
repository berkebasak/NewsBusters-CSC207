package utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LanguageCodeExtractor {

    private final ArrayList<String> languages = new ArrayList<String>();

    /**
     * Default constructor that loads the language codes from "language-codes.txt"
     * in the resources folder.
     */
    public LanguageCodeExtractor() {
        this("language-codes.txt");
    }

    /**
     * Overloaded constructor that allows us to specify the filename to load the language code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resources file can't be loaded properly
     */
    public LanguageCodeExtractor(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            Iterator<String> iterator = lines.iterator();
            iterator.next(); // skip header
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    String languageName = parts[0].trim();
                    languages.add(languageName);
                }
            }
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** Return all language names (for use in GUI dropdown). */
    public List<String> getAllLanguageNames() {
        return new ArrayList<>(languages);
    }
}
