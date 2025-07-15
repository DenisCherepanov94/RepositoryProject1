package ru.cherepanov.FirstProjectCherepanov.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cherepanov.FirstProjectCherepanov.entity.Order;
import ru.cherepanov.FirstProjectCherepanov.entity.User;

import java.util.UUID;

/**
 * Подключение репозитория для сущности Заказ
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findByUser(User user, Pageable pageable);
}
