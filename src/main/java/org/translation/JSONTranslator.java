package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // TODO Task: pick appropriate instance variables for this class
    private List<Map<String, String>> countryData = new ArrayList<>();
    private Map<String, List<String>> countryLanguages = new HashMap<>();
    private Map<String, Map<String, String>> translations = new HashMap<>();
    // [{<alpha2: code>, <alpha3: code>}]

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {
            // TODO Task: use the data in the jsonArray to populate your instance variables
            //            Note: this will likely be one of the most substantial pieces of code you write in this lab.

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONArray data = new JSONArray(jsonString);

            for (int i = 0; i < data.length(); i++) {
                JSONObject line = data.getJSONObject(i);

                // Get the country codes and store them
                Map<String, String> countryCodes = new HashMap<>();
                String alpha2 = line.getString("alpha2");
                String alpha3 = line.getString("alpha3");
                countryCodes.put("alpha2", alpha2);
                countryCodes.put("alpha3", alpha3);
                this.countryData.add(countryCodes);

                // Extract languages and store translations
                List<String> languages = new ArrayList<>();
                Map<String, String> countryTranslations = new HashMap<>();

                for (String key : line.keySet()) {
                    if (key.length() == 2 && !key.equals("alpha2") && !key.equals("alpha3")) {
                       //how do I add key???????
                        languages.add(key);
                        countryTranslations.put(key, line.getString(key));
                    }
                }
                // Map country to languagelist and for each country
                this.countryLanguages.put(alpha3, languages);
                this.translations.put(alpha3, countryTranslations);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // TODO Task: return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        if (countryLanguages.containsKey(country)) {
            //avoid alising
            return new ArrayList<>(countryLanguages.get(country));
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getCountries() {
        // TODO Task: return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        List<String> countries = new ArrayList<>();
        for (Map<String, String> countryCodeMap : countryData) {
            // Get the alpha3 for each country
            countries.add(countryCodeMap.get("alpha3"));
        }
        return countries;
    }

    @Override
    public String translate(String country, String language) {
        // TODO Task: complete this method using your instance variables as needed
        if (translations.containsKey(country)) {
            Map<String, String> countryTranslations = translations.get(country);

            // Check if the requested language translation exists
            if (countryTranslations.containsKey(language)) {
                return countryTranslations.get(language);
            } else {
                return "Translate not found";
            }
        } else {
            return "Translate not found";
        }
    }
}
