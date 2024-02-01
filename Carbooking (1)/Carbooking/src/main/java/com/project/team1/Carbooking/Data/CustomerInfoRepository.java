package com.project.team1.Carbooking.Data;

import com.project.team1.Carbooking.model.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long> {
   List<CustomerInfo> findByNameContainingIgnoreCaseOrContactNumberContainingIgnoreCase(String search, String search1);

}
