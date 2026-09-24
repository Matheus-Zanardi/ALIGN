package com.zanamat.align.controller;

import com.zanamat.align.dto.DashboardDTO;
import com.zanamat.align.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService
    ) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardDTO getDashboard(
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return dashboardService.getDashboard(userId);
    }
}