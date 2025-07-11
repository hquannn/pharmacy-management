package com.app.pharmacy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION("API9999", "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    NO_RESOURCE_FOUND("404", "Not Found", HttpStatus.NOT_FOUND),
    INVALID_KEY("PS0000", "Uncategorized error", HttpStatus.BAD_REQUEST),
    UN_AUTHORIZE("403", "Forbidden", HttpStatus.FORBIDDEN),
    USER_EXISTED("PS00001", "Username or Email is existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST("PS00002", "User is not found", HttpStatus.BAD_REQUEST),
    USERNAME_CANNOT_CHANGE("PS00003", "Username cannot be changed!", HttpStatus.BAD_REQUEST),
    MEDICINE_NOT_EXIST("PS00004", "Medicine is not found", HttpStatus.BAD_REQUEST),
    SUPPLIER_NOT_EXIST("PS00005", "Supplier is not found", HttpStatus.BAD_REQUEST),
    CUSTOMER_NOT_EXIST("PS00006", "Customer is not found", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXIST("PS00007", "Category is not found", HttpStatus.BAD_REQUEST),
    LOCATION_RACK_NOT_EXIST("PS00008", "Location rack is not found", HttpStatus.BAD_REQUEST),
    MED_IS_BEING_USED("PS00009", "Medicine is being used, cannot delete", HttpStatus.BAD_REQUEST),
    PURCHASE_NOT_EXIST("PS00010", "Purchase is not found", HttpStatus.BAD_REQUEST),
    PURCHASE_UPDATE_MEDICINE_INFO_REQUIRED("PS00011",
            "Manufacturing date, expire date, location rack is required when the medicine is provided", HttpStatus.BAD_REQUEST),
    INVENTORY_NOT_EXIST("PS00012", "Inventory is not found", HttpStatus.BAD_REQUEST),
    PHONE_NO_EXISTED("PS00013", "Phone no is used by other customer", HttpStatus.BAD_REQUEST),
    CATEGORY_IS_BEING_USED("PS00014", "Category is being used, cannot delete", HttpStatus.BAD_REQUEST),
    LOCATION_RACK_IS_BEING_USED("PS00015", "Location rack is being used, cannot delete", HttpStatus.BAD_REQUEST),
    SUPPLIER_IS_BEING_USED("PS00016", "Supplier is being used, cannot delete", HttpStatus.BAD_REQUEST),
    CUSTOMER_IS_BEING_USED("PS00017", "Customer is being used, cannot delete", HttpStatus.BAD_REQUEST),
    CONFIG_NOT_EXIST("PS00018", "Config is being used, cannot delete", HttpStatus.BAD_REQUEST),
    OLD_PASSWORD_INVALID("PS00019", "Old password is invalid", HttpStatus.BAD_REQUEST),
    CONFIRM_NEW_PASSWORD_INVALID("PS00020", "Confirm new password does not match", HttpStatus.BAD_REQUEST),
    UNIT_NOT_EXIST("PS00021", "Unit is not found", HttpStatus.BAD_REQUEST),
    CANNOT_CHANGE_ADMIN_PASSWORD("PS00022", "Cannot change password for user 'admin'", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(String code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final String code;
    private final String message;
    private final HttpStatusCode statusCode;
}
