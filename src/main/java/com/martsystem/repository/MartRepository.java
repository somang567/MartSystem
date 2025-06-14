package com.martsystem.repository;

import com.martsystem.entity.user.Mart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MartRepository extends JpaRepository<Mart , Long> {
	boolean existsByBusinessRegistrationNumber(String businessRegistrationNumber);
}
