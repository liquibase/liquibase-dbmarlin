package org.liquibase.ext.dbmarlin;

import org.liquibase.ext.dbmarlin.hub.DBMarlinConfiguration;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * The main DB Marlin API Interface.
 */
public class DBMarlin {

    private final String DBMARLIN_URL;
    private final Integer DBMARLIN_DB_TARGET_ID;
    private final Integer DBMARLIN_EVENT_TYPE_ID;
    private final String DBMARLIN_COLOUR_CODE;
    private final String DBMARLIN_API_KEY;

    {
        DBMARLIN_URL = DBMarlinConfiguration.DBMARLIN_URL.getCurrentValue();
        DBMARLIN_DB_TARGET_ID = DBMarlinConfiguration.DBMARLIN_INSTANCE_ID.getCurrentValue();
        DBMARLIN_EVENT_TYPE_ID = DBMarlinConfiguration.DBMARLIN_EVENT_TYPE_ID.getCurrentValue();
        DBMARLIN_COLOUR_CODE = "#2962ff";
		DBMARLIN_API_KEY = DBMarlinConfiguration.DBMARLIN_API_KEY.getCurrentValue();
    }

    /**
     * Main method to handle posts to the DB Marlin API and handle exceptions
     * @param url - URL for DB Marlin endpoint
     * @param payload - JSON Payload for POST
     *
     * @return int
     */
    private int handlePost(String url, String payload) throws IOException {
        // https://www.javaguides.net/2019/07/java-http-getpost-request-example.html
        URL obj = new URL(url);
        HttpURLConnection httpClient = (HttpURLConnection) obj.openConnection();
        httpClient.setRequestMethod("POST");
        httpClient.setRequestProperty("Content-Type", "application/json");
		httpClient.setRequestProperty ("Authorization", "Basic " + DBMARLIN_API_KEY);

        // Send post request
        httpClient.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(payload);
            wr.flush();
        }

        return httpClient.getResponseCode();
    }


    /**
     * Format Java Date to DateTimeFormatter String
     *
     * @param date - Java Date to be formatted
     *
     * @return String
     */
    private String formatDate(Date date, DateTimeFormatter format) {
        LocalDateTime localDateTime = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return localDateTime.format(format);
    }

    /**
     * JSON Payload for DB Marlin API
     *
     * example from:
     * https://docs.dbmarlin.com/docs/integrations/jenkins/#shell-command-to-execute
     *
     * @param eventLink - Shortened Link to Event Report
     * @param eventTime - Time of Event
     * @param eventTitle - Title for Event
     * @param eventDescription - Description for Event
     *
     * @return String
     */
    private String generatePayload(String eventLink, Date eventTime, String eventTitle, String eventDescription) {
        return "[{" +
                "\"eventId\": 1" + "," +
                "\"startDateTime\": \"" + this.formatDate(eventTime, DateTimeFormatter.ISO_DATE_TIME) + "\"," +
                "\"databaseTargetId\": " + this.DBMARLIN_DB_TARGET_ID + "," +
                "\"eventTypeId\": " + this.DBMARLIN_EVENT_TYPE_ID + "," +
                "\"title\": \"" + eventTitle + "\"," +
                "\"colourCode\": \"" + this.DBMARLIN_COLOUR_CODE + "\"," +
                "\"description\": \"" + eventDescription + "\"," +
                "\"detailsUrl\": \"" + eventLink + "\"," +
                "\"endDateTime\": " + null +
                "}]";
    }

    /**
     * Post Event data to Endpoint
     *
     * @param eventLink - Shortened Link to Event Report
     * @param eventTime - Time of Event
     * @param eventTitle - Title for Event
     * @param eventDescription - Description for Event
     *
     * @return int
     */
    public int postEvent(String eventLink, Date eventTime, String eventTitle, String eventDescription) throws IOException {
        String url = DBMARLIN_URL + "/archiver/rest/v1/event";
        String payload = this.generatePayload(eventLink, eventTime, eventTitle, eventDescription);
        return this.handlePost(url, payload);
    }

}
