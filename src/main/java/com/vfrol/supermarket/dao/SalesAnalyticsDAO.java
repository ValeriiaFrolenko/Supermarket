package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.dto.analytics.SalesAnalyticsDTO;
import com.vfrol.supermarket.filter.SalesAnalyticsFilter;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.stringtemplate4.UseStringTemplateEngine;

import java.util.List;

@UseStringTemplateEngine
@RegisterConstructorMapper(SalesAnalyticsDTO.class)
public interface SalesAnalyticsDAO {

    @SqlQuery("""
    SELECT p.product_name,
           SUM(s.product_number)                   AS quantity_sold,
           SUM(s.product_number * s.selling_price) AS total_amount,
           c.check_number,
           e.empl_surname || ' ' || e.empl_name    AS cashier_name,
           c.print_date
    FROM Sale s
    JOIN Store_Product sp ON s.UPC        = sp.UPC
    JOIN Product       p  ON sp.id_product = p.id_product
    JOIN Check_Table   c  ON s.check_number = c.check_number
    JOIN Employee      e  ON c.id_employee  = e.id_employee
    WHERE 1=1
    <if(filter.dateFrom)>  AND DATE(c.print_date) >= :dateFrom      <endif>
    <if(filter.dateTo)>    AND DATE(c.print_date) \\<= :dateTo       <endif>
    <if(filter.productId)>  AND p.id_product  = :productId          <endif>
    <if(filter.employeeId)> AND e.id_employee = :employeeId         <endif>
    GROUP BY p.id_product, p.product_name,
             c.check_number,
             e.id_employee, e.empl_surname, e.empl_name,
             c.print_date
    ORDER BY <if(filter.sortBy)><filter.sortBy.column><else>c.print_date DESC<endif>
    """)
    List<SalesAnalyticsDTO> findByFilter(@BindBean @Define("filter") SalesAnalyticsFilter filter);
}