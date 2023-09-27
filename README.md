# Liquibase DBmarlin Extension
Liquibase for DBmarlin uses structured logging to bring database deployment information (details of deployed changes from your Liquibase changelog) into the DBmarlin timeline.

This information helps you see the performance impact of database changes and gives you the right information in the right context to quickly perform a root-cause analysis of operational problems.

## Install Liquibase Pro + DBmarlin extension

1. If not already installed, install Liquibase Pro. It can be found at https://www.liquibase.com/trial (new users) or https://www.liquibase.com/download (if you already have a license).
2. Download [the latest released DBmarlin extension](https://github.com/liquibase/liquibase-dbmarlin/releases) `.jar` file and place it in the `liquibase/lib` install directory. If you want to use another location, specify the extension `.jar` file in the `classpath` of your [liquibase.properties file](https://docs.liquibase.com/workflows/liquibase-community/creating-config-properties.html).

## Configure your Liquibase Properties file
Note: This extension has a prerequisite of Liquibase Pro.
Make sure to specify your Liquibase Pro license key in the properties file (as shown below) to unlock this capability. Here are additional [instructions for applying your Liquibase Pro license key](https://docs.liquibase.com/workflows/liquibase-pro/how-to-apply-your-liquibase-pro-license-key.html), if needed.

Add the following properties to your `liquibase.properties` file.
```properties
dbmarlin.url: *** URL to your DBmarlin Instance ***
dbmarlin.instanceId: *** The database target ID from the DBmarlin dashboard ***
dbmarlin.apiKey:  *** The base64 encoded username:password if basic auth is enabled ***
dbmarlin.eventTypeId: *** The event type id in DBmarlin (default is 5) ***
liquibase.licenseKey: *** Your Liquibase Pro License Key ***
```
## Run Liquibase Update
Now that Liquibase Pro and the Liquibase DBmarlin extension have been installed and configured, `liquibase update` commands will send their information on database deployments to DBmarlin for easy viewing on the database performance timeline.

## Contribution

To [file a bug](https://github.com/liquibase/liquibase-dbmarlin/issues), improve documentation, or [contribute code](https://github.com/liquibase/liquibase-dbmarlin/pulls), follow our [guidelines for contributing](https://contribute.liquibase.com/code).

[These step-by-step instructions](https://contribute.liquibase.com/extensions-integrations/extensions-overview/#how-to-build-extensions) will help you contribute code for the extension.

## Issue Tracking

Any issues can be logged in the [Github issue tracker](https://github.com/liquibase/liquibase-dbmarlin/issues).

## License

This project is licensed under the [Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.html).
