package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.dto.report.ProfitPerDayRequest;
import com.app.pharmacy.domain.dto.report.ProfitPerDayResponse;
import com.app.pharmacy.domain.dto.report.SaleChartInfoResponse;
import com.app.pharmacy.domain.dto.sale.SaleType;
import com.app.pharmacy.domain.entity.SaleLog;
import com.app.pharmacy.repository.SaleLogRepository;
import com.app.pharmacy.specification.SaleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final SaleLogRepository saleLogRepository;

    public ApiResponse<ProfitPerDayResponse> getProfitPerDay(ProfitPerDayRequest request) {
        ApiResponse<ProfitPerDayResponse> response = new ApiResponse<>();
        LocalDate date = request.date() != null ? request.date() : LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999);
        Specification<SaleLog> specification = Specification.where(
                SaleSpecification.hasSaleDate(startOfDay, endOfDay));
        ProfitPerDayResponse profitPerDayResponse = ProfitPerDayResponse
                .builder()
                .amount(saleLogRepository.findAll(specification).stream()
                        .map(SaleLog::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();

        response.setData(profitPerDayResponse);
        return response;
    }

    public ApiResponse<List<SaleChartInfoResponse>> getSaleChartInfo() {
        ApiResponse<List<SaleChartInfoResponse>> response = new ApiResponse<>();
        LocalDateTime startOfYear = LocalDate.now().withDayOfYear(1).atStartOfDay();
        LocalDateTime endOfYear = LocalDate.now().withDayOfYear(1)
                .plusYears(1)
                .minusDays(1)
                .atTime(23, 59, 59, 999999999);

        Specification<SaleLog> specification = Specification.where(
                SaleSpecification.hasSaleDate(startOfYear, endOfYear));
        Map<Month, List<SaleLog>> groupedByMonth = splitByMonth(saleLogRepository.findAll(specification));

        response.setData(toSaleChartInfoResponses(groupedByMonth));
        return response;
    }

    private Map<Month, List<SaleLog>> splitByMonth(List<SaleLog> saleLogs) {
        return saleLogs.stream()
                .collect(Collectors.groupingBy(saleLog -> saleLog.getCreatedDate().getMonth()));
    }

    private List<SaleChartInfoResponse> toSaleChartInfoResponses(Map<Month, List<SaleLog>> groupedByMonth) {
        List<SaleChartInfoResponse> saleChartInfoResponses = new ArrayList<>();
        groupedByMonth.forEach((month, list) -> {
            final BigDecimal[] amountOfRefund = {new BigDecimal(0)};
            final BigDecimal[] amountOfSale = {new BigDecimal(0)};
            AtomicReference<Integer> quantityOfRefund = new AtomicReference<>(0);
            AtomicReference<Integer> quantityOfSale = new AtomicReference<>(0);
            list.forEach(l -> {
                if (SaleType.REFUND.equals(l.getType())) {
                    amountOfRefund[0] = amountOfRefund[0].add(l.getTotalAmount());
                    quantityOfRefund.getAndSet(quantityOfRefund.get() + 1);
                }
                if (SaleType.SALE.equals(l.getType())) {
                    amountOfSale[0] = amountOfSale[0].add(l.getTotalAmount());
                    quantityOfSale.getAndSet(quantityOfSale.get() + 1);
                }
            });
            saleChartInfoResponses.add(SaleChartInfoResponse
                    .builder()
                    .month(month)
                    .amountOfRefund(amountOfRefund[0])
                    .amountOfSale(amountOfSale[0])
                    .totalAmount(amountOfSale[0].add(amountOfRefund[0]))
                    .quantityOfRefund(quantityOfRefund.get())
                    .quantityOfSale(quantityOfSale.get())
                    .totalQuantity(quantityOfSale.get() + quantityOfRefund.get())
                    .build());
        });
        return saleChartInfoResponses;
    }

}
