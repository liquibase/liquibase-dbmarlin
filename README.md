# Liquibase DB Marlin Extension
When a Liquibase changelog runs and creates an operation in Liquibase Hub, the link for that operation is sent to DB Marlin. This integration is based on the [DB Marlin Jenkins Integration](https://docs.dbmarlin.com/docs/integrations/jenkins/).

## Configuration
Add the following properties to your `liquibase.properties` file.
```properties
liquibase.hub.ApiKey: *** Your Liquibase Hub API Key ***
dbmarlin.url: *** URL to your DBmarlin Instance ***
dbmarlin.instanceId: *** The database target ID from the DBmarlin dashboard ***
dbmarlin.basicAuth: username:password
```

## Configuring the extension

These instructions will help you get the extension up and running on your local machine for development and testing purposes. This extension has a prerequisite of Liquibase core in order to use it. Liquibase core can be found at https://www.liquibase.org/download.

### Liquibase CLI

Download [the latest released Liquibase extension](https://github.com/liquibase/liquibase-dbmarlin/releases) `.jar` file and place it in the `liquibase/lib` install directory. If you want to use another location, specify the extension `.jar` file in the `classpath` of your [liquibase.properties file](https://docs.liquibase.com/workflows/liquibase-community/creating-config-properties.html).

## Contribution

To file a bug, improve documentation, or contribute code, follow our [guidelines for contributing](https://www.liquibase.org/community).

[This step-by-step instructions](https://www.liquibase.org/community/contribute/code) will help you contribute code for the extension.

## Issue Tracking

Any issues can be logged in the [Github issue tracker](https://github.com/liquibase/liquibase-dbmarlin/issues).

## License

This project is licensed under the [Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.html).
