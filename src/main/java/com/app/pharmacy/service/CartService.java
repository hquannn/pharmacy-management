package com.app.pharmacy.service;

import com.app.pharmacy.domain.common.ApiResponse;
import com.app.pharmacy.domain.common.CommonGetResponse;
import com.app.pharmacy.domain.dto.cart.CartDTO;
import com.app.pharmacy.domain.dto.cart.GetCartRequest;
import com.app.pharmacy.domain.entity.Cart;
import com.app.pharmacy.mapper.CartMapper;
import com.app.pharmacy.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;

import static com.app.pharmacy.specification.CartSpecification.*;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public ApiResponse<CommonGetResponse<CartDTO>> getCart(GetCartRequest request, Pageable pageable){
        ApiResponse<CommonGetResponse<CartDTO>> response = new ApiResponse<>();
        Specification<Cart> specification = Specification.where(
                hasMedicineName(request.medicineName())
                        .and(hasQuantity(request.quantity()))
                        .and(hasExpireDate(request.expireDateBegin(), request.expireDateEnd()))
        );
        Page<Cart> cartPage = cartRepository.findAll(specification, pageable);
        List<CartDTO> cartDTOS = CartMapper.INSTANCE.toDtos(cartPage.getContent());
        response.setData(new CommonGetResponse<>(
                cartDTOS,
                cartPage.getSize(),
                cartPage.getNumber(),
                cartPage.getTotalElements()
        ));
        return response;
    }
}
