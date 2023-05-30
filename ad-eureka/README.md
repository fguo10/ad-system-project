# Eureka server: service discovery

How to build jar file?
```bash
mvn clean package -Dmaven.test.skip=true -U
```

How to run jar file with specific profile?
```bash
java -jar ad-eureka-1.0-SNAPSHOT.jar --spring.profiles.active=server1
```