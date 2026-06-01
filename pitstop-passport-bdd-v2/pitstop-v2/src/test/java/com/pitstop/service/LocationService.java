package com.pitstop.service;

import com.pitstop.model.DigitalPassport;
import com.pitstop.model.Pitstop;
import com.pitstop.model.Stamp;

public class LocationService {

    private static final double CHECK_IN_RADIUS_METRES = 50.0;
    private static final double EARTH_RADIUS_METRES = 6_371_000.0;

    public double calculateDistance(double userLat, double userLon,
                                    double pitstopLat, double pitstopLon) {
        double dLat = Math.toRadians(pitstopLat - userLat);
        double dLon = Math.toRadians(pitstopLon - userLon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(userLat))
                * Math.cos(Math.toRadians(pitstopLat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_METRES * c;
    }

    public Stamp checkIn(DigitalPassport passport, Pitstop pitstop,
                         double userLat, double userLon, String photoURL) {
        double distance = calculateDistance(userLat, userLon,
                pitstop.getLatitude(), pitstop.getLongitude());

        if (distance > CHECK_IN_RADIUS_METRES) {
            throw new IllegalStateException(
                String.format("Too far from location. Distance: %.1f m. Max: %.0f m.",
                        distance, CHECK_IN_RADIUS_METRES));
        }

        boolean alreadyStamped = passport.getStamps().stream()
                .anyMatch(s -> s.getPitstop().getId() == pitstop.getId());
        if (alreadyStamped) {
            throw new IllegalStateException("Veche imash pechat ot tazi lokaciya.");
        }

        Stamp stamp = new Stamp(
                passport.getStamps().size() + 1,
                passport.getId(),
                pitstop,
                photoURL
        );
        passport.addStamp(stamp);
        return stamp;
    }
}
