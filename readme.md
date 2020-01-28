# Fuber ride hailing application

## Introduction

This project just scratches the surface of a taxi hailing service. 

The generation of the executable jar file can be performed by issuing the following command

    mvn clean install -DskipTests=true

This will create an executable jar file **fuberservice-microbundle.jar** within the _target_ maven folder. This can be started by executing the following command

    java -jar target/fuberservice-microbundle.jar


For user(s) who have docker engine installed, a convenience script - `start.sh` is available. Executing
the script will spin up a docker container.

## Workflow which can be demonstrated via REST API(s)

User registers to app -> User requests for a cab with or without color preference -> A trip get created -> Trip is started -> Trip is completed -> Invoice is generated -> User submits trip feedback


## Exposed REST endpoint(s)
Verb|Endpoint
---------|--------
GET	| /fuber/riders
POST | /fuber/riders
DELETE | /fuber/riders/{riderId}
GET | /fuber/riders/{riderId}
PUT | /fuber/riders/{riderId}
GET | /fuber/riders/{riderId}/trips
GET	| /fuber/rideRequests
POST |/fuber/rideRequests
GET	| /fuber/rideRequests/{rideId}
PUT	| /fuber/rideRequests/{rideId}
GET | /fuber/trips
POST | /fuber/trips
GET | /fuber/trips/{tripId}
PUT | /fuber/trips/{tripId}
GET	| /fuber/bill/{tripId}
GET	| /fuber/reviews
POST | /fuber/reviews
GET	| /fuber/reviews/{reviewId}

Details on the exposed REST endpoints can also be found at:

    http://localhost:8080/fuber/application.wadl
    
    


## Package structure
```Package name```| ```Responsibility```|
----------------- |---------------------|
billing     | everything related to billing logic goes here|
controller  | all service end-points go here|
booking     | everything related to booking logic goes here|
repository  | simulates data-store operations; contains in-memory domain objects|
common      | domain objects which apply equally to all packages
api.errors  | POJO(s) which are marshalled to JSON to be sent on 4xx scenario(s)


## Assumption(s)
1. Drivers are already registered in the system (in the interest of time, I didnt expose REST endpoints for drivers)
2. All data is in memory

**N.B:** Some unit tests are failing since I could not address them in the interest of time.















