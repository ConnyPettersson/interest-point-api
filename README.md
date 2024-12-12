# interest-point-api
Spring Boot project Lab 2

Versionshantering
API:et är för närvarande versionerat med en version i URL:en.
Allt under denna dokumentation refererar till version 1 (/v1).

Exempel:
Hämta alla kategorier: GET http://localhost:8080/v1/categories
Autentisering: POST http://localhost:8080/v1/auth/login



Autentisering
request (POST /v1/auth/login):
{
"username": "user",
"password": "userPassword"
}
Token skickas sedan med i Authorization-headern vid skyddade anrop.


Hämta alla platser utan paginering.
Endpoint: GET /v1/places
Exempel response
[
{
"id": 1,
"name": "Central Park",
"category": {
"id": 1,
"name": "Parks",
"symbol": "🌳",
"description": "Public parks and green spaces"
},
"userId": 1,
"isPrivate": false,
"description": "A large public park",
"coordinates": "POINT(-73.968285 40.785091)",
"createdAt": "2024-01-01T10:00:00",
"updatedAt": "2024-01-02T15:30:00"
}
]


Hämta platser med paginering.
Endpoint: GET /v1/places/paged?page={page}&size={size}
page: vilken sida du vill hämta (0-baserad)
size: hur många objekt per sida (max 100).
GET /v1/places/paged?page=0&size=10

Exempel response
{
"content": [
{
"id": 1,
"name": "Central Park",
"category": {
"id": 1,
"name": "Parks",
"symbol": "🌳",
"description": "Public parks and green spaces"
},
"userId": 1,
"isPrivate": false,
"description": "A large public park",
"coordinates": "POINT(-73.968285 40.785091)",
"createdAt": "2024-01-01T10:00:00",
"updatedAt": "2024-01-02T15:30:00"
}
],
"pageable": {
"pageNumber": 0,
"pageSize": 10
},
"totalElements": 50,
"totalPages": 5,
"last": false
}


Skapa ny plats POST
Endpoint (kräver inloggning): POST /v1/places
Du måste skicka in JSON-data som följer kraven:
name: obligatorisk, minst 3 tecken.
categoryId: obligatorisk (existerande kategori).
isPrivate: true eller false.
description: valfri text (max 500 tecken).
coordinates: giltig WKT-punkt, t.ex. "POINT(-73.968285 40.785091)".
Exempel request:
{
"name": "New Place",
"categoryId": 1,
"isPrivate": false,
"description": "A new location",
"coordinates": "POINT(10 20)"
}

Exempel response:
{
"id": 10,
"name": "New Place",
"category": {
"id": 1,
"name": "Parks",
"symbol": "🌳",
"description": "Public parks and green spaces"
},
"userId": 1,
"isPrivate": false,
"description": "A new location",
"coordinates": "POINT(10 20)",
"createdAt": "2024-01-03T09:00:00",
"updatedAt": "2024-01-03T09:00:00"
}


Uppdatera en befintlig plats PUT
Endpoint (kräver inloggning): PUT /v1/places/{id}
Skicka endast de fält du vill uppdatera. Regler för 
validering gäller beroende på om det är en befintlig plats eller en ny.
Exempel request:
{
"name": "Updated Place Name",
"description": "Updated description"
}

Exempel response:
{
"id": 10,
"name": "Updated Place Name",
"category": {
"id": 1,
"name": "Parks",
"symbol": "🌳",
"description": "Public parks and green spaces"
},
"userId": 1,
"isPrivate": false,
"description": "Updated description",
"coordinates": "POINT(10 20)",
"createdAt": "2024-01-03T09:00:00",
"updatedAt": "2024-01-04T10:30:00"
}
