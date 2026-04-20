package com.vfrol.supermarket.dto.store_product;

import lombok.Builder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Builder
public record StoreProductPromoInfoDTO(
        @ColumnName("id_product") Integer productId,
        @ColumnName("used_as_promo_base") boolean usedAsPromoBase
) {
}