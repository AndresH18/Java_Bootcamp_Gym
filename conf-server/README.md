# Configuration Server

To add configuration server we need the following dependency:

```groovy
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-config-server'
}
```

And enable the in the application

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfServerApplication.class, args);
    }
}
```

## Configuration Data

### Repository

We can store the configurations inside of a repository by setting the following properties in `application.properties`
_(or yaml)_:

```properties
spring.cloud.config.server.git.uri=route-to-repo
```

For example:

```properties
spring.cloud.config.server.git.uri=${user.home}/source/repos/gym-config
```

### File System

To set configuration data from the filesystem, we do the following

```properties
spring.profiles.active=native
spring.cloud.config.server.native.search-locations=file:///<absolute-route-to-[folder|file]>
```
For example
```properties
spring.profiles.active=native
spring.cloud.config.server.native.search-locations=file:///${user.dir}/resources/gym_app.properties
# como este projecto tiene varios modulos, entonces la ubicacion es proyect/resources/gym_app.resources
```

## Endpoints

The endpoints follow the following format, having a configuration file `gym.properties` with "REPORTING_SERVICES_PORT:
8081" in the repository:

- `<appname>/<environment>` returns information about the file
  ```http request
  GET http://localhost:8888/gym/default
  ```
  Returns
  ```json
  {
    "name": "gym",
    "profiles": [
      "default"
    ],
    "label": null,
    "version": "e9c61e3b0a8d104967d64366afb7263063bf0875",
    "state": null,
    "propertySources": [
      {
        "name": "C:\\Users\\andres_hoyos/source/repos/gym-config/gym.properties",
        "source": {
          "REPORTING_SERVICES_PORT": "8081"
        }
      }
    ]
  }
  ```

- `<appname>-<environment>.<file-extension>` Returns the content of the configuration
  ```http request
  GET http://localhost:8888/gym-default.properties
  ```
  Returns
  ```text
  REPORTING_SERVICES_PORT: 8081
  ```




