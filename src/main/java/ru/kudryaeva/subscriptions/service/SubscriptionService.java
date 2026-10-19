package ru.kudryaeva.subscriptions.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kudryaeva.subscriptions.api.dto.SubscriptionRequest;
import ru.kudryaeva.subscriptions.api.dto.SubscriptionResponse;
import ru.kudryaeva.subscriptions.domain.Subscription;
import ru.kudryaeva.subscriptions.repo.SubscriptionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;

    public SubscriptionService(SubscriptionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public SubscriptionResponse create(SubscriptionRequest request) {
        Subscription entity = new Subscription();
        apply(entity, request);
        return toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse get(int id) {
        return toResponse(find(id));
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> list(UUID userId, String serviceName) {
        Specification<Subscription> spec = Specification.where(null);
        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userId"), userId));
        }
        if (serviceName != null && !serviceName.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("serviceName"), serviceName));
        }
        return repository.findAll(spec).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SubscriptionResponse update(int id, SubscriptionRequest request) {
        Subscription entity = find(id);
        apply(entity, request);
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(int id) {
        Subscription entity = find(id);
        repository.delete(entity);
    }

    @Transactional(readOnly = true)
    public int total(String from, String to, UUID userId, String serviceName) {
        LocalDate fromDate = MonthFormat.parse(from);
        LocalDate toDate = MonthFormat.parse(to);
        return list(userId, serviceName).stream()
                .mapToInt(item -> {
                    LocalDate start = MonthFormat.parse(item.getStartDate());
                    LocalDate end = item.getEndDate() == null
                            ? toDate
                            : MonthFormat.parse(item.getEndDate());
                    int months = OverlapCalculator.overlapMonths(start, end, fromDate, toDate);
                    return months > 0 ? item.getPrice() * months : 0;
                })
                .sum();
    }

    private Subscription find(int id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    private void apply(Subscription entity, SubscriptionRequest request) {
        String name = request.getServiceName() == null ? "" : request.getServiceName().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("service_name is required");
        }
        if (request.getPrice() <= 0) {
            throw new IllegalArgumentException("price must be > 0");
        }
        entity.setServiceName(name);
        entity.setPrice(request.getPrice());
        entity.setUserId(request.getUserId());
        entity.setStartDate(MonthFormat.parse(request.getStartDate()));
        if (request.getEndDate() == null || request.getEndDate().isBlank()) {
            entity.setEndDate(null);
        } else {
            entity.setEndDate(MonthFormat.parse(request.getEndDate()));
        }
    }

    private SubscriptionResponse toResponse(Subscription entity) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(entity.getId());
        response.setServiceName(entity.getServiceName());
        response.setPrice(entity.getPrice());
        response.setUserId(entity.getUserId());
        response.setStartDate(MonthFormat.format(entity.getStartDate()));
        if (entity.getEndDate() != null) {
            response.setEndDate(MonthFormat.format(entity.getEndDate()));
        }
        return response;
    }
}
