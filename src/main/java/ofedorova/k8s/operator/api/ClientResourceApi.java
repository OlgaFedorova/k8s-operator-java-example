package ofedorova.k8s.operator.api;

import lombok.AllArgsConstructor;
import ofedorova.k8s.operator.resource.ClientResource;
import ofedorova.k8s.operator.service.ClientResourceService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping(value = "/api/v1/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@AllArgsConstructor
public class ClientResourceApi {

    private ClientResourceService clientResourceService;

    @GetMapping
    public Map<String, ClientResource> getClientResource() {
        return clientResourceService.getClientResource();
    }
}
