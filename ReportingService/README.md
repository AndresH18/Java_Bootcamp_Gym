# Reporting Service

## Run

To run the service, use:

```shell
.\gradlew bootJar
java -jar .\build\libs\ReportingService-1.0.0.jar
```

If you need to use a specific profile, the add the `--spring.profiles.active` flag with the value of the profile to use,
like this `--spring.profiles.active=dev`; or if there are many, then a comma separated string of profiles, like
this `--spring.profiles.active="aws,dev"`