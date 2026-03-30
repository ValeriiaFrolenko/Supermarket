package com.vfrol.supermarket.dto.customer_card;
import lombok.Builder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Builder
public record CustomerCardDetailsDTO(
        @ColumnName("card_number") String cardNumber,
        @ColumnName("cust_surname") String surname,
        @ColumnName("cust_name") String name,
        @ColumnName("cust_patronymic") String patronymic,
        @ColumnName("phone_number") String phoneNumber,
        @ColumnName("city") String city,
        @ColumnName("street") String street,
        @ColumnName("zip_code") String zipCode,
        @ColumnName("percent") int discount
) {}