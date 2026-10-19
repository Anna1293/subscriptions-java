package ru.kudryaeva.subscriptions.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.kudryaeva.subscriptions.domain.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer>,
        JpaSpecificationExecutor<Subscription> {
}
