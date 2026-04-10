package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardListDTO;
import com.vfrol.supermarket.entity.CustomerCard;
import com.vfrol.supermarket.filter.CustomerCardFilter;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateEngine;

import java.util.List;
import java.util.Optional;

@UseStringTemplateEngine
@RegisterConstructorMapper(CustomerCard.class)
@RegisterConstructorMapper(CustomerCardDetailsDTO.class)
@RegisterConstructorMapper(CustomerCardListDTO.class)
public interface CustomerCardDAO {

    @SqlUpdate("""
    INSERT INTO Customer_Card (card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent)
    VALUES (:cardNumber, :surname, :name, :patronymic, :phoneNumber, :city, :street, :zipCode, :discount)
    """)
    void create(@BindMethods CustomerCard customerCard);

    @SqlUpdate("""
    UPDATE Customer_Card SET cust_surname = :surname, cust_name = :name,
    cust_patronymic = :patronymic, phone_number = :phoneNumber, city = :city,
    street = :street, zip_code = :zipCode, percent = :discount
    WHERE card_number = :cardNumber
    """)
    void update(@BindMethods CustomerCard customerCard);

    @SqlUpdate("DELETE FROM Customer_Card WHERE card_number = :cardNumber")
    void delete(@Bind("cardNumber") String cardNumber);

    @SqlQuery("""   
    SELECT card_number, cust_surname, cust_name,
    cust_patronymic, phone_number, city,
    street, zip_code, percent
    FROM Customer_Card WHERE card_number = :cardNumber
    """)
    Optional<CustomerCardDetailsDTO> findById(@Bind("cardNumber") String cardNumber);

    @SqlQuery("""
    SELECT card_number, cust_surname, cust_name, percent, phone_number
    FROM Customer_Card ORDER BY cust_surname
    """)
    List<CustomerCardListDTO> findAll();

    @SqlQuery("""
    SELECT card_number, cust_surname, cust_name,
    cust_patronymic, phone_number, city,
    street, zip_code, percent
    FROM Customer_Card ORDER BY cust_surname, cust_name
    """)
    List<CustomerCardDetailsDTO> findAllDetails();

    @SqlQuery("""
    SELECT card_number, cust_surname, cust_name, percent, phone_number
    FROM Customer_Card
    WHERE 1=1
    <if(filter.surname)> AND cust_surname LIKE :surname || '%' <endif>
    <if(filter.phoneNumber)> AND phone_number LIKE :phoneNumber || '%' <endif>
    <if(filter.discountFrom)> AND percent >= :discountFrom <endif>
    <if(filter.discountTo)> AND percent \\<= :discountTo <endif>
    ORDER BY <if(filter.sortBy)><filter.sortBy.column><else>cust_surname<endif>
    """)
    List<CustomerCardListDTO> findByFilter(@BindBean @Define("filter") CustomerCardFilter filter);
}