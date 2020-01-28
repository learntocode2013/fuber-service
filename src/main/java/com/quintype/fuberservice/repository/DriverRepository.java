package com.quintype.fuberservice.repository;

import com.quintype.fuberservice.common.Address;
import com.quintype.fuberservice.common.Coordinate;
import com.quintype.fuberservice.common.Location;
import com.quintype.fuberservice.common.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Singleton
public class DriverRepository {
    private static final Logger logger = LoggerFactory.getLogger(DriverRepository.class.getSimpleName());

    private final Map<UUID, User> driversById = new HashMap<>();

    public DriverRepository() {
        // Cache all registered drivers in memory
        fetchAllRegisteredDrivers()
                .stream()
                .forEach(driver -> driversById.put(driver.getUniqueId().get(),driver));
    }

    User getRandomDriver() {
        List<User> drivers = driversById.values().stream().collect(Collectors.toList());
        Random r = new Random();
        int index = r.ints(0, drivers.size()).findFirst().getAsInt();
        return drivers.get(index);
    }

    private List<User> fetchAllRegisteredDrivers() {
        List<User> drivers = IntStream.range(1,10)
                .mapToObj(i -> {
                    User aDriver = new User(
                            "Driver-" + i,
                            "91-0082134" + i + "6",
                            getRandomAddress(),
                            "driver_"+ i + "@yahoo.com",
                            User.Type.Driver);
                    aDriver.setUniqueId(UUID.randomUUID());
                    return aDriver;
                })
                .collect(Collectors.toList());
        return drivers;
    }

    private Address getRandomAddress() {
        List<Address> addrs = generateDriverAddress();
        Random r = new Random();
        int index = r.ints(0, addrs.size()).findFirst().getAsInt();
        return addrs.get(index);
    }

    private List<Address> generateDriverAddress() {
        List<Address> result = new ArrayList<>();
        result.add(
                new Address("Ground Floor","Akshaya House",
                        "Hosur Road",560100,
                        "Karnataka","Bangalore", Address.Type.Home)
        );

        result.add(
                new Address("115 A/1","Peace home",
                        "Devanahalli",560102,
                        "Karnataka","Bangalore", Address.Type.Home)
        );

        result.add(
                new Address("31-a/2","Ram Nilaya",
                        "ITPL Road",560105,
                        "Karnataka","Bangalore", Address.Type.Home)
        );
        return result;
    }

    private List<Location> getDriverLocations() {
        List<Location> result = new ArrayList<>();
        result.add(new Location(
                new Coordinate(13.906703356906363,79.6323654778837),
                new Address("First Floor","Diamond District",
                        "Old Airport Rd",560100,
                        "Karnataka","Bangalore", Address.Type.Office)

        ));
        result.add(new Location(
                new Coordinate(12.911217,12.911217),
                new Address("First Floor","Diamond District",
                        "Old Airport Rd",560100,
                        "Karnataka","Bangalore", Address.Type.Office)

        ));
        result.add(new Location(
                new Coordinate(12.970559,77.617669),
                new Address("First Floor","Diamond District",
                        "Old Airport Rd",560100,
                        "Karnataka","Bangalore", Address.Type.Office)

        ));
        return result;
    }

    private Location getRandomDriverLocation() {
        List<Location> driverLocations = getDriverLocations();
        Random r = new Random();
        int index = r.ints(0, driverLocations.size()).findFirst().getAsInt();
        return driverLocations.get(index);
    }

}
