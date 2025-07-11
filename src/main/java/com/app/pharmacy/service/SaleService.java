package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.common.Constants;
import com.app.pharmacy.domain.dto.sale.CreateSaleRequest;
import com.app.pharmacy.domain.dto.sale.RefundItemResponse;
import com.app.pharmacy.domain.dto.sale.RefundRequest;
import com.app.pharmacy.domain.dto.sale.RefundResponse;
import com.app.pharmacy.domain.dto.sale.SaleLogRequest;
import com.app.pharmacy.domain.dto.sale.SaleResponse;
import com.app.pharmacy.domain.dto.sale.SaleType;
import com.app.pharmacy.domain.entity.Inventory;
import com.app.pharmacy.domain.entity.Sale;
import com.app.pharmacy.domain.entity.SaleItem;
import com.app.pharmacy.domain.entity.SaleLog;
import com.app.pharmacy.exception.CustomResponseException;
import com.app.pharmacy.exception.ErrorCode;
import com.app.pharmacy.mapper.SaleMapper;
import com.app.pharmacy.repository.CustomerPointConfigRepository;
import com.app.pharmacy.repository.CustomerRepository;
import com.app.pharmacy.repository.InventoryRepository;
import com.app.pharmacy.repository.SaleLogRepository;
import com.app.pharmacy.repository.SaleRepository;
import com.app.pharmacy.specification.SaleSpecification;
import com.app.pharmacy.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.app.pharmacy.specification.SaleSpecification.hasEmployeeId;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final InventoryRepository inventoryRepository;
    private final SaleLogRepository saleLogRepository;
    private final CustomerRepository customerRepository;
    private final CustomerPointConfigRepository customerPointConfigRepository;
    private final Clock clock;

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse<SaleResponse> createSale(CreateSaleRequest request, Authentication connectedUser) {
        ApiResponse<SaleResponse> response = new ApiResponse<>();
        LocalDateTime now = LocalDateTime.now(clock);
        Sale sale = SaleMapper.INSTANCE.toEntity(request, now, connectedUser.getName());

        List<SaleItem> saleItems = SaleMapper.INSTANCE.toListEntity(request.saleItems(), now, connectedUser.getName(), sale);
        sale.setSaleItems(saleItems);
        final BigDecimal[] totalAmount = {saleItems.stream().map(SaleItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add)};

        if (request.customerId() != null) {
            customerRepository.findById(request.customerId()).ifPresentOrElse(customer -> {
                if (request.usePoint() != null && request.usePoint()) {
                    totalAmount[0] = totalAmount[0].subtract(customer.getPoints());
                    customer.setPoints(new BigDecimal(0));
                } else {
                    BigDecimal point = customer.getPoints().add(customerPointConfigRepository.findAll().get(0).getRatio().multiply(totalAmount[0]));
                    customer.setPoints(point);
                }
                customerRepository.save(customer);
            }, () -> {});
        }
        sale.setTotalAmount(totalAmount[0]);
        saleRepository.save(sale);

        List<String> inventoryIds = new ArrayList<>();
        Map<String, Integer> inventoryRequestMap = new HashMap<>();
        request.saleItems().forEach(saleItemRequest -> {
            inventoryIds.add(saleItemRequest.inventoryId());
            inventoryRequestMap.put(saleItemRequest.inventoryId(), saleItemRequest.quantity());
        });

        List<Inventory> inventory = inventoryRepository
                .findByIdIn(inventoryIds).stream().peek(
                        i -> {
                            i.setQuantity(i.getQuantity() - inventoryRequestMap.get(i.getId()));
                            i.setUpdatedBy(connectedUser.getName());
                            i.setUpdatedDate(LocalDateTime.now(clock));
                            inventoryRequestMap.remove(i.getId());
                        }).toList();
        if (!inventoryRequestMap.isEmpty()) {
            throw new CustomResponseException(ErrorCode.INVENTORY_NOT_EXIST);
        }
        inventoryRepository.saveAll(inventory);

        String saleCode = Constants.SALE_CODE_PREFIX
                + StringUtils.generateRandomString(2)
                + StringUtils.generateRandomNumberString(6);
        saleLogRepository.save(SaleLog
                .builder()
                .saleId(sale.getId())
                .totalAmount(sale.getTotalAmount())
                .usePoint(request.usePoint())
                .createdBy(connectedUser.getName())
                .type(SaleType.SALE)
                .code(saleCode)
                .createdDate(now)
                .build());

        SaleResponse saleResponse = SaleMapper.INSTANCE.toSaleResponse(sale);
        saleResponse.setType(SaleType.SALE);
        saleResponse.setCode(saleCode);
        saleResponse.setCreatedBy(connectedUser.getName());
        saleResponse.setCreatedDate(now);
        response.setData(saleResponse);
        return response;
    }

    public ApiResponse<CommonGetResponse<SaleResponse>> getSales(SaleLogRequest request, Pageable pageable, Authentication connectedUser) {
        ApiResponse<CommonGetResponse<SaleResponse>> response = new ApiResponse<>();
        String employeeId = connectedUser.getName();
        if (connectedUser.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            employeeId = null;
        }
        Specification<SaleLog> specification = Specification.where(
                SaleSpecification.hasSaleDate(request.saleDateBegin(), request.saleDateEnd())
                        .and(hasEmployeeId(employeeId))
        );

        Page<SaleLog> saleLogPage = saleLogRepository.findAll(specification, pageable);
        List<SaleResponse> saleResponses = SaleMapper.INSTANCE.toSaleResponseList(saleLogPage.getContent(), inventoryRepository);
        response.setData(new CommonGetResponse<>(
                saleResponses, saleLogPage.getSize(), saleLogPage.getNumber(), saleLogPage.getTotalElements()
        ));
        return response;
    }

    @Transactional(rollbackOn = Exception.class)
    public ApiResponse<RefundResponse> refund(List<RefundRequest> request, Authentication connectedUser) {
        ApiResponse<RefundResponse> response = new ApiResponse<>();
        List<RefundItemResponse> refundItemResponses = new ArrayList<>();
        AtomicReference<BigDecimal> totalRefundAmountAtomic = new AtomicReference<>(new BigDecimal(0));
        String saleCode = Constants.REFUND_CODE_PREFIX
                + StringUtils.generateRandomString(2)
                + StringUtils.generateRandomNumberString(6);
        List<String> inventoryIds = request.stream().map(RefundRequest::refundItemId).distinct().toList();
        Map<String, Integer> requestMap = new HashMap<>();
        request.forEach(r -> requestMap.merge(r.refundItemId(), r.refundItemQuantity(), Integer::sum));
        List<Inventory> inventories = new ArrayList<>();
        List<String> foundInventoryIds = new ArrayList<>();
        inventoryRepository.findByIdIn(inventoryIds).forEach(inventory -> {
            Integer refundQuantity = requestMap.get(inventory.getId());
            inventory.setQuantity(inventory.getQuantity() + refundQuantity);
            BigDecimal refundAmount = inventory.getMedicine().getPrice().multiply(BigDecimal.valueOf(refundQuantity));
            totalRefundAmountAtomic.updateAndGet(current -> current.add(refundAmount));
            foundInventoryIds.add(inventory.getId());
            RefundItemResponse refundItemResponse = RefundItemResponse
                    .builder()
                    .medicineName(inventory.getMedicine().getName())
                    .refundAmount(refundAmount)
                    .quantity(refundQuantity)
                    .build();
            refundItemResponses.add(refundItemResponse);
        });
        if (foundInventoryIds.isEmpty()) {
            throw new CustomResponseException(ErrorCode.INVENTORY_NOT_EXIST);
        }
        RefundResponse refundResponse = RefundResponse
                .builder()
                .refundItemResponses(refundItemResponses)
                .type(SaleType.REFUND)
                .code(saleCode)
                .build();
        response.setData(refundResponse);
        inventoryRepository.saveAll(inventories);
        SaleLog saleLog = SaleLog
                .builder()
                .saleId(UUID.randomUUID().toString())
                .createdBy(connectedUser.getName())
                .createdDate(LocalDateTime.now(clock))
                .totalAmount(new BigDecimal(0).subtract(totalRefundAmountAtomic.get()))
                .code(saleCode)
                .type(SaleType.REFUND)
                .refundItemId(String.join(",", foundInventoryIds))
                .build();
        saleLogRepository.save(saleLog);
        return response;
    }
}
