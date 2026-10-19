package ru.kudryaeva.subscriptions.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.kudryaeva.subscriptions.api.dto.SubscriptionRequest;
import ru.kudryaeva.subscriptions.api.dto.SubscriptionResponse;
import ru.kudryaeva.subscriptions.api.dto.TotalResponse;
import ru.kudryaeva.subscriptions.service.SubscriptionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse create(@Valid @RequestBody SubscriptionRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<SubscriptionResponse> list(
            @RequestParam(name = "user_id", required = false) UUID userId,
            @RequestParam(name = "service_name", required = false) String serviceName
    ) {
        return service.list(userId, serviceName);
    }

    @GetMapping("/total")
    public TotalResponse total(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(name = "user_id", required = false) UUID userId,
            @RequestParam(name = "service_name", required = false) String serviceName
    ) {
        if (from.isBlank() || to.isBlank()) {
            throw new IllegalArgumentException("from and to are required (MM-YYYY)");
        }
        return new TotalResponse(service.total(from, to, userId, serviceName));
    }

    @GetMapping("/{id}")
    public SubscriptionResponse get(@PathVariable int id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public SubscriptionResponse update(@PathVariable int id, @Valid @RequestBody SubscriptionRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}
