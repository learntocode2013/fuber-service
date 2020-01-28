package com.quintype.fuberservice.repository;

import com.jasongoodwin.monads.Try;
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
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Singleton
public class RiderRepository {
    private static final Logger logger = LoggerFactory.getLogger(RiderRepository.class.getSimpleName());

    private final Map<UUID, User> ridersById = new HashMap<>();

    public RiderRepository() {
        // Cache registered rider details in memory
        fetchAllRegisteredRiders().stream()
                .filter(rider -> rider.getUniqueId().isPresent())
                .forEach(rider -> ridersById.put(rider.getUniqueId().get(), rider));
    }

    public List<User> getRegisteredRiders() {
        List<User> result = new ArrayList<>();
        result.addAll(ridersById.values());
        return result;
    }

    public Optional<User> getById(UUID id) {
        Optional<User> maybeUser = Optional.ofNullable(ridersById.get(id));
        maybeUser.ifPresent(user -> logger.info("Found user with id: {}", id));
        return maybeUser;
    }

    public Try<User> updateRider(UUID id, User rider) {
        return Try.ofFailable(() -> {
            ridersById.put(id, rider);
            logger.info("Details updated for rider with id: {}",id);
            return rider;
        });
    }

    public Try<UUID> registerNewRider(User newRider) {
        return Try.ofFailable(() -> {
            UUID newRiderId = newRider.getUniqueId().orElseGet(() -> UUID.randomUUID());
            newRider.setUniqueId(newRiderId);
            ridersById.put(newRiderId, newRider);
            return newRiderId;
        });
    }

    public void deleteRider(User aRider) {
        Optional.ofNullable(ridersById.remove(aRider.getUniqueId().get()))
                .ifPresent(user -> logger.info("Rider with id: {} was purged from the system",
                        aRider.getUniqueId().get()));
    }

    public Address getRandomRiderAddress() {
        List<Address> addresses = generateRiderAddresses();
        Random r = new Random();
        int index = r.nextInt(addresses.size() - 1);
        return addresses.get(index);
    }

    User getRandomRider() {
        List<User> riders = getRegisteredRiders();
        Random r = new Random();
        int index = r.ints(0, riders.size()).findFirst().getAsInt();
        return riders.get(index);
    }

    private List<User> fetchAllRegisteredRiders() {
        List<User> riders = IntStream.range(1, 5)
                .mapToObj(i -> {
                    User aRider = new User(
                            "Rider-" + i,
                            "91-00821342" + i,
                            getRandomRiderAddress(),
                            "rider_" + i + "@yahoo.com",
                            User.Type.Rider);
                    aRider.setUniqueId(UUID.randomUUID());
                    return aRider;
                })
                .collect(Collectors.toList());

        return riders;
    }

    private List<Address> generateRiderAddresses() {
        List<Address> result = new ArrayList<>();
        result.add(new Address("F-918","GR Sagar Nivas Appt",
                "Hosa Rd",560100,
                "Karnataka","Bangalore", Address.Type.Home)
        );
        result.add(new Address("Flat-202","Neeladri Enclave",
                "Old Airport Rd",560104,
                "Karnataka","Bangalore", Address.Type.Home)

        );
        result.add(new Address("Flat-2B","SpringFields Appartment",
                "Sarjapur Road",560101,
                "Karnataka","Bangalore", Address.Type.Home)
        );
        return result;
    }

    private List<Location> getRiderLocations() {
        List<Location> result = new ArrayList<>();
        result.add(
                new Location(
                        new Coordinate(12.906703356906363,77.6323654778837),
                        getRandomRiderAddress())
        );
        result.add(
                new Location(
                        new Coordinate(12.975553,77.650535),
                        getRandomRiderAddress())
        );
        result.add(
                new Location(
                        new Coordinate(12.893810,77.726070),
                        getRandomRiderAddress())
        );
        return result;
    }

    Location getRandomRiderLocation() {
        List<Location> riderLocations = getRiderLocations();
        Random r = new Random();
        int index = r.ints(0, riderLocations.size()).findFirst().getAsInt();
        return riderLocations.get(index);
    }
}
