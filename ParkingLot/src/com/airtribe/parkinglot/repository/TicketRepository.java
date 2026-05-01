package com.airtribe.parkinglot.repository;

import java.util.ArrayList;
import java.util.List;

import com.airtribe.parkinglot.entity.Ticket;

public class TicketRepository {
    private List<Ticket> tickets;

    public TicketRepository() {
        this.tickets = new ArrayList<>();
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public Ticket getTicketById(String ticketId) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                return ticket;
            }
        }
        return null;
    }
}
