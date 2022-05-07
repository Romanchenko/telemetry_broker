echo "Building package"
mvn package clean
mvn package
echo "Building image"
docker build . -t apollin/telemetry-broker-sandbox:v4
echo "Pushing image"
docker push apollin/telemetry-broker-sandbox:v4
echo "delete current deployment"
kubectl delete -f  resource-manifests/telemetry-broker.yaml -n istio-system
echo "apply new deployment"
kubectl apply -f resource-manifests/telemetry-broker.yaml -n istio-system