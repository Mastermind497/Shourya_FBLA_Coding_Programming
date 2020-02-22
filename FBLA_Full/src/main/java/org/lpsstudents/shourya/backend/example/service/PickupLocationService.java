package org.lpsstudents.shourya.backend.example.service;

import java.util.Optional;

import org.lpsstudents.shourya.backend.example.data.entity.PickupLocation;
import org.lpsstudents.shourya.backend.example.data.entity.User;
import org.lpsstudents.shourya.backend.example.repositories.PickupLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class PickupLocationService implements FilterableCrudService<PickupLocation>{

	private final PickupLocationRepository pickupLocationRepository;

	@Autowired
	public PickupLocationService(PickupLocationRepository pickupLocationRepository) {
		this.pickupLocationRepository = pickupLocationRepository;
	}

	public Page<PickupLocation> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return pickupLocationRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable);
		} else {
			return pickupLocationRepository.findAll(pageable);
		}
	}

	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return pickupLocationRepository.countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return pickupLocationRepository.count();
		}
	}

	public PickupLocation getDefault() {
		return findAnyMatching(Optional.empty(), PageRequest.of(0, 1)).iterator().next();
	}

	@Override
	public JpaRepository<PickupLocation, Long> getRepository() {
		return pickupLocationRepository;
	}

	@Override
	public PickupLocation createNew(User currentUser) {
		return new PickupLocation();
	}
}
