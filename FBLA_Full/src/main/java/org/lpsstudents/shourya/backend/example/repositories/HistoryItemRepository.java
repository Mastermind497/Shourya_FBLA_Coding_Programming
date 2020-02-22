package org.lpsstudents.shourya.backend.example.repositories;

import org.lpsstudents.shourya.backend.example.data.entity.HistoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
}
