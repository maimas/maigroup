package com.mg.persistence.commons;

import com.mg.persistence.domain.bizitem.model.BizItemModel;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PropertyUtils {


    /**
     * Converts the value to a String type
     *
     * @param value - value to convert
     * @return - string
     */
    public static String getAsString(Object value) {
        return String.valueOf(value);
    }

    /**
     * Converts the value to a Integer type
     *
     * @param value - value to convert
     * @return - integer
     */
    public static int getAsInteger(Object value) {
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(String.valueOf(value));
    }


    /**
     * Converts the value to a Boolean type
     *
     * @param value - value to convert
     * @return - boolean
     */
    public static Boolean getAsBoolean(Object value) {
        return Boolean.parseBoolean(String.valueOf(value));
    }

    /**
     * Converts the value to a Double type
     *
     * @param value - value to convert
     * @return - double
     */
    public static double getAsDouble(Object value) {
        return Double.parseDouble(String.valueOf(value));
    }

    /**
     * Converts the value to a List of Strings
     *
     * @param value - value to convert
     * @return - list of strings <code> List<String> </code>
     */
    public static List<String> getAsListOfStrings(Object value) {
        List<String> result = new ArrayList<>();
        if (value == null) {
            return result;
        }

        if (value instanceof List) {
            ((List) value).forEach(it -> result.add(getAsString(it)));
            return result;
        }

        if (value instanceof String) {
            String[] split = getAsString(value).split(",");
            result.addAll(Arrays.asList(split));
            return result;
        }
        return result;
    }

    /**
     * Converts the value to an Array of Strings
     *
     * @param value - value to convert
     * @return - array of strings <code> String[] </code>
     */
    public static String[] getAsArrayOfStrings(Object value) {
        Object[] to = getAsListOfStrings(value).toArray();
        return Arrays.copyOf(to, to.length, String[].class);
    }


    /**
     * Converts the value to a Date type
     *
     * @param date - value to convert
     * @return - date
     */
    public static Date getAsDateTime(Object date) {

        if (date == null) {
            return null;
        }

        if (date instanceof Date) {
            return (Date) date;

        } else if (date instanceof String) {
            return format((String) date);

        } else if (date instanceof Integer) {
            return format(String.valueOf(date));


        } else if (date instanceof Long) {
            return format(String.valueOf(date));

        } else {
            throw new RuntimeException("Could not parse to date:" + date);
        }
    }


    /**
     * Converts the value to a BizItemModel type
     *
     * @param value - value to convert
     * @return - bizItem model instance
     */
    public static BizItemModel getObjectAsModel(Object value) {
        try {
            return new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(value), BizItemModel.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Object to model", e);
        }
    }

    /**
     * Converts the value to a BizItemModel type
     *
     * @param value - value to convert
     * @return - bizItem model instance
     */
    public static List<BizItemModel> getObjectListAsModels(Object value) {
        try {
            return new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(value), new TypeReference<List<BizItemModel>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Object to model. It might be that passed value is not a list of models.", e);
        }
    }


    public static Object getAsByteArray(Object value) {
        if (value instanceof byte[]) {
            return value;

        } else if (value instanceof String) {

            if (Base64.isBase64((String) value)) {
                return Base64.decodeBase64(String.valueOf(value).getBytes());
            } else {
                return String.valueOf(value).getBytes();
            }

        } else {
            throw new RuntimeException("Failed to parse to byte array: " + value);
        }
    }

    //--------------------------private Methods--------------------------

    private static Date format(String date) {

        List<String> patterns = Arrays.asList("MM-dd-yyyy HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss");

        for (String pattern : patterns) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                return simpleDateFormat.parse(date);
            } catch (ParseException e) {//ignore
            }
        }

        try {
            return new Date(Long.parseLong(date));
        } catch (Exception e) {//ignore
        }
        throw new RuntimeException("Failed to parse to date: " + date);
    }


}
