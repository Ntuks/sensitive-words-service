# Sensitive Words API
### Overview

The Sensitive Words API provides a service for managing sensitive words, allowing users to create, read, update, and delete sensitive words from a database. This API is designed to assist applications in filtering out sensitive content from user messages.

### Features
- Create sensitive words: 
  - Add new sensitive words to the database. 
- Read sensitive words: 
  - Retrieve all sensitive words or a specific word by its ID.
- Update sensitive words: 
  - Modify existing sensitive words.
- Delete sensitive words: 
  - Remove sensitive words from the database.
- Redact messages: 
  - Replace sensitive words in a message with placeholder - currently: `********`.
  
### Technologies Used
- Kotlin 2.x
- Spring Boot 3.x
- Spring Data JPA with MS SQL Server
- Vault for secrets management
- JUnit 5 for unit testing
- Mockito for mocking dependencies

### Prerequisites
- JDK 11 or higher
- Gradle 7.x or higher
- Docker for running an instance of MS SQL Server and Vault (server and client)

### Getting Started
- Clone the Repository
```bash
git clone https://github.com/yourusername/sensitive-words-api.git
cd sensitive-words-api
``` 

### Build the Project
```bash
  ./gradlew build
```
  
### Configure Credentials
There are two
Ensure your application.yml (or application.properties) file is set up for your database connection. For example, for MS SQL Server:

### Running the Application
To run the application, execute:
```bash
./gradlew bootRun
```
The API will be available at http://localhost:8080.
The API documentation will be available at: 

### Security
Basic authentication is enabled for admin endpoints.
To access the admin functionalities, provide credentials in the request headers.
Current Limitation: Only one user is allowed per application instance and that user's credentials need to be set
as env. variables in order to allow usage of private endpoints.

### Running Tests
To run the unit tests, use the following command:

```bash
./gradlew test
```

### Deployment
The project can be deployed on any cloud provider since it has a docker to build an image to be deployed.

A recommended approach would be to deploy the service on AWS, using AWS ECS, ECR and RDS.
Then for the DB and Vault, it is recommended to use hosted services and only use the Docker compose setup for development.

Since currently there is no CI config for the project, there are number of ways to go about this.
1. Use CI tool like GitHub Actions, GitLab CI, etc. then use a CD tool like Harness, etc. to deploy to ECS
2. Do the steps manually:
   1. Build the application locally using the steps above.
   2. Build the application image using Docker.
   3. Use the AWS CLI to push the image to ECR.
   4. Set up a Vault instance for secrets management on HarshiCorp Cloud
      - if (an account does not exist would need to be created)
   5. Go to the console AWS Console to configure/create:
      1. The RDS for MSSQL for the database of the application.
      2. Then ECS cluster.
      3. Task definition - include the links for the RDS and Vault in the configuration
      4. Load balancer to expose the API over internet to allow for load management of requests.


### Project Considerations:
#### Updates and fixes:
   1. Fix the DB configuration to get creds from Vault - currently not working properly
   2. Add system tests
   3. Adding acl structure to the user access. Allowing for different roles to interact with the system.
   4. Saving the messages and their redacted versions (they would have to be encrypted of course) - this
   will allow for redactions to be queried and also improving the system from potential bugs.
   5. Adding Multi-language support. Allowing words to be grouped by languages to allow for language detection of 
   messages and using correct list.
#### Logging & Monitoring:
To allow logging and monitoring in production, I have considered the OpenTelemetry(a.k.a O-Tel) framework.
The reason for this is that it allows for collecting of traces, metrics and application logs. This will allow different views 
of the application with a relatively not complex setup. Also, since O-Tel is growing in adoption this allows non-vendor locking of services
that are used from an infrastructure perspective.
#### Scalability:
For scaling, the service will need to have multiple instances running. Which means that a Load balance is 
also needed to manage the load across the instances. 

Applying Event Driven Architecture for that system as a whole to decouple the system components but most important 
allowing for asynchronous and real-time processing of events and tasks.

#### Security: 
The application as it is needs a number of things to be looked at or improved in this regard.
First is probably improving the authentication from basic auth to token authentication (JWT) for admin users.
For the API, even though public - for our clients, needs to be secure behind an API key mechanisms. 
There is also rate limiting that needs to be considered for the API as to protect against DOS attacks, brute force and API abuse, etc.
Restricting the maximum size of the messages that can be processed for reduction. This will protect from DOS attacks. 

### License
This project is licensed under the MIT License.