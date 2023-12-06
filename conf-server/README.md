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

## Client
### Configuration Data

#### Repository

We can store the configurations inside of a repository by setting the following properties in `application.properties`
_(or yaml)_:

```properties
spring.cloud.config.server.git.uri=route-to-repo
```

For example:

```properties
spring.cloud.config.server.git.uri=${user.home}/source/repos/gym-config
```

> [!NOTE] We can use the file system for local development (using application-dev.properties, or another local
> environment), and when we deploy we use a production environment using the repository url.

#### File System

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

### Endpoints

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

### Security

To enable security for the configuration server, we add the following dependency:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-security'
```

This will enable username and password authentication by default. however, they are randomly generated at runtime. To
set a username and password, we set the following properties:
```properties
spring.security.user.name=username
spring.security.user.password=password
```
Clients also can configure these properties to enable them to get their configuration

## Client
To enable cloud configuration, we add the following to the `build.gradle`
```groovy
ext {
    set('springCloudVersion', "2022.0.3")
}

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-config'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```

And set this property:
```properties
spring.config.import=optional:configserver:<config-server-url>
```
For example
```properties
spring.config.import=optional:configserver:http://localhost:8888
```
### Security
If the configuration server has default security enable we can set the following properties

```properties
spring.cloud.config.username=username
spring.cloud.config.password=username
```