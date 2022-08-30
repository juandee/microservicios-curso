package com.motorcycle.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motorcycle.service.entities.Motorcycle;
import com.motorcycle.service.repository.MotorcycleRepository;

@Service
public class MotorcycleService {

	@Autowired
	private MotorcycleRepository motorcycleRepository;
	
	public List<Motorcycle> getAll() {	
		return motorcycleRepository.findAll();
	}
	
	public Motorcycle getMotorcycleById(int id) {
		return motorcycleRepository.findById(id).orElse(null);
	}
	
	public Motorcycle save(Motorcycle motorcycle) {
		Motorcycle newMotorcycle = motorcycleRepository.save(motorcycle);
		return newMotorcycle;
	}
	
	public List<Motorcycle> byUserId(int userId) {
		return motorcycleRepository.findByUserId(userId);
	}
	
}
