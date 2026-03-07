package com.airtribe.librarymanagementsystem.repository;

import java.util.*;

import com.airtribe.librarymanagementsystem.entity.Patron;
import com.airtribe.librarymanagementsystem.entity.Reservation;

public class ReservationRepository {

    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation receipt) {
        reservations.add(receipt);
    }

    public List<Reservation> getAllReservations() {
        /*
        Returns a defensive copy of reservations list.
        */
        return new ArrayList<>(reservations);
    }

    public List<Reservation> getReservationsByPatron(Patron patron) {
        /*
        Returns all reservations made by a patron as an ArrayList.
        */

        List<Reservation> result = new ArrayList<>();

        for (Reservation receipt : reservations) {
            if (receipt.getPatron().equals(patron)) {
                result.add(receipt);
            }
        }
        return result;
    }

}
