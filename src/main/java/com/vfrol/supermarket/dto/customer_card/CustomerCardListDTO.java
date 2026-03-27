package com.vfrol.supermarket.dto.customer_card;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public record CustomerCardListDTO(
        @ColumnName("card_number") String cardNumber,
        @ColumnName("cust_surname") String surname,
        @ColumnName("cust_name") String name,
        @ColumnName("percent") int discount
) {
}