# java-lib

Java library for interacting with the puncty api

## examples

### get yourself as a user object

```java
// first, a requester needs to be defined with the api base-url
Requester r = new Requester("https://api.puncty.com");
// a new session can be created by either registering or logging in
Session s = Session.register(r, "Dude", "dude@gmail.com", "MySecretPassword");

// using a UserCollection you can interact with the user data from the API
UserCollection uc = new UserCollection(s);

User me = uc.getMe();

System.out.println(me.getName());
System.out.println(me.getEmail());
```

```
Dude
dude@gmail.com
```

### create a meetup

```java
Requester r = new Requester("https://api.puncty.com");
Session s = Session.login(r, "dude@gmail.com", "MySecretPassword");

MeetupCollection mc = new MeetupCollection(s);
Meetup meetup = mc.create(LocalDate.now(), "city center");

System.out.println(meetup.getLocation());
```

```
city center
```
