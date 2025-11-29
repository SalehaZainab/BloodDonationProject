package com.example.BloodDonationProject;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class BloodDonationProjectController {

    @RequestMapping("/BloodDonation")
    public String BloodDonation() {
        return "Welcome to the Blood Donation Project!";
    }
}