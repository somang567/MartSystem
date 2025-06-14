package com.martsystem.repository;

import com.martsystem.entity.user.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributorRepository extends JpaRepository<Distributor , Long> {
	boolean existsByBusinessRegistrationNumber(String businessRegistrationNumber);
}
