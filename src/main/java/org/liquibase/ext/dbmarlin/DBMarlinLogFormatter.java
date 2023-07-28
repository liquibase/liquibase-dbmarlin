package org.liquibase.ext.dbmarlin;

import liquibase.Scope;
import liquibase.exception.LiquibaseException;
import liquibase.ui.UIService;

import com.datical.liquibase.ext.logging.structured.StructuredLogFormatter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.logging.LogRecord;

public class DBmarlinLogFormatter extends StructuredLogFormatter {

    private final DBmarlin DBMARLIN = new DBmarlin();

    private void writeToUI(String message) {
        UIService output = Scope.getCurrentScope().getUI();
        output.sendMessage("----------------------------------------------------------------------");
        output.sendMessage(message);
    }

    private void postToDBMarlin(String logRecord) throws LiquibaseException {

        // Temporary development code that shows the format of the structured logging JSON records:
        this.writeToUI("=================================================");
        this.writeToUI(logRecord);
        this.writeToUI("=================================================");

        Date eventDate = new java.util.Date();
        String eventTitle = "Liquibase changelog";
        String eventDescription = "Liquibase changes applied from " + "<set the name of the changelog file>";
        try {
            int response = this.DBMARLIN.postEvent(logRecord, eventDate, eventTitle, eventDescription);
            if (response == HttpURLConnection.HTTP_OK) {
                this.writeToUI("DBmarlin Successfully Updated.");
            } else  {
                this.writeToUI("Unable to update DBmarlin. Response code is " + response);
            }
        } catch (IOException e) {
            this.writeToUI("There was an error trying to connect to DBmarlin.");
            e.printStackTrace();
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
