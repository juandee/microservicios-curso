package com.user.service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.service.entities.User;
import com.user.service.models.Car;
import com.user.service.models.Motorcycle;
import com.user.service.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<List<User>> listUsers() {
		List<User> users = userService.getAll();
		if (users.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(users);
		}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") int id) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}
	
	@PostMapping
	public ResponseEntity<User> saveUser(@RequestBody User user) {
		User newUser = userService.save(user);
		return ResponseEntity.ok(newUser);
	}
	
	@CircuitBreaker(name = "carsCB", fallbackMethod = "fallbackGetCars")
	@GetMapping("/cars/{userId}")
	public ResponseEntity<List<Car>> getCars(@PathVariable("userId") int id) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		List<Car> cars = userService.getCars(id);
		return ResponseEntity.ok(cars);
	}
	
	@CircuitBreaker(name = "motorcyclesCB", fallbackMethod = "fallbackGetMotorcycles")
	@GetMapping("/motorcycles/{userId}")
	public ResponseEntity<List<Motorcycle>> getMotorcycles(@PathVariable("userId") int id) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		List<Motorcycle> motorcycles = userService.getMotorcycles(id);
		return ResponseEntity.ok(motorcycles);
	}
	
	@CircuitBreaker(name = "carsCB", fallbackMethod = "fallbackSaveCar")
	@PostMapping("/car/{userId}")
	public ResponseEntity<Car> saveCar(@PathVariable("userId") int userId, @RequestBody Car car){
		Car newCar = userService.saveCar(userId, car);
		return ResponseEntity.ok(newCar);
	}
	
	@CircuitBreaker(name = "motorcyclesCB", fallbackMethod = "fallbackSaveMotorcycle")
	@PostMapping("/motorcycle/{userId}")
	public ResponseEntity<Motorcycle> saveMotorcycle(@PathVariable("userId") int userId, @RequestBody Motorcycle motorcycle){
		Motorcycle newMotorcycle = userService.saveMotorcycle(userId, motorcycle);
		return ResponseEntity.ok(newMotorcycle);
	}
	
	@CircuitBreaker(name = "allCB", fallbackMethod = "fallbackGetAll")
	@GetMapping("/all/{userId}")
	public ResponseEntity<Map<String,Object>> listAllVehicles(@PathVariable("userId") int userId){
		Map<String,Object> result = userService.getUserAndVehicles(userId);
		return ResponseEntity.ok(result);
	}
	
	private ResponseEntity<List<Car>> fallbackGetCars(@PathVariable("userId") int id, RuntimeException exception) {
		return new ResponseEntity("El usuario: " + id + "tiene los autos en el taller", HttpStatus.OK);
	}
	
	private ResponseEntity<Car> fallbackSaveCar(@PathVariable("userId") int id, @RequestBody Car car, RuntimeException exception) {
		return new ResponseEntity("El usuario: " + id + "no tiene plata para comprar el auto", HttpStatus.OK);
	}
	
	private ResponseEntity<List<Motorcycle>> fallbackGetMotorcycles(@PathVariable("userId") int id, RuntimeException exception) {
		return new ResponseEntity("El usuario: " + id + "tiene las motos en el taller", HttpStatus.OK);
	}
	
	private ResponseEntity<Motorcycle> fallbackSaveMotorcycle(@PathVariable("userId") int id, @RequestBody Motorcycle motorcycle, RuntimeException exception) {
		return new ResponseEntity("El usuario: " + id + "no tiene plata para comprar la moto", HttpStatus.OK);
	}
	
	private ResponseEntity<Map<String, Object>> fallbackGetAll(@PathVariable("userId") int id, RuntimeException exception) {
		return new ResponseEntity("El usuario: " + id + "tiene los vehiculos en el taller", HttpStatus.OK);
	}
}
