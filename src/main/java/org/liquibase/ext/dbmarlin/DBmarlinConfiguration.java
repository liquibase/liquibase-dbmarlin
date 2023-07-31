package org.liquibase.ext.dbmarlin;

import liquibase.configuration.AutoloadedConfigurations;
import liquibase.configuration.ConfigurationDefinition;

public class DBmarlinConfiguration implements AutoloadedConfigurations {

    public static ConfigurationDefinition<String> DBMARLIN_URL;
    public static ConfigurationDefinition<Integer> DBMARLIN_INSTANCE_ID;
    public static ConfigurationDefinition<Integer> DBMARLIN_EVENT_TYPE_ID;
    public static ConfigurationDefinition<String> DBMARLIN_API_KEY;

    static {
        ConfigurationDefinition.Builder builder = new ConfigurationDefinition.Builder("dbmarlin");
        DBMARLIN_URL = builder.define("url", String.class)
                .setDescription("URL for DBmarlin Database performance monitoring platform.")
                .setDefaultValue("")
                .build();
        DBMARLIN_INSTANCE_ID = builder.define("instanceId", Integer.class)
                .setDescription("The database target ID from the DBmarlin dashboard.")
                //.setDefaultValue(0)
                .build();
        DBMARLIN_API_KEY = builder.define("apiKey", String.class)
                .setDescription("The Base64 encoded user:password for DBmarlin UI/API.")
                .setDefaultValue("")
                .build();
        DBMARLIN_EVENT_TYPE_ID = builder.define("eventTypeId", Integer.class)
                .setDescription("The event type ID from the DBmarlin dashboard.")
                .setDefaultValue(5)
                .build();
    }
}
