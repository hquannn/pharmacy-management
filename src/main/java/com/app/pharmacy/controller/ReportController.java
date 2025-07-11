package com.app.pharmacy.controller;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.inventory.GetInventoryRequest;
import com.app.pharmacy.domain.dto.inventory.InventoryDTO;
import com.app.pharmacy.domain.dto.report.ProfitPerDayRequest;
import com.app.pharmacy.domain.dto.report.ProfitPerDayResponse;
import com.app.pharmacy.domain.dto.report.SaleChartInfoResponse;
import com.app.pharmacy.domain.dto.sale.SaleLogRequest;
import com.app.pharmacy.domain.dto.sale.SaleResponse;
import com.app.pharmacy.service.InventoryService;
import com.app.pharmacy.service.ReportService;
import com.app.pharmacy.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final SaleService saleService;
    private final InventoryService inventoryService;
    private final ReportService reportService;

    @GetMapping("/sales")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<ApiResponse<CommonGetResponse<SaleResponse>>> reportSales(
            @ModelAttribute SaleLogRequest request,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(saleService.getSales(request, pageable, connectedUser));
    }

    @GetMapping("/profit-per-day")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<ApiResponse<ProfitPerDayResponse>> reportProfitPerDay(
            @ModelAttribute ProfitPerDayRequest request
    ) {
        return ResponseEntity.ok(reportService.getProfitPerDay(request));
    }

    @GetMapping("/sale-chart-infos")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<ApiResponse<List<SaleChartInfoResponse>>> reportSaleChart() {
        return ResponseEntity.ok(reportService.getSaleChartInfo());
    }

    @GetMapping("/inventory")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<ApiResponse<CommonGetResponse<InventoryDTO>>> reportInventory(
            @ModelAttribute GetInventoryRequest request,
            @PageableDefault(sort = "expDate", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(inventoryService.getInventory(request, pageable));
    }
}
