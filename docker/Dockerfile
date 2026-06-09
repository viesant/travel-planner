# 1: Build
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app
COPY backend .

RUN apk add --no-cache dos2unix && dos2unix mvnw
RUN ./mvnw clean package -DskipTests

# 2: Runtime
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Mapeia a pasta de recursos para que a API enxergue as chaves RSA nativamente
COPY --from=build /app/src/main/resources /app/src/main/resources

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
