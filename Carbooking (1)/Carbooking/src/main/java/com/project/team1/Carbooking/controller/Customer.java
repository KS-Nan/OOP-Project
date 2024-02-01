package com.project.team1.Carbooking.controller;



import com.project.team1.Carbooking.Data.CustomerInfoRepository;

import com.project.team1.Carbooking.Data.TripRepository;
import com.project.team1.Carbooking.model.CustomerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customerInfo")
public class Customer {

    @Autowired
    private CustomerInfoRepository repository;
    private TripRepository tripRepository;

    @GetMapping
    public String getAll(@RequestParam(name = "search", required = false) String search, Model model) {
        List<CustomerInfo> customerInfoList = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            customerInfoList = repository.findByNameContainingIgnoreCaseOrContactNumberContainingIgnoreCase(search, search);
        } else {
            customerInfoList = repository.findAll();
        }
        model.addAttribute("customerInfoList", customerInfoList);
        return "index1";
    }

    @GetMapping("/create")
    public String showCreateForms(Model model) {
        model.addAttribute("customerInfo", new CustomerInfo());
        return "booking_trip";
    }

    @PostMapping("/create")
    public String createCustomerInfo(@ModelAttribute("customerInfo") CustomerInfo customerInfo) {
        repository.save(customerInfo);
        return "redirect:/customerInfo";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<CustomerInfo> optionalCustomerInfo = repository.findById(id);

        if (optionalCustomerInfo.isPresent()) {
            model.addAttribute("customerInfo", optionalCustomerInfo.get());
            return "update_customerInfo";
        } else {
            redirectAttributes.addFlashAttribute("error", "No customer found with ID: " + id);
            return "redirect:/customerInfo";
        }
    }


    @PostMapping("/update/{id}")
    public String updateCustomerInfo(@PathVariable Long id, @ModelAttribute("customerInfos") CustomerInfo updatedCustomerInfo) {
        Optional<CustomerInfo> optionalCustomerInfo = repository.findById(id);
        if (optionalCustomerInfo.isPresent()) {
            CustomerInfo existingCustomerInfo = optionalCustomerInfo.get();
            existingCustomerInfo.setName(updatedCustomerInfo.getName());
            existingCustomerInfo.setContactNumber(updatedCustomerInfo.getContactNumber());
            // Add other properties if needed

            repository.save(existingCustomerInfo);
        }
        return "redirect:/customerInfo";
    }


    @GetMapping("/delete/{id}")
    public String deleteCustomerInfo(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/customerInfo";
    }
}
