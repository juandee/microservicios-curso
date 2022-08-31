package com.user.service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.user.service.models.Car;
import com.user.service.models.Motorcycle;
import com.user.service.entities.User;
import com.user.service.feignclients.CarFeignClient;
import com.user.service.feignclients.MotorcycleFeignClient;
import com.user.service.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CarFeignClient carFeignClient;
	
	@Autowired
	private MotorcycleFeignClient motorcycleFeignClient;
	
	//RestTemplate
	public List<Car> getCars(int userId) {
		List<Car> cars = restTemplate.getForObject("http://car-service/car/user/" + userId, List.class);
		return cars;
	}
	
	public List<Motorcycle> getMotorcycles(int userId) {
		List<Motorcycle> motorcycles = restTemplate.getForObject("http://motorcycle-service/motorcycle/user/" + userId, List.class);
		return motorcycles;
	}
	//RestTemplate
	
	public List<User> getAll() {	
		return userRepository.findAll();
	}
	
	public User getUserById(int id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public User save(User user) {
		User newUser = userRepository.save(user);
		return newUser;
	}
	
	//FeignClient
	public Car saveCar(int userId, Car car) {
		car.setUserId(userId);
		Car newCar = carFeignClient.save(car);
		return newCar;
	}
	
	public Motorcycle saveMotorcycle(int userId, Motorcycle motorcycle) {
		motorcycle.setUserId(userId);
		Motorcycle newMotorcycle = motorcycleFeignClient.save(motorcycle);
		return newMotorcycle;
	}
	
	public Map<String, Object> getUserAndVehicles(int userId){
		Map<String, Object> result = new HashMap<>();
		User user = userRepository.findById(userId).orElse(null);
		if (user == null) {
			result.put("Mensaje", "El usuario no existe");
			return result;
		}
		result.put("User", user);
		List<Car> cars = carFeignClient.getCars(userId);
		if (cars.isEmpty()) {
			result.put("Autos", "El usuario no tiene autos");
		}
		else {
			result.put("Autos", cars);
		}
		
		List<Motorcycle> motorcycles = motorcycleFeignClient.getMotorcycles(userId);
		if (motorcycles.isEmpty()) {
			result.put("Motos", "El usuario no tiene motos");
		}
		else {
			result.put("Motos", motorcycles);
		}
		
		return result;
	}

}
