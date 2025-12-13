package com.example.BloodDonationProject.dto.donation;

import com.example.BloodDonationProject.entity.BloodGroup;
import com.example.BloodDonationProject.entity.DonationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationHistoryRequest {

    private String donorId;
    private String recipientId;
    private String bloodRequestId;
    private BloodGroup bloodGroup;
    private Integer units;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime donationDate;

    private String hospital;
    private String city;
    private DonationStatus status;
    private String notes;
}
