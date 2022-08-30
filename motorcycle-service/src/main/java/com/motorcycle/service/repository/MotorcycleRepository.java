package com.motorcycle.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.motorcycle.service.entities.Motorcycle;

@Repository
public interface MotorcycleRepository extends JpaRepository<Motorcycle, Integer>{
	
	List<Motorcycle> findByUserId(int userId);
	
}
