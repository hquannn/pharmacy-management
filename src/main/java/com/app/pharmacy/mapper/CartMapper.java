package com.app.pharmacy.mapper;

import com.app.pharmacy.domain.dto.cart.CartDTO;
import com.app.pharmacy.domain.entity.Cart;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;

@Mapper
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "createdBy", source = "employeeCreated.firstName")
    @Mapping(target = "updatedBy", source = "customerUpdated.firstName")
    @Mapping(target = "isGettingExpire", source = "expDate", qualifiedByName = "isGettingExpire")
    CartDTO toDto(Cart cart);

    List<CartDTO> toDtos(List<Cart> Carts);

    @Named("isGettingExpire")
    default Boolean isGettingExpire(LocalDate expDate) {return LocalDate.now().isAfter(expDate.minusMonths(7));}
}
