FROM openjdk:11
COPY target/telemetry-broker-1.0-SNAPSHOT.jar telemetry-broker-1.0.jar
ENV IMPORT_DELAY=20000
ENV PROMETHEUS_URL="http://localhost"
ENV CH_HOST="http://localhost"
ENTRYPOINT java -Dprometheus.base.url=$PROMETHEUS_URL -Dimport.task.fixedDelay.in.milliseconds=$IMPORT_DELAY -Dch.connection.string=$CH_HOST -jar /telemetry-broker-1.0.jar