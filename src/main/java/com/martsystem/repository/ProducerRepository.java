package com.martsystem.repository;

import com.martsystem.entity.user.Producer;
import com.martsystem.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRepository extends JpaRepository<Producer,Long> {
	boolean existsByBusinessRegistrationNumber(String businessRegistrationNumber);
}
