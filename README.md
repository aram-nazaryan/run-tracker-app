# RunTrackerEndpoint API Documentation

## Overview

The `RunTrackerEndpoint` is a RESTful API that provides endpoints for managing users and their running activities. It supports user creation, retrieval, updating, and deletion, as well as operations related to user runs.

## Endpoints

### 1. Create a New User

**URL:** `/users`  
**Method:** `POST`  
**Description:** Creates a new user.

**Request Body:**

- `UserCreationRequestDto`: The data required to create a user.
  ```json
  {
  "firstName": "string",
  "lastName": "string",
  "birthDate": "2024-09-01",
  "gender": "MALE"
  }
  ```

**Response:**

- `200 OK`: Returns the created user details.
- `EntityModel<UserDetailedRM>`: The response model for the created user.
  ```json
  {
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "firstName": "string",
  "laseName": "string",
  "birthDate": "2024-09-01",
  "gender": "MALE",
  "created": "2024-09-01T18:42:12.557Z",
  "updated": "2024-09-01T18:42:12.557Z",
  "runs": [
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "startTime": "2024-09-01T18:42:12.557Z",
      "finishTime": "2024-09-01T18:42:12.557Z",
      "startPosition": {
        "latitude": 0,
        "longitude": 0
      },
      "finishPosition": {
        "latitude": 0,
        "longitude": 0
      },
      "distance": 0,
      "speed": 0,
      "created": "2024-09-01T18:42:12.557Z"
    }
  ],
  "_links": {}
  }
  ```

---

### 2. Update an Existing User

**URL:** `/users/{userId}`  
**Method:** `PUT`  
**Description:** Updates an existing user with the specified `userId`.

**Path Parameters:**

- `userId` (UUID): The ID of the user to update.

**Request Body:**

- `UserPatchRequestDto`: The data required to patch the user.

**Response:**

- `200 OK`: Returns the updated user details.
- `EntityModel<UserDetailedRM>`: The response model for the updated user.

**Error Responses:**

- `404 Not Found`: User not found.

---

### 3. Retrieve User Details

**URL:** `/users/{userId}`  
**Method:** `GET`  
**Description:** Retrieves the details of a user by `userId`.

**Path Parameters:**

- `userId` (UUID): The ID of the user to retrieve.

**Response:**

- `200 OK`: Returns the user details.
- `EntityModel<UserDetailedRM>`: The response model for the user.

**Error Responses:**

- `404 Not Found`: User not found.

---

### 4. Search for Users

**URL:** `/users`  
**Method:** `GET`  
**Description:** Searches for users based on specified criteria.

**Query Parameters:**

- `query` (String): The search query (optional).
- `genders` (Set<Gender>): Filter by gender (optional).
- `bornBefore` (Instant): Filter users born before a specific date (optional).
- `bornAfter` (Instant): Filter users born after a specific date (optional).
- `page` (int): Page number (default is `0`).
- `size` (int): Number of results per page (default is `20`).
- `sortProperty` (UserSortProperty): Property to sort by (default is `CREATED`).
- `sortDirection` (PageRequest.SortDirection): Sort direction (`ASC` or `DESC`, default is `DESC`).

**Response:**

- `200 OK`: Returns a paginated list of users.
- `PagedModel<UserRM>`: The response model for the paginated list of users.

---

### 5. Delete a User

**URL:** `/users/{userId}`  
**Method:** `DELETE`  
**Description:** Deletes a user by `userId`.

**Path Parameters:**

- `userId` (UUID): The ID of the user to delete.

**Response:**

- `200 OK`: Returns the deleted user details.
- `EntityModel<UserDetailedRM>`: The response model for the deleted user.

**Error Responses:**

- `404 Not Found`: User not found.

---

### 6. Update a User's Run
**URL**: `/users/{userId}/runs`
**Method**: `POST`
**Description**: Updates the run details of a user.

**Path Parameters:**

- `userId` (UUID): The ID of the user.
**Request Body**:

`UpdateRunRequestDto`: The data required to update the run. The request body can have two types: START and FINISH. This endpoint supports both starting and finishing operations.

```json
{
  "position": {
    "latitude": 0,
    "longitude": 0
  },
  "time": "2024-09-01T18:46:17.237Z",
  "type": "START"
}
```
- For requests with the type FINISH, the user can optionally send a distance property. If not provided, the distance will be calculated by the service.

**Behavior**:

- Handling Active Runs:

- When a START request is received, the service first checks if there is an active run for the user:
- If an active run exists, the service has two options:
- Finish the Active Run: The service will finish the current active run using the start position of the new run request as the finish position of the active run.

- (Alternative Option) Throw an ActiveRunAlreadyExistsException: This could be another design choice where the service would reject starting a new run if an active run already exists.
- After finishing the existing active run, the service will start a new run with the details provided in the START request.
- If the request is of type FINISH, the service will complete the active run using the provided or calculated finish details.

**Response**:

`200 OK`: Returns the updated run details.
`EntityModel<RunRM>`: The response model for the updated run.
**Error Responses**:

- `404 Not Found`: User or active run not found.
- `400 Bad Request`: Malformed run finish request.
- `409 Conflict`: (If implemented) Active run already exists, and the service does not allow starting a new run until the current one is finished.

---

### 7. Retrieve a User's Runs

**URL:** `/users/{userId}/runs`  
**Method:** `GET`  
**Description:** Retrieves all runs associated with a user.

**Path Parameters:**

- `userId` (UUID): The ID of the user.

**Query Parameters:**

- `from_datetime` (Instant): Filter runs after this date (optional).
- `to_datetime` (Instant): Filter runs before this date (optional).
- `page` (int): Page number (default is `0`).
- `size` (int): Number of results per page (default is `20`).
- `sortProperty` (RunSortProperty): Property to sort by (default is `CREATED`).
- `sortDirection` (PageRequest.SortDirection): Sort direction (`ASC` or `DESC`, default is `DESC`).

**Response:**

- `200 OK`: Returns a paginated list of runs.
- `PagedModel<RunRM>`: The response model for the paginated list of runs.
- ```json
  {
  "_embedded": {
    "content": [
      {
        "id": "8ef4a598-e54e-4b72-8231-1058b9cbd1af",
        "userId": "97cdbb45-5f9c-40cc-901c-7cc11928dd42",
        "startTime": "2024-09-01T18:30:00Z",
        "finishTime": "2024-09-01T19:00:00Z",
        "startPosition": {
          "latitude": 40.494747161865234,
          "longitude": 45.28315544128418
        },
        "finishPosition": {
          "latitude": 40.467796325683594,
          "longitude": 45.28315544128418
        },
        "distance": 2992,
        "speed": 1.6622222222222223,
        "created": "2024-09-01T21:35:41.977016Z"
      }
    ]
  },
  "_links": {
   
  },
  "page": {
    "size": 20,
    "totalElements": 1,
    "totalPages": 1,
    "number": 0
  }
  }
  ```

**Error Responses:**

- `404 Not Found`: User not found.

---

### 8. Retrieve a User's Run Statistics

**URL:** `/users/{userId}/runs/stats`  
**Method:** `GET`  
**Description:** Retrieves statistical data for a user's runs.

**Path Parameters:**

- `userId` (UUID): The ID of the user.

**Query Parameters:**

- `from_datetime` (Instant): Start date for the statistics (optional).
- `to_datetime` (Instant): End date for the statistics (optional).

**Response:**

- `200 OK`: Returns the run statistics.
- `EntityModel<RunStatsRM>`: The response model for the run statistics.
- ```json
  {
  "totalRuns": 2,
  "averageSpeed": 1.78,
  "totalDistance": 6408
  }
  ```

**Error Responses:**

- `404 Not Found`: User not found.

---

## Error Handling

- `UserNotFoundExceptionDto`: Returned when a user is not found.
- `NoActiveRunExceptionDto`: Returned when no active run is found for the user.
- `MalformedRunFinishRequestExceptionDto`: Returned when the run finish request is malformed.

---
