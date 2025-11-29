package utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CountryCodeExtractor {

    private final ArrayList<String> countries = new ArrayList<>();

    /**
     * Default constructor that loads the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeExtractor() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor that allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resources file can't be loaded properly
     */
    public CountryCodeExtractor(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            Iterator<String> iterator = lines.iterator();
            iterator.next(); // skip header
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] parts = line.split("\t");

                String countryName = parts[0].trim();

                countries.add(countryName);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** Return all country names (for use in GUI list). */
    public List<String> getAllCountryNames() {
        return new ArrayList<>(countries);
    }
}
