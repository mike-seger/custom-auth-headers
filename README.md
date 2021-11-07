# Spring Security: Custom header based authentication

This example illustrates how to implement authentication (and authorization) in Spring Security based on a custom HTTP header.

In this example the custom header name is "remote-user", but could be any other header or combination of headers.

The header is used as a token and the "naive" implementation just checks its existence.

Authorization roles are just assigned to the user, disregarding any identity.

Although the example contains the required customization, a real world application must implement the required authentication (identity verification) and add the authorization roles according to the identity.

## Running
```
./gradlew bootRun
```

## Example REST API calls
```
$ curl -H "remote-user: blaster" http://localhost:8080/api/greeting/sdfgdf
{"message":"Successfully sent: sdfgdf by: UsernamePasswordAuthenticationToken [Principal=blaster, Credentials=[PROTECTED], Authenticated=true, Details=null, Granted Authorities=[ROLE_ANOTHER, ROLE_ADMIN]]"}

$ curl http://localhost:8080/api/greeting/sdfgdf
{"timeStamp":"2021-11-07T14:58:24.693743","status":401,"error":"No principal provided in authentication header","path":"/api/greeting/sdfgdf"}

$ curl -H "remote-user: blaster" http://localhost:8080/api/notallowed/sdfgdf
{"timestamp":"2021-11-07T13:43:41.979+00:00","status":403,"error":"Forbidden","path":"/api/notallowed/sdfgdf"}

$ curl -H "remote-user: blaster" http://localhost:8080/api/wrong/sdfgdf
{"timestamp":"2021-11-07T13:44:11.377+00:00","status":404,"error":"Not Found","path":"/api/wrong/sdfgdf"}
```

## Links
[Spring Security: Authentication and Authorization In-Depth](https://www.marcobehler.com/guides/spring-security)
[Custom Header based authentication using Spring example.security](https://salahuddin-s.medium.com/custom-header-based-authentication-using-spring-example.security-17f4163d0986)
