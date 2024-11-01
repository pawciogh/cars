package com.example.cars.controller;

import com.example.cars.model.Car;
import com.example.cars.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/cars")
@CrossOrigin(origins = "http://localhost:3000")

public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        try {
            List<Car> cars = carService.getAllCars();
            return new ResponseEntity<>(cars, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve cars", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        try {
            Optional<Car> car = carService.getCarById(id);
            return car.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve car", e);
        }
    }

    @PostMapping
    public ResponseEntity<String> addCar(@Valid @RequestBody Car newCar) {
        try {
            Car addedCar = carService.addCar(newCar);
            return new ResponseEntity<>("Car added successfully with ID: " + addedCar.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add car", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCar(@PathVariable Long id, @RequestBody Car updatedCar) {
        try {
            Optional<Car> updated = carService.updateCar(id, updatedCar);
            return updated.map(car -> new ResponseEntity<>("Car updated successfully", HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update car", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        try {
            boolean deleted = carService.deleteCar(id);
            return deleted ? new ResponseEntity<>("Car deleted successfully", HttpStatus.OK)
                    : new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete car", e);
        }
    }
}