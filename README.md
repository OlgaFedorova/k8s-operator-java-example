#### Settings
Add in .m2\settings.xml
```
 <pluginGroups>
     <pluginGroup>io.fabric8</pluginGroup>
  </pluginGroups>
```

#### Build
build docker images
```
mvn clean install docker:build
```

#### Configure for minikube

Assign role "cluster-admin" to service account “default” in the namespace “default”.
```
kubectl create clusterrolebinding add-on-cluster-admin-default --clusterrole=cluster-admin --serviceaccount=default:default
```


#### Create custom resource definition (CRD)
```
kubectl create -f .\kubectl\cdr-client.yaml
```

#### Add records for custom resource definition
```
kubectl create -f .\kubectl\client-resources.yaml
```

#### Deploy operator service
```
kubectl create -f .\kubectl\client-resource-operator.yml
```