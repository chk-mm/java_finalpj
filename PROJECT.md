# Exercise Tracker Application

## Overview

The exercise tracker application allows users to track their activities. 
The application has the following features:

1. Managing activity types
2. Tracking user exercise
3. Managing users _(already implemented)_


## Implementation

Your task is to implement managing activity types and tracking user exercise.
The database model has already been created, you can use it, or modify it if you want to.
The database technology chosen as a starting point is H2, but you can use any other database if you want to.

---
# Specification


## Users
_This is already implemented. Use it as example for your work._

### Users: functional requirements

Only SYSTEM user (user with id 0) can manage users. The following operations are available:
1. Get a list of all users.
2. Get a user by its unique identifier.
3. Add a new user.
4. Update an existing user.
5. Delete an existing user.

### Users: model

**User** - Defines a user. It has the following properties:
- **id** - The unique identifier of the user.
- **name** - The name of the user.


### About User Context
The application works in user context. Each user has a unique identifier, which is used to associate the user with
their activities and activity types.

For the purpose of simplicity, I have already implemented (very naive) the user management, authentication and authorization.

For each request, the user is identified by a token that is sent in the **Authorization** request header as follows:
```
Authorization: Basic {id}
```
where `{id}` is the unique identifier of the user.

For example, this would identify user 1:
```
Authorization: Basic 1
```

If in your code you need to work with user context, use **AuthorizationService**. 
Method **getCurrentUserId()** always returns id of the user that is currently making the request.


_This is not how real application would implement security, but I wanted to keep is simple_


## Activities
The application allows users to manage activity types. An activity type is a type of activity that a user can perform.

There are two kinds of activity types:
- Application-defined activity types (SYSTEM)
- User-defined activity types (USER)

### Activities: functional requirements

The application must be able to perform the following operations on activity types:
1. Get a list of all activity types **in context of a user**.
2. Get a list of activity types **in context of a user**, filtered by:
   - type (SYSTEM or USER)
3. Get an activity type by its unique identifier, **in context of a user**.
4. Add a new USER activity type.
5. Update an existing USER activity type.
6. Delete an existing USER activity type, if it is not used in any user activity.
7. There are some rules that apply to activity types, see below. 

The following rules apply to activity types:
1. **SYSTEM** activity types are predefined in the application and cannot be modified or deleted by any user.
2. **USER** types are created by users and can be modified or deleted by the user who created them, under following conditions:
    - User-defined activity types cannot be deleted if they are used in any user activity.
    - User-defined activity types can be modified if they are used in any user activity, and the changes will be reflected in the user activities that have already been recorded.
    - User-defined activity types can be deleted if they are not used in any user activity, and only by the user who created them.
    - Activity name must be unique within the user profile (one user cannot use the same activity name twice, but two users can have activity of same name). This is handled by database constraint, but you should handle the error and return a proper error message to the user.

### Activities: model

The activity should have the following properties:
- **id** - Unique identifier of the activity.
- **userId** - The unique identifier of the user who added the activity (0 for SYSTEM)
- **type** - The type of the activity (SYSTEM or USER).
- **name** - The name of the activity.
- **kcalPerMinute** - The number of kilocalories burned per minute during the activity.


## Exercises

The application allows users to track their activities. 

### Exercise: functional requirements

The following rules apply to exercises:
1. User can add a exercise in their context (session).
   - exercise **must** be associated with a user (by id) and an activity (by id).
   - exercise **must** have a start time and duration.
2. Exercises may be updated or deleted only by the user who added it.
3. User can list all their exercises.
4. User can list their exercises filtered by 
   - date
   - activity
   - duration.
5. User can get an exercises by its unique identifier.
6. User cannot see other user exercises.

Optionally, the application may also provide endpoint to get statistics for user activities, if you want to implement it.

### Exercise: model

**NewExercise** - Represents a new exercise record. It has the following properties:
- **userId** - The unique identifier of the user who added the activity.
- **activityId** - The unique identifier of the activity.
- **startTime** - The date and time the activity was started.
- **duration** - The duration of the activity in seconds.

**Exercise** - Represents existing user activity. It has the following properties:
  - **id** - The unique identifier of the user activity.
  - **userId** - The unique identifier of the user who added the activity.
  - **activityId** - The unique identifier of the activity.
  - **startTime** - The date and time the activity was started.
  - **duration** - The duration of the activity in seconds.
  - **kcalBurned** - The number of kilocalories burned during the activity. This is a calculated number based on duration and activity type.

## Other
If there is anything that is not clear, you may ask for clarification, of think about it and make a decision that you think is best.
