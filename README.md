# Tado api demo - java

tado&ordm; is a german based company which offers a smart thermostat solution.
They also have an API to control this solution.

This java application shows how you can use the tado API v2 which is available
at https://my.tado.com/api/v2.

# Module set-up

## generated-tado-api-client
Generated java client for the tado API.

It uses the `openapi-generator-maven-plugin` to generate the client code based on the
tado OpenAPI spec maintained here https://github.com/kritsel/tado-openapispec-v2/

## tado demo

Spring boot application which executes some tado API calls upon application
start-up.

It also implements the OAuth 2.0 authentication mechanism to authenticate
to the API.

# How to run this application

## Prerequisites
You need to have a tado account which is linked to a tado Home.

The username and password of this account need to be supplied as arguments
to the program, as they are needed to authenticate to the API.

## maven command-line - on Windows

On Windows the `spring-boot.run.jvmArguments` part needs to be surrounded by quotes

Replace the placeholders with your account details.

`mvn -pl tado-demo -am spring-boot:run -D"spring-boot.run.jvmArguments"="-Dtado.username=<username> -Dtado.password=<password>"`

## maven command-line - non-Windows

Replace the placeholders with your account details.

`mvn -pl tado-demo -am spring-boot:run -Dspring-boot.run.jvmArguments="-Dtado.username=<username> -Dtado.password=<password>"`

## IntelliJ

Navigate to tado-demo > src > main > java > tadodemo > Application and run
the `main` method.

The first run will be unsuccessful as you will be missing some necessary
arguments. Edit the run configuration and add this part to the VM options
(replace the placeholders with your account details):

`-Dtado.username=<username> -Dtado.password=<password>`

## `useWindowsKeystore` option

This application is developed on a company managed Windows machine,
where the company manages the certificates in the Windows keystore.

This set-up requires that some specific system properties need to be set to instruct
the program to use the Windows keystore instead of the Java one.

When you happen to be in a similar situation, you can add this extra JVM argument:

`-DuseWindowsKeystore=true`
