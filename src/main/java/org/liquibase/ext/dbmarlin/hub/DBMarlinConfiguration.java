package org.liquibase.ext.dbmarlin.hub;

import liquibase.configuration.AutoloadedConfigurations;
import liquibase.configuration.ConfigurationDefinition;

public class DBMarlinConfiguration implements AutoloadedConfigurations {

    public static ConfigurationDefinition<String> DBMARLIN_URL;
    public static ConfigurationDefinition<Integer> DBMARLIN_INSTANCE_ID;

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
    }
}