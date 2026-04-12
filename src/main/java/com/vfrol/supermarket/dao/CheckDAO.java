package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.dto.check.CheckDetailsDTO;
import com.vfrol.supermarket.dto.check.CheckListDTO;
import com.vfrol.supermarket.entity.Check;
import com.vfrol.supermarket.filter.CheckFilter;
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
@RegisterConstructorMapper(Check.class)
@RegisterConstructorMapper(CheckDetailsDTO.class)
@RegisterConstructorMapper(CheckListDTO.class)
public interface CheckDAO {

    @SqlUpdate("""
    INSERT INTO Check_Table (check_number, id_employee, card_number, sum_total, vat)
    VALUES (:checkNumber, :idEmployee, :cardNumber, :sumTotal, :vat)
    """)
    void create(@BindMethods Check check);

    @SqlUpdate("DELETE FROM Check_Table WHERE check_number = :checkNumber")
    void delete(@Bind("checkNumber") String checkNumber);

    @SqlQuery("""
    SELECT c.check_number,
           e.empl_surname || ' ' || e.empl_name AS employee_name,
           c.card_number,
           CASE WHEN cc.card_number IS NOT NULL
                THEN cc.cust_surname || ' ' || cc.cust_name
                ELSE NULL
           END AS customer_name,
           c.print_date,
           c.sum_total,
           c.vat,
           (SELECT COALESCE(SUM(product_number * selling_price), 0)
            FROM Sale WHERE check_number = c.check_number) AS base_sum,
           ((SELECT COALESCE(SUM(product_number * selling_price), 0)
             FROM Sale WHERE check_number = c.check_number) - c.sum_total) AS discount_amount

    FROM Check_Table c
    JOIN Employee e ON c.id_employee = e.id_employee
    LEFT JOIN Customer_Card cc ON c.card_number = cc.card_number
    WHERE c.check_number = :checkNumber
    """)
    Optional<CheckDetailsDTO> findById(@Bind("checkNumber") String checkNumber);

    @SqlQuery("""
    SELECT c.check_number,
           e.empl_surname || ' ' || e.empl_name AS employee_name,
           c.print_date, c.sum_total
    FROM Check_Table c
    JOIN Employee e ON c.id_employee = e.id_employee
    ORDER BY c.print_date DESC
    """)
    List<CheckListDTO> findAll();

    @SqlQuery("""
    SELECT c.check_number,
           e.empl_surname || ' ' || e.empl_name AS employee_name,
           c.card_number,
           CASE WHEN cc.card_number IS NOT NULL THEN cc.cust_surname || ' ' || cc.cust_name ELSE NULL END AS customer_name,
           c.print_date,
           c.sum_total,
           c.vat,
           (SELECT COALESCE(SUM(product_number * selling_price), 0) FROM Sale WHERE check_number = c.check_number) AS base_sum,
           ((SELECT COALESCE(SUM(product_number * selling_price), 0) FROM Sale WHERE check_number = c.check_number) - c.sum_total) AS discount_amount
    FROM Check_Table c
    JOIN Employee e ON c.id_employee = e.id_employee
    LEFT JOIN Customer_Card cc ON c.card_number = cc.card_number
    ORDER BY c.print_date DESC
    """)
    List<CheckDetailsDTO> findAllDetails();

    @SqlQuery("""
    SELECT c.check_number,
           e.empl_surname || ' ' || e.empl_name AS employee_name,
           c.print_date, c.sum_total
    FROM Check_Table c
    JOIN Employee e ON c.id_employee = e.id_employee
    WHERE 1=1
    <if(filter.checkNumber)> AND c.check_number LIKE :checkNumber || '%' <endif>
    <if(filter.cashierSurname)> AND e.empl_surname LIKE :cashierSurname || '%' <endif>
    <if(filter.employeeId)> AND c.id_employee = :employeeId <endif> 
    <if(filter.dateFrom)> AND DATE(c.print_date) >= :dateFrom <endif>
    <if(filter.dateTo)> AND DATE(c.print_date) \\<= :dateTo <endif>
    ORDER BY <if(filter.sortBy)><filter.sortBy.column><else>c.print_date DESC<endif>
    """)
    List<CheckListDTO> findByFilter(@BindBean @Define("filter") CheckFilter filter);

    @SqlQuery("SELECT EXISTS (SELECT 1 FROM Check_Table WHERE card_number = :cardNumber)")
    boolean existsByCardNumber(@Bind("cardNumber") String cardNumber);

    @SqlQuery("SELECT EXISTS (SELECT 1 FROM Check_Table WHERE id_employee = :idEmployee)")
    boolean existsByEmployeeId(@Bind("idEmployee") String idEmployee);
}