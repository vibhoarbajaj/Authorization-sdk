## Authorization SDK Integration Guide

This guide explains how to integrate the authz-sdk into a Spring Boot application using the latest version of the dependency. You will configure the AuthorizationManager, register an AuthorizationFilter, and protect API endpoints.

⸻

1. Add Dependency in pom.xml

Include the dependency in your project. Make sure to always use the latest version available on Maven Central.

<dependency>
    <groupId>io.github.vibhoarbajaj</groupId>
    <artifactId>authz-sdk</artifactId>
    <version>0.0.5</version> <!-- Use latest version available -->
</dependency>


⸻

2. Create a Configuration Class

You need to create a Spring @Configuration class that defines two beans:
•	AuthorizationManager – responsible for managing authorization strategies.
•	AuthorizationFilter – applied to incoming HTTP requests.

File: Beanbuilder.java

@Configuration
public class Beanbuilder {

    @Bean
    public AuthorizationManager authorizationManager() {
        Set<String> strategies = new HashSet<>();
        strategies.add("JWT");

        AuthorizationConfig config = new AuthorizationConfig(new HashMap<>(), strategies);

        return new AuthorizationBuilder()
                .withConfig(config)
                .build();
    }

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter(AuthorizationManager manager) {
        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AuthorizationFilter(manager));
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}


⸻

3. Explanation of Each Step

Step 1 – Creating the AuthorizationManager Bean

The AuthorizationManager handles all authorization logic. You configure it by defining which strategies your application will use.
•	Set<String> strategies defines the list of enabled authorization strategies. Here, we add:
•	JWT – tells the SDK to use JWT token validation.
•	AuthorizationConfig holds configuration details including:
•	A map of properties (empty for now)
•	The set of strategies
•	AuthorizationBuilder constructs the final AuthorizationManager instance.

Step 2 – Registering the AuthorizationFilter

The AuthorizationFilter intercepts incoming HTTP requests and enforces authorization using the configured AuthorizationManager.
•	FilterRegistrationBean is used to register the filter in Spring Boot.
•	addUrlPatterns("/api/*") ensures that all API endpoints under /api/ are protected.
•	setOrder(1) ensures the filter runs early in the filter chain.

⸻

4. Folder Structure Example

src/main/java/com/example/demo/
├── Beanbuilder.java
├── controller/
│   └── TestController.java
└── DemoApplication.java


⸻

5. Testing the Setup

Start your Spring Boot application and make a request to any endpoint under /api/. The request will now pass through the authorization filter.

Example:

GET /api/test

If the JWT token or authorization header isn’t valid, the filter will reject the request.

⸻

6. Updating to the Latest Version

Always check for the latest version of the SDK:
•	Visit the official Maven Repository page: io.github.vibhoarbajaj : authz-sdk on MVN Repository
(https://mvnrepository.com/artifact/io.github.vibhoarbajaj/authz-sdk)
•	Use the newest version shown there in your pom.xml

Example:

<version>LATEST_VERSION_FROM_MVN_REPOSITORY</version>

---

## You're ready to secure your APIs!
Your Spring Boot project is now configured with the Authorization SDK. Customize strategies and properties based on your application's needs.