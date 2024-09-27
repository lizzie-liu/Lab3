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
    //private List<Map<String, String>> countryData = new ArrayList<>();
    private Map<String, List<String>> countryLanguages = new HashMap<>();

    private Map<String, Map<String, String>> translations = new HashMap<>();
    private Map<String, Map<String, String>> countryData = new HashMap<>();

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

            for (int i = 0; i < data.length(); i++) {   //loop through each JSONobject
                JSONObject line = data.getJSONObject(i);
                // Extract languages and store translations
                Map<String, String> countryTranslations = new HashMap<>();

                for (String key : line.keySet()) {
                    if (!key.equals("alpha2") && !key.equals("alpha3") && !key.equals("id")) {
                        countryTranslations.put(key, line.getString(key));
                    }
                }
                this.countryData.put(line.getString("alpha3"), countryTranslations);
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
        if (countryData.containsKey(country)) {
            //avoid aliasing
            return new ArrayList<>(countryData.get(country).keySet());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getCountries() {
        // TODO Task: return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        return new ArrayList<>(countryData.keySet());
    }

    @Override
    public String translate(String country, String language) {
        // TODO Task: complete this method using your instance variables as needed
        Map<String, String> countryTranslations = countryData.get(country);
        if (countryTranslations != null && countryTranslations.containsKey(language)) {
            return countryTranslations.get(language);
        } else {
            return "Translate not found";
        }
    }
}
