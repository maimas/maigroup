package com.mg.drools.businessrules.base;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RuleHeaders {

    private HashMap<String, Object> headers = new HashMap<>();
    private List<String> errors = new ArrayList<>();

    /**
     * Check if the headers contains specific header;
     *
     * @param key - header name
     * @return true if found, else returns false
     */
    public boolean has(String key) {
        return headers.containsKey(key);
    }

    /**
     * Puts the key <-> value pair in the headers
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        headers.put(key, value);
    }


    /**
     * Gets the value for the specific header key
     *
     * @param key - header key
     * @return object for the prided key if the key is present, else returns null
     */
    public Object get(String key) {
        return headers.get(key);
    }

    /**
     * Adds an error in the errors headers.
     *
     * @param error - error message
     */
    public void addError(String error) {
        errors.add(error);
    }

    /**
     * Get the error list
     *
     * @return - error list from the errors header
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Get the errors as a string.
     *
     * @return - returns the string value of the errors list
     */
    public String getErrorsAsString() {
        StringBuilder sb = new StringBuilder();
        if (hasErrors()) {
            getErrors().forEach(err -> sb.append(err).append("\n"));
        }
        return sb.toString();
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((k, v) -> {
            sb.append(k);
            sb.append(" -> ");
            sb.append(v);
            sb.append("\n");
        });
        return sb.toString();

    }
}
