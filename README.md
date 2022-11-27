## How to Run
### Clone this repository
### Create database(Using mysql):
```
        create datebase your_database
```
### In src\main\resources\application.properties modify:
```
        spring.datasource.url=jdbc:mysql://localhost:3306/your_database
```       

### Then run to create all table:
- Open project with intelliJ IDE
- Waiting for update dependency
- Run
### To view Swagger 2 API docs

Run the server and browse to localhost:5000/swagger-ui/index.html#

## Add role to use API

### in your_database roles table add:
```
        INSERT INTO `your_database`.`roles` (`id`, `name`) VALUES ('5f4c9aaa-4c42-4230-87f7-14b8d41bf934', 'USER');
        INSERT INTO `your_database`.`roles` (`id`, `name`) VALUES ('e3b094ed-7c14-493e-a859-e8f58991192a', 'ADMIN');
``` 