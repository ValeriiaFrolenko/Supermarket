package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.dto.analytics.EmployeePerformanceDTO;
import com.vfrol.supermarket.filter.EmployeePerformanceFilter;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.stringtemplate4.UseStringTemplateEngine;

import java.util.List;


@UseStringTemplateEngine
@RegisterConstructorMapper(EmployeePerformanceDTO.class)
public interface EmployeePerformanceDAO {

    @SqlQuery("""
    SELECT e.id_employee,
           e.empl_surname || ' ' || e.empl_name AS cashier_name,
           COUNT(c.check_number)                AS receipt_count,
           COALESCE(SUM(c.sum_total), 0)        AS total_amount
    FROM Employee e
    JOIN Check_Table c ON e.id_employee = c.id_employee
    WHERE 1=1
    <if(filter.dateFrom)> AND DATE(c.print_date) >= :dateFrom  <endif>
    <if(filter.dateTo)>   AND DATE(c.print_date) \\<= :dateTo   <endif>
    <if(filter.onlyWithCardAlways)>
      AND NOT EXISTS (
          SELECT 1
          FROM Check_Table c2
          WHERE c2.id_employee = e.id_employee
            <if(filter.dateFrom)> AND DATE(c2.print_date) >= :dateFrom  <endif>
            <if(filter.dateTo)>   AND DATE(c2.print_date) \\<= :dateTo   <endif>
            AND NOT EXISTS (
                SELECT 1
                FROM Customer_Card cc
                WHERE cc.card_number = c2.card_number
            )
      )
    <endif>
    GROUP BY e.id_employee, e.empl_surname, e.empl_name
    ORDER BY <if(filter.sortBy)><filter.sortBy.column><else>total_amount DESC<endif>
    """)
    List<EmployeePerformanceDTO> findByFilter(@BindBean @Define("filter") EmployeePerformanceFilter filter);
}