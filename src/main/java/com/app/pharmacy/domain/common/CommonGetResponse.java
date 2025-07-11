package com.app.pharmacy.domain.common;

import java.util.List;

public record CommonGetResponse<T>(
        List<T> content,
        int size, int number, long totalElement
) {
}
