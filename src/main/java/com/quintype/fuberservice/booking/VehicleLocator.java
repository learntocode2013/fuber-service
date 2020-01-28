package com.quintype.fuberservice.booking;

import com.quintype.fuberservice.common.Coordinate;
import com.quintype.fuberservice.common.Location;
import com.quintype.fuberservice.common.Utils;
import com.quintype.fuberservice.common.Vehicle;
import com.quintype.fuberservice.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.quintype.fuberservice.common.Constants.VEHICLE_PROXIMITY_MSG;

public class VehicleLocator {
    private static final Logger logger = LoggerFactory.getLogger(VehicleLocator.class.getSimpleName());
    private static final VehicleRepository vehicleRepository = new VehicleRepository();
    // Ideally, this is a business rule which requires stake-holder approval. :)
    private final int MAX_KM_DISTANCE_BETWEEN_CAB_AND_PICKUP = 3;
    private Predicate<Double> nearCondition = distance -> distance <= MAX_KM_DISTANCE_BETWEEN_CAB_AND_PICKUP;


    public VehicleLocator() {
    }

    public List<Vehicle> locateNearestCabs(Location pickupFrom) {
        logger.info("Searching nearest cabs for pickup co-ordinates: {}",
                pickupFrom.getCoordinate());
        List<Vehicle> result = vehicleRepository.getAllAvailableVehicles()
                .orElse(new ArrayList<>())
                .stream()
                .peek(v -> logger.info("Before proximity filter, vehicle: {} {} bearing number {} is available for booking",
                        v.getColor().name(),v.getType(),
                        v.getVehicleNumber()))
                .filter(vehicle -> {
                    double distanceBetween = Utils.compute_KM_DistanceBetween(
                            pickupFrom.getCoordinate(),
                            vehicle.getPresentLocation());
                    boolean isNearby = nearCondition.test(distanceBetween);
                    logProximityMsg(isNearby,pickupFrom.getCoordinate(),vehicle.getVehicleNumber(),distanceBetween);
                    return isNearby;
                })
                .collect(Collectors.toList());
        logger.info("There are {} vehicle(s) near pickup location: {}",result.size(),pickupFrom.getCoordinate());
        return result;
    }

    private void logProximityMsg(boolean isNearby, Coordinate pickup, String cabNum, double distanceFromPickup) {
        String msg = isNearby ? String.format(VEHICLE_PROXIMITY_MSG,cabNum,
                "is",pickup,distanceFromPickup):
                String.format(VEHICLE_PROXIMITY_MSG,cabNum,"is not",
                        pickup, distanceFromPickup);
        logger.info(msg);
    }
}
