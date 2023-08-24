package org.liquibase.ext.dbmarlin;

import liquibase.Scope;
import liquibase.exception.LiquibaseException;
import liquibase.pro.packaged.w;
import liquibase.ui.UIService;

import com.datical.liquibase.ext.logging.structured.StructuredLogFormatter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.logging.LogRecord;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class DBmarlinLogFormatter extends StructuredLogFormatter {

    private final DBmarlin DBMARLIN = new DBmarlin();

    private void writeToUI(String message) {
        UIService output = Scope.getCurrentScope().getUI();
        output.sendMessage("----------------------------------------------------------------------");
        output.sendMessage(message);
    }

    public String truncateString(String input, int maxLength) {
        if (input.length() <= maxLength) {
            return input; // No truncation needed
        } else {
            return input.substring(0, maxLength); // Truncate to maxLength characters
        }
    }

    private void postToDBMarlin(String logRecord) throws LiquibaseException {

        // Create a SnakeYAML Yaml instance
        Yaml yaml = new Yaml();

        try {
            // Parse the JSON string into a Map
            Object parsedObject = yaml.load(logRecord);
            
            if (parsedObject instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) parsedObject;
                String message = (String) map.get("message");
                String timestamp = (String) map.get("timestamp");
                String changesetSql = (String) map.get("changesetSql");
                String changesetId = (String) map.get("changesetId");
                String changesetFilepath = (String) map.get("changesetFilepath");
                Date eventDate = new java.util.Date();
                
                

                // There are a lot of messages and we only want to send and event for changesets that ran successfully
                if (message.contains("ran successfully")) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                
                    try {
                        eventDate = dateFormat.parse(timestamp);
                        System.out.println("Parsed Date: " + eventDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // eventTitle is limited to 50 chars on the DBmarlin side
                    String eventTitle = "ChangeSet " + changesetFilepath + "::" + changesetId + " success";
                    eventTitle = truncateString(eventTitle, 50);

                    String eventDescription = message + ". \\n" + changesetSql;
                    try {
                        int response = this.DBMARLIN.postEvent("", eventDate, eventTitle, eventDescription);
                        if (response == HttpURLConnection.HTTP_OK) {
                            this.writeToUI ("DBmarlin Successfully Updated.");
                        } else  {
                            this.writeToUI("Unable to update DBmarlin. Response code is " + response);
                        }
                    } catch (IOException e) {
                        this.writeToUI("There was an error trying to connect to DBmarlin.");
                        e.printStackTrace();
                    }
                }

            } else {
                System.err.println("Parsed object is not a Map.");
            }
        } catch (YAMLException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }

    }

    @Override
    public String format(LogRecord logRecord) {
        String formatted = super.format(logRecord);
        try {
            this.postToDBMarlin(formatted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return formatted;
    }

}
