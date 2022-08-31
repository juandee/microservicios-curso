package com.user.service.feignclients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.user.service.models.Motorcycle;

@FeignClient(name = "motorcycle-service")
@RequestMapping("/motorcycle")

public interface MotorcycleFeignClient {
	
	@PostMapping()
	public Motorcycle save(@RequestBody Motorcycle motorcycle);
	
	@GetMapping("/user/{userId}")
	public List<Motorcycle> getMotorcycles(@PathVariable("userId") int userId);
}
