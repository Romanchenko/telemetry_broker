FROM openjdk:11
COPY target/telemetry-broker-1.0-SNAPSHOT.jar telemetry-broker-1.0.jar
ENV IMPORT_DELAY=20000
ENV PROMETHEUS_URL="http://localhost"
ENV CH_HOST="http://localhost"
ENV MONGO_CONNECTION_STRING="mongodb://bifrost:Zklk83728fshj12la@localhost:27017/main?authSource=admin&readPreference=primary&ssl=true"
ENV RETRIEVAL_TYPE=prometheus
ENV CURRENT_NODE=node
ENTRYPOINT java -Dcurrent.node.name= -Dbasic.metrics.mechanism=$RETRIEVAL_TYPE -Dprometheus.base.url=$PROMETHEUS_URL -Dimport.task.fixedDelay.in.milliseconds=$IMPORT_DELAY -Dch.connection.string=$CH_HOST -Dsettings.mongo.uri=$MONGO_CONNECTION_STRING -jar /telemetry-broker-1.0.jar