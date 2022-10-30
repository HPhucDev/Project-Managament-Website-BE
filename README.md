## How to Run 

This application is packaged as a war which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary. You run it using the ```java -jar``` command.

* Clone this repository 
* Make sure you are using JDK 11 and Maven 3.x
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by one of these two methods:
```
        java -jar -Dspring.profiles.active=test target/Project-Managament-Website-BE.war
or
        mvn spring-boot:run -Drun.arguments="spring.profiles.active=test"
```

## Running the project with MySQL

This project uses an in-memory database so that you don't have to install a database in order to run it. However, converting it to run with another relational database such as MySQL is very easy. Since the project uses Spring Data and the Repository pattern. 

### Create database:
```
        create datebase your_database
```

### In src\main\resources\application.properties add:
```
        spring.datasource.url=jdbc:mysql://localhost:3306/your_database
```       

### Then run is using the 'mysql' profile:
```
        java -jar -Dspring.profiles.active=mysql target/Project-Managament-Website-BE.war
or
        mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=mysql"     
```
or simple start run with your IDE(intelliJ,eclipse)

### To view Swagger 2 API docs

Run the server and browse to localhost:8080/swagger-ui/index.html#

## Add role to use API

### in your_database roles table add:
```
        INSERT INTO `final_project`.`roles` (`id`, `name`) VALUES ('5f4c9aaa-4c42-4230-87f7-14b8d41bf934', 'USER');
        INSERT INTO `final_project`.`roles` (`id`, `name`) VALUES ('e3b094ed-7c14-493e-a859-e8f58991192a', 'ADMIN');
``` 