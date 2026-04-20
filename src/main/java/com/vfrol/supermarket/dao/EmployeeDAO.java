package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.dto.employee.EmployeeListDTO;
import com.vfrol.supermarket.entity.Employee;
import com.vfrol.supermarket.enums.EmployeeRole;
import com.vfrol.supermarket.filter.EmployeeFilter;
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
@RegisterConstructorMapper(Employee.class)
@RegisterConstructorMapper(EmployeeDetailsDTO.class)
@RegisterConstructorMapper(EmployeeListDTO.class)
public interface EmployeeDAO {

    @SqlQuery("SELECT password_hash FROM Employee WHERE id_employee = :id")
    Optional<String> getPasswordById(@Bind("id") String id);

    @SqlUpdate("""
    INSERT INTO Employee (id_employee, password_hash, empl_surname, empl_name,
    empl_patronymic, empl_role, salary, date_of_birth, date_of_start,
    phone_number, city, street, zip_code)
    VALUES (:id, :passwordHash, :surname, :name, :patronymic, :role,
    :salary, :dateOfBirth, :dateOfStart, :phoneNumber, :city, :street, :zipCode)
    """)
    void create(@BindMethods Employee employee);

    @SqlUpdate("""
    UPDATE Employee SET password_hash = :passwordHash, empl_surname = :surname,
    empl_name = :name, empl_patronymic = :patronymic, empl_role = :role,
    salary = :salary, date_of_birth = :dateOfBirth, date_of_start = :dateOfStart,
    phone_number = :phoneNumber, city = :city, street = :street, zip_code = :zipCode
    WHERE id_employee = :id
    """)
    void update(@BindMethods Employee employee);

    @SqlUpdate("DELETE FROM Employee WHERE id_employee = :id")
    void delete(@Bind("id") String id);

    @SqlQuery("""
    SELECT id_employee, password_hash,
    empl_surname, empl_name, empl_patronymic,
    empl_role, salary, date_of_birth,
    date_of_start, phone_number,
    city, street, zip_code
    FROM Employee WHERE id_employee = :id
    """)
    Optional<EmployeeDetailsDTO> findById(@Bind("id") String id);

    @SqlQuery("""
    SELECT id_employee, empl_surname, empl_name, empl_role, phone_number
    FROM Employee ORDER BY empl_surname
    """)
    List<EmployeeListDTO> findAll();

    @SqlQuery("""
    SELECT id_employee, password_hash,
    empl_surname, empl_name, empl_patronymic,
    empl_role, salary, date_of_birth,
    date_of_start, phone_number,
    city, street, zip_code
    FROM Employee
    ORDER BY empl_surname, empl_name
    """)
    List<EmployeeDetailsDTO> findAllDetails();

    @SqlQuery("""
    SELECT id_employee, empl_surname, empl_name, empl_role, phone_number
    FROM Employee
    WHERE 1=1
    <if(filter.surname)> AND empl_surname LIKE :surname || '%' <endif>
    <if(filter.name)> AND empl_name LIKE :name || '%' <endif>
    <if(filter.phoneNumber)> AND phone_number LIKE :phoneNumber || '%' <endif>
    <if(filter.role)> AND empl_role = :role <endif>
    ORDER BY empl_surname, empl_name
    """)
    List<EmployeeListDTO> findByFilter(@BindBean @Define("filter") EmployeeFilter filter);

    @SqlQuery("SELECT COUNT(*) FROM Employee WHERE empl_role = :role")
    int countByRole(@Bind("role") EmployeeRole role);

    @SqlQuery("SELECT EXISTS (SELECT 1 FROM Employee WHERE id_employee = :id AND empl_role = :role)")
    boolean existsByIdAndRole(@Bind("id") String id, @Bind("role") EmployeeRole role);

    @SqlQuery("SELECT NOT EXISTS (SELECT 1 FROM Employee)")
    boolean isEmpty();
}