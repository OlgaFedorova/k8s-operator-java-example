package ofedorova.k8s.operator.configuration;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import ofedorova.k8s.operator.resource.ClientResource;
import ofedorova.k8s.operator.resource.ClientResourceList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class K8sConfiguration {

    @Bean
    public KubernetesClient kubernetesClient() {
        Config config = new ConfigBuilder().build();
        KubernetesClient kubernetesClient = new DefaultKubernetesClient(config);
        return kubernetesClient;
    }

    @Bean
    public NonNamespaceOperation<ClientResource, ClientResourceList, Resource<ClientResource>> clientResourceClient(KubernetesClient kubernetesClient){
        return kubernetesClient.customResources(ClientResource.class, ClientResourceList.class)
                .inNamespace(kubernetesClient.getNamespace());
    }
}
