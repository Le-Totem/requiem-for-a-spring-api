# Dockerfile "multistage build" en 2 étapes :
# 1. la compilation du projet dans un conteneur "maven"
# 2. le déploiement du projet dans un conteneur "eclipse temurin"

# Etape 1 : compilation du projet
FROM maven:3.9.8-eclipse-temurin-21-alpine as builder
# paramétrage du dossier de travail, le nom est libre
WORKDIR /opt/requiem-for-a-spring-api
# copie uniquement du pom.xml dans le conteneur
COPY pom.xml .
# copie du code source
COPY ./src ./src
# compilation du projet (sans les tests) et création d'un .jar
RUN mvn clean install -DskipTests

# Etape 2 : conteneur en production
# utilisation d'une image recommandée pour sa légèreté
FROM eclipse-temurin:21-jre-alpine
EXPOSE 8000/tcp
WORKDIR /opt/requiem-for-a-spring-api
COPY --from=builder /opt/requiem-for-a-spring-api/target/requiem-of-a-spring-0.0.1-SNAPSHOT.jar requiem-of-a-spring-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/opt/requiem-for-a-spring-api/requiem-of-a-spring-0.0.1-SNAPSHOT.jar"]