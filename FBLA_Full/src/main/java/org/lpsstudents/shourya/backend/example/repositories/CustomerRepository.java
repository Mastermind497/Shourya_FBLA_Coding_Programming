package org.lpsstudents.shourya.backend.example.repositories;

import org.lpsstudents.shourya.backend.example.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
