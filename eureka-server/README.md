# Discovery Service - EUREKA

## Server

We need to add the following to the dependencies file of the project (`build.gradle`)

```groovy
ext {
    set('springCloudVersion', "2023.0.0-RC1")
}

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```

And we add `@EnableEurekaServer` to the main class:

```java
package com.javabootcamp.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

}
```

### Port

We configure the default port for the Netflix Eureka service **8761**, and other properties

```properties
server.port=8761
# prevent the discovery service from being discovered by the discovery service
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.datacenter=localhost
```

## Client

For the client, we need to add this dependency

```groovy
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
}
```

And this annotation in the main class, `@EnableDiscoveryClient`

```java
package com.javabootcamp.reportingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ReportingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportingServiceApplication.class, args);
    }

}
```

It is a good idea to set the application name in the properties file

```properties
spring.application.name=reporting-service
```

This way, eureka will use this as the name of the service

### Consuming Eureka clients

To consume Eureka registered clients we have different options

- RestTemplate:

  With this, we use rest templates, consuming a web address that corresponds to the eureka server address for the
  client. If we have a service registered with the name "reporting-service", we can use the following
  url `http://reporting-service` and the rest template and eureka will handle redirecting to the microservice

- Feign Clients

  We add the following dependency:
  ```groovy
  dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
  }
  ```
  Now, we create an interface for the service we want (which will comunicate with the other microservice) and annotate
  it with `@FeignClient` and add as the parameter, the name of the service registered in eureka. And add method with
  rest mappings that represent the endpoints of the microservice
```java
@FeignClient("room-service")
public interface RoomServiceClient { 
    @GetMapping("/rooms") 
    List<Room> getAll();
    
    @PostMapping("/rooms")
    Room addRoom(@RequestBody Room room);
    // ......
}
```