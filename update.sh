mvn package clean
mvn package
docker build . -t apollin/telemetry-broker-sandbox:v4
docker push apollin/telemetry-broker-sandbox:v4
kubectl delete -f  resource-manifests/telemetry-broker.yaml
kubectl apply -f  resource-manifests/telemetry-broker.yaml