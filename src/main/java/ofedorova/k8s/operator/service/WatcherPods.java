package ofedorova.k8s.operator.service;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.VersionInfo;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatcherPods {

    private final KubernetesClient kubernetesClient;

    @PostConstruct
    public void init() {
        VersionInfo versionInfo = kubernetesClient.getVersion();

        log.info("Version details of this Kubernetes cluster : - ");
        log.info("Compiler: {}", versionInfo.getCompiler());
        log.info("GitCommit: {}",versionInfo.getGitCommit());
        log.info("GitTreeState: {}",versionInfo.getGitTreeState());
        log.info("GitVersion: {}",versionInfo.getGitVersion());
        log.info("GoVersion: {}",versionInfo.getGoVersion());
        log.info("Major: {}",versionInfo.getMajor());
        log.info("Minor: {}",versionInfo.getMinor());
        log.info("Platform: {}",versionInfo.getPlatform());
        log.info("BuildDate: {}",versionInfo.getBuildDate());

        listAndWatchPods(kubernetesClient);

    }

    private void listAndWatchPods(KubernetesClient client) {
        List<Pod> pods = client.pods().list().getItems();
        log.info("Found {} pods : ", pods.size());
        pods.forEach(pod -> log.info("* {}", pod.getMetadata().getName()));

        client.pods().watch(new Watcher<Pod>() {
            @Override
            public void eventReceived(Action action, Pod pod) {
                log.info("Event receive {}, pod {}", action, pod.getMetadata().getName());

            }

            @Override
            public void onClose(WatcherException e) {
                log.error("On close", e);
            }
        });

    }
}
