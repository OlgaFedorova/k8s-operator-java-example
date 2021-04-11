package ofedorova.k8s.operator.service;

import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ofedorova.k8s.operator.resource.ClientResource;
import ofedorova.k8s.operator.resource.ClientResourceList;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClientResourceService {

    private final NonNamespaceOperation<ClientResource, ClientResourceList, Resource<ClientResource>> clientResourceClient;
    private final Map<String, ClientResource> cache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        listThenWatch();
    }

    private void listThenWatch() {

        clientResourceClient
                .list()
                .getItems()
                .forEach(resource -> cache.put(resource.getMetadata().getUid(), resource));

        clientResourceClient.watch(new Watcher<ClientResource>() {

            @Override
            public void eventReceived(Action action, ClientResource resource) {

                String uid = resource.getMetadata().getUid();
                if (cache.containsKey(uid)) {
                    int knownResourceVersion = Integer.parseInt(cache.get(uid).getMetadata().getResourceVersion());
                    int receivedResourceVersion = Integer.parseInt(resource.getMetadata().getResourceVersion());
                    if (knownResourceVersion > receivedResourceVersion) {
                        return;
                    }
                }

                log.info("received {} for resource ", action, resource);
                if (action == Action.ADDED || action == Action.MODIFIED) {
                    cache.put(uid, resource);
                }

                if (action == Action.DELETED) {
                    cache.remove(uid);
                }

            }

            @Override
            public void onClose(WatcherException cause) {
                log.error("Error onClose", cause);
            }
        });

    }

    public Map<String, ClientResource> getClientResource() {
        return cache;
    }
}
