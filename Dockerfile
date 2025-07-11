FROM openjdk:17-jdk-slim

COPY build/libs/*.jar /app/app.jar

# just for test
# for real: we can setup database info via config map, and load password using secret
# pass database info to connect
#ENV DATABASE_URL=jdbc:postgresql://pharmacy_db:5432/pharmacy_db
#ENV DATABASE_USER=admin
#ENV DATABASE_PASSWORD=admin
#ENV KEY_CLOAK_AUTH_SERVER_URL=keycloak:9090

# Run the application
ENTRYPOINT ["java", "-jar", "/libs/app.jar"]