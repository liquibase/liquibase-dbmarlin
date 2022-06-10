package org.liquibase.ext.dbmarlin.hub;

import liquibase.Scope;
import liquibase.exception.LiquibaseException;
import liquibase.hub.core.StandardHubService;
import liquibase.hub.model.*;
import liquibase.ui.UIService;
import org.liquibase.ext.dbmarlin.DBMarlin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;

public class DBMarlinHubService extends StandardHubService {

    private final DBMarlin DBMARLIN = new DBMarlin();

    @Override
    public OperationEvent sendOperationEvent(Operation operation, OperationEvent operationEvent) throws LiquibaseException {
        OperationEvent returnEvent = super.sendOperationEvent(operation, operationEvent);
        if (operationEvent.getEventType().equals("COMPLETE")) {
            this.postToDBMarlin(operation);
        }
        return returnEvent;
    }

    private String getShortHubLink(Operation operation) throws LiquibaseException {
        Connection connection = operation.getConnection();
        String reportURL =
                "/organizations/" + this.getOrganization().getId().toString() +
                "/projects/" + connection.getProject().getId() +
                "/operations/" + operation.getId().toString();
        return this.shortenLink(reportURL);
    }

    private void writeToUI(String message) {
        UIService output = Scope.getCurrentScope().getUI();
        output.sendMessage("----------------------------------------------------------------------");
        output.sendMessage(message);
    }

    private void postToDBMarlin(Operation operation) throws LiquibaseException {
        String hubLink = this.getShortHubLink(operation);
        Date eventDate = operation.getCreateDate();
        String eventTitle = "Liquibase changelog";
        String eventDescription = "Liquibase changes applied from " + operation.getParameters().get("argument__changelogFile");
        try {
            int response = this.DBMARLIN.postEvent(hubLink, eventDate, eventTitle, eventDescription);
            if (response == HttpURLConnection.HTTP_OK) {
                this.writeToUI("DBmarlin Successfully Updated.");
            } else  {
                this.writeToUI("Unable to update DBmarlin. Response code is " +
					response);
            }
        } catch (IOException e) {
            this.writeToUI("There was an error trying to connect to DB Marlin.");
            e.printStackTrace();
        }
    }

    @Override
    public int getPriority() {
        return super.getPriority() + 100;
    }
}
