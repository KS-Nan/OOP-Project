package com.project.team1.Carbooking.controller;

import com.project.team1.Carbooking.Data.TripRepository;
import com.project.team1.Carbooking.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.ArrayList;

@Controller
@RequestMapping("/AdminTrip")
public class AdminTripController {

    @Autowired
    private TripRepository repository;

    @GetMapping
    public String getAll(@RequestParam(name = "search", required = false) String search, Model model) {
        List<Trip> tripList = new ArrayList<Trip>();
        if (search != null && !search.isEmpty()) {
            tripList = repository.findByTripNumberContainingIgnoreCaseOrDepartureCityContainingIgnoreCase(search, search);
        } else {
            tripList = repository.findAll();
        }
        model.addAttribute("tripList", tripList);
        return "AdminTrip";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("trips", new Trip());
        return "redirect:/trips";
    }

    @PostMapping("/create")
    public String createTrip(@ModelAttribute("trips") Trip trip, Model model) {
        repository.save(trip);
        return "redirect:/trips";
    }

    @GetMapping("/update/{id}")
    public String showUpdatingForm(@PathVariable("id") int id, Model model) {
        Trip trip= repository.findById(id).get(0);
        model.addAttribute("trips", trip);
        return "update_trips";
    }
    @PostMapping("/update/{id}")
    public String updateTrip(@PathVariable Long id, @ModelAttribute("trips") Trip updatedTrip) {
        Optional<Trip> optionalTrip = repository.findById(id);
        if (optionalTrip.isPresent()) {
            Trip existingTrip = optionalTrip.get();
            existingTrip.setTripNumber(updatedTrip.getTripNumber());
            existingTrip.setDepartureCity(updatedTrip.getDepartureCity());
            existingTrip.setDestinationCity(updatedTrip.getDestinationCity());
            existingTrip.setDepartureTime(updatedTrip.getDepartureTime());
            existingTrip.setArrivalTime(updatedTrip.getArrivalTime());
            existingTrip.setAvailableSeats(updatedTrip.getAvailableSeats());

            repository.save(existingTrip);
        }
        return "redirect:/trips";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrip(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/trips";
    }
}