# Building Your First REST API with Java and Spring Boot

## Prerequisites
- Java 21
- Maven

## Step 1: Add Spring Boot to `pom.xml`

Add the Spring Boot parent and `spring-boot-starter-web` dependency to your project's `pom.xml`:

```xml
<!-- Add Spring Boot parent -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.5</version>
</parent>

<!-- Add this dependency inside <dependencies> -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

## Step 2: Create the Application Entry Point

Create `Application.java` in `com.a_api`:

```java
package com.a_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## Step 3: Create a REST Controller

Create `HelloController.java` in `com.a_api`:

```java
package com.a_api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
```

## Step 4: Run the Application

```bash
mvn spring-boot:run
```

Visit `http://localhost:8080/api/hello` in your browser. You should see `Hello, World!`.
