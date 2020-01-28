package com.quintype.fuberservice.repository;

import com.quintype.fuberservice.common.Color;
import com.quintype.fuberservice.common.Coordinate;
import com.quintype.fuberservice.common.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.quintype.fuberservice.common.Vehicle.Status.AVAILABLE;
import static com.quintype.fuberservice.common.Vehicle.Status.BOOKED_FOR_RIDE;

//Purpose: In reality, it will be a service which receives location updates of all
// Vehicles and has a view of all available vehicles
@Singleton
public class VehicleRepository {
    private static final Logger logger = LoggerFactory.getLogger(VehicleRepository.class.getSimpleName());
    private static final DriverRepository driverRepository = new DriverRepository();
    // In reality, grouping vehicles by state will not be so straight forward :)
    private Map<Vehicle.Status, List<Vehicle>> vehiclesByState;

    public VehicleRepository() {
        vehiclesByState = fetchAllVehicles()
                .stream()
                .collect(Collectors.groupingBy(vehicle -> vehicle.getStatus()));
    }

    public Optional<List<Vehicle>> getAllAvailableVehicles() {
        return vehiclesByState.entrySet()
                .stream()
                .filter(entry -> AVAILABLE == entry.getKey())
                .map(entry -> entry.getValue())
                .findFirst();
    }

    // Magic method - for demonstration. Too utopian so say the least.
    public List<Vehicle> fetchAllVehicles() {
        List<Vehicle> cabs = new ArrayList<>();
        cabs.add(
                new Vehicle(Vehicle.Type.HATCHBACK,
                        "KA-51MG-9125",
                        getRandomCoordinate(),
                        Color.Pink,
                        driverRepository.getRandomDriver())
        );
        cabs.add(new Vehicle(Vehicle.Type.HATCHBACK,
                "KA-21MG-9125",
                getRandomCoordinate(),
                Color.Silver,
                driverRepository.getRandomDriver())
                .setStatus(BOOKED_FOR_RIDE));
        cabs.add(new Vehicle(Vehicle.Type.SEDAN,
                "KA-11MG-9125",
                getRandomCoordinate(),
                Color.White,
                driverRepository.getRandomDriver()));
        cabs.add(new Vehicle(Vehicle.Type.SEDAN,
                "KA-41MG-9125",
                getRandomCoordinate(),
                Color.Pink,
                driverRepository.getRandomDriver()));
        cabs.add(new Vehicle(Vehicle.Type.SUV,
                "KA-81MG-9125",
                getRandomCoordinate(),
                Color.White,
                driverRepository.getRandomDriver()));
        cabs.add(new Vehicle(Vehicle.Type.SUV,
                "KA-55MG-9125",
                getRandomCoordinate(),
                Color.Pink,
                driverRepository.getRandomDriver()));
        return cabs;
    }

    Vehicle getRandomVehicle() {
        List<Vehicle> vehicles = fetchAllVehicles();
        Random r = new Random();
        int index = r.ints(0,vehicles.size()).findFirst().getAsInt();
        return vehicles.get(index);
    }

    private Coordinate getRandomCoordinate() {
        List<Coordinate> coordinates = generateCoordinates();
        Random r = new Random();
        int index = r.ints(0, coordinates.size()).findFirst().getAsInt();
        return coordinates.get(index);
    }

    private List<Coordinate> generateCoordinates() {
        List<Coordinate> result = new ArrayList<>();
        result.add(new Coordinate(13.9,79.6));
        result.add(new Coordinate(12.8,77.5));
        result.add(new Coordinate(12.8,77.5));
        return result;
    }

    public VehicleRepository markAvailable(Vehicle vehicle) {
        logger.info("Before update, Vehicles booked: {} | Vehicles available: {}",
                vehiclesByState.getOrDefault(BOOKED_FOR_RIDE,new ArrayList<>()).size(),
                vehiclesByState.getOrDefault(AVAILABLE,new ArrayList<>()).size());
        vehiclesByState.getOrDefault(BOOKED_FOR_RIDE,new ArrayList<>()).remove(vehicle);
        vehiclesByState.getOrDefault(AVAILABLE,new ArrayList<>()).add(vehicle);
        logger.info("After update, Vehicles booked: {} | Vehicles available: {}",
                vehiclesByState.getOrDefault(BOOKED_FOR_RIDE, new ArrayList<>()).size(),
                vehiclesByState.getOrDefault(AVAILABLE,new ArrayList<>()).size());
        return this;
    }

    public VehicleRepository markBooked(Vehicle vehicle) {
        logger.info("Before update, Vehicles booked: {} | Vehicles available: {}",
                vehiclesByState.get(BOOKED_FOR_RIDE).size(),
                vehiclesByState.get(AVAILABLE).size());

        vehiclesByState.computeIfPresent(BOOKED_FOR_RIDE,(state,availList) -> {
            availList.add(vehicle);
            return availList;
        });

        vehiclesByState.computeIfPresent(AVAILABLE,(state,availList) -> {
            availList.remove(vehicle);
            return availList;
        });

//        vehiclesByState.getOrDefault(BOOKED_FOR_RIDE, new ArrayList<>()).add(vehicle);
//        vehiclesByState.getOrDefault(AVAILABLE, new ArrayList<>()).remove(vehicle);

        logger.info("After update, Vehicles booked: {} | Vehicles available: {}",
                vehiclesByState.get(BOOKED_FOR_RIDE).size(),
                vehiclesByState.get(AVAILABLE).size());
        return this;
    }
}
