# ----- Phase 1: Build-Umgebung -----
# Wir verwenden ein Image, das Gradle und das JDK enthält, um unsere Anwendung zu bauen.
FROM gradle:8.8-jdk21 AS build

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere nur Wrapper + Buildfiles (Cache!)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN chmod +x gradlew

# Lade Dependencies vor (Cache-Layer)
# (Kann fehlschlagen wenn noch kein src da ist -> deshalb "|| true")
RUN ./gradlew --no-daemon dependencies || true

# Kopiere den restlichen Quellcode und baue die Anwendung.
COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test


# ----- Phase 2: Laufzeit-Umgebung -----
# Wir verwenden ein schlankes Image, das nur die Java Runtime Environment (JRE) enthält.
FROM eclipse-temurin:21-jre

WORKDIR /app

# Kopiere nur die gebaute .jar-Datei aus der Build-Phase.
# Das Ergebnis ist ein kleines, produktionsreifes Image ohne Build-Tools.
COPY --from=build /app/build/libs/*.jar app.jar

# Exponiere den Port, auf dem die Anwendung läuft.
EXPOSE 8082

# Der Befehl zum Starten der Anwendung.
ENTRYPOINT ["java","-jar","app.jar"]
