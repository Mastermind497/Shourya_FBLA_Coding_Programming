package org.lpsstudents.shourya.backend.example.service;

import java.util.Optional;

import org.lpsstudents.shourya.backend.example.data.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterableCrudService<T extends AbstractEntity> extends CrudService<T> {

	Page<T> findAnyMatching(Optional<String> filter, Pageable pageable);

	long countAnyMatching(Optional<String> filter);

}
