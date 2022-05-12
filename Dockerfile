FROM openjdk:11
COPY target/telemetry-broker-1.0-SNAPSHOT.jar telemetry-broker-1.0.jar
ENV IMPORT_DELAY=20000
ENV PROMETHEUS_URL="http://localhost"
ENV CH_HOST="http://localhost"
ENV MONGO_URI="mongodb://bifrost:Zklk83728fshj12la@localhost:27017/main?authSource=admin&readPreference=primary&ssl=true"
RUN mkdir -p /usr/local/share/ca-certificates/Yandex
RUN wget "https://storage.yandexcloud.net/cloud-certs/CA.pem" -O /usr/local/share/ca-certificates/Yandex/YandexInternalRootCA.crt
RUN keytool -importcert -trustcacerts -file /usr/local/share/ca-certificates/Yandex/YandexInternalRootCA.crt -keystore /opt/yandex/keystore -storepass storepass

ENTRYPOINT java -Dprometheus.base.url=$PROMETHEUS_URL -Dimport.task.fixedDelay.in.milliseconds=$IMPORT_DELAY -Dch.connection.string=$CH_HOST -Dsettings.mongo.uri=$MONGO_URI -jar /telemetry-broker-1.0.jar