package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.dto.sale.SaleListDTO;
import com.vfrol.supermarket.entity.Sale;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterConstructorMapper(Sale.class)
@RegisterConstructorMapper(SaleListDTO.class)
public interface SaleDAO {

    @SqlUpdate("""
    INSERT INTO Sale (UPC, check_number, product_number, selling_price)
    VALUES (:UPC, :checkNumber, :quantity, :price)
    """)
    void create(@BindMethods Sale sale);

    @SqlQuery("""
    SELECT s.UPC, p.product_name, s.product_number, s.selling_price,
           (s.product_number * s.selling_price) AS total_price
    FROM Sale s
    JOIN Store_Product sp ON s.UPC = sp.UPC
    JOIN Product p ON sp.id_product = p.id_product
    WHERE s.check_number = :checkNumber
    ORDER BY p.product_name
    """)
    List<SaleListDTO> findByCheckNumber(@Bind("checkNumber") String checkNumber);

    @SqlQuery("""
    SELECT s.UPC, p.product_name, s.product_number, s.selling_price,
           (s.product_number * s.selling_price) AS total_price
    FROM Sale s
    JOIN Store_Product sp ON s.UPC = sp.UPC
    JOIN Product p ON sp.id_product = p.id_product
    WHERE s.UPC = :upc
    ORDER BY s.check_number
    """)
    List<SaleListDTO> findByUPC(@Bind("upc") String upc);

    @SqlUpdate("DELETE FROM Sale WHERE check_number = :checkNumber")
    void deleteByCheckNumber(@Bind("checkNumber") String checkNumber);
}