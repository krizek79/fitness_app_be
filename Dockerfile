# ---------- Build Stage ----------
FROM maven:3.9.6-amazoncorretto-17 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests


# ---------- Run Stage ----------
FROM amazoncorretto:17-alpine
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar
COPY entrypoint.sh .
RUN chmod +x entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]
