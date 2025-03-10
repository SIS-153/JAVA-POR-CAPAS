FROM maven:3.8.5-openjdk-17 AS build

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo POM y descargar las dependencias
COPY pom.xml .

# Copiar el código fuente
COPY src ./src

RUN mvn clean package -DskipTests

# Usar una imagen base ligera con JDK 17 para la ejecución
FROM openjdk:17-jdk-slim

# Copiar el archivo JAR generado desde la etapa de compilación
COPY --from=build /app/target/taller-modas-0.0.1-SNAPSHOT.jar /app/taller.jar


# Exponer el puerto en el que corre la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/taller.jar"]


