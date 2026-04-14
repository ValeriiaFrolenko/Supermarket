package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductListDTO;
import com.vfrol.supermarket.entity.StoreProduct;
import com.vfrol.supermarket.filter.StoreProductFilter;
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
@RegisterConstructorMapper(StoreProduct.class)
@RegisterConstructorMapper(StoreProductDetailsDTO.class)
@RegisterConstructorMapper(StoreProductListDTO.class)
public interface StoreProductDAO {

    @SqlUpdate("""
    INSERT INTO Store_Product (UPC, UPC_prom, id_product, selling_price, products_number, promotional_product)
    VALUES (:UPC, :UPCprom, :productId, :price, :quantity, :promotional)
    """)
    void create(@BindMethods StoreProduct storeProduct);

    @SqlUpdate("""
    UPDATE Store_Product SET UPC_prom = :UPCprom, id_product = :productId,
    selling_price = :price, products_number = :quantity, promotional_product = :promotional
    WHERE UPC = :UPC
    """)
    void update(@BindMethods StoreProduct storeProduct);

    @SqlUpdate("DELETE FROM Store_Product WHERE UPC = :upc")
    void delete(@Bind("upc") String upc);

    @SqlQuery("""
    SELECT sp.UPC, sp.UPC_prom, sp.id_product, p.product_name, c.category_name,
           sp.selling_price, sp.products_number, sp.promotional_product,
        CASE
            WHEN sp.promotional_product = TRUE AND orig_sp.selling_price > 0
            THEN ROUND(((orig_sp.selling_price - sp.selling_price) / orig_sp.selling_price) * 100, 2)
            ELSE 0.0
        END AS discount
    FROM Store_Product sp
    JOIN Product p ON sp.id_product = p.id_product
    JOIN Category c ON p.category_number = c.category_number
    LEFT JOIN Store_Product orig_sp ON sp.UPC_prom = orig_sp.UPC
    WHERE sp.UPC = :upc
    """)
    Optional<StoreProductDetailsDTO> findById(@Bind("upc") String upc);

    @SqlQuery("""
    SELECT sp.id_product
    FROM Store_Product sp
    JOIN Product p ON sp.id_product = p.id_product
    WHERE sp.UPC = :upc
    """)
    Integer findProductIdByUPC(@Bind("upc") String upc);

    @SqlQuery("""
    SELECT sp.UPC, p.product_name, sp.selling_price, sp.products_number, sp.promotional_product
    FROM Store_Product sp
    JOIN Product p ON sp.id_product = p.id_product
    ORDER BY p.product_name
    """)
    List<StoreProductListDTO> findAll();

    @SqlQuery("""
    SELECT sp.UPC, sp.UPC_prom, sp.id_product, p.product_name, c.category_name,
           sp.selling_price, sp.products_number, sp.promotional_product,
        CASE
            WHEN sp.promotional_product = TRUE AND orig_sp.selling_price > 0
            THEN ROUND(((orig_sp.selling_price - sp.selling_price) / orig_sp.selling_price) * 100, 2)
            ELSE 0.0
        END AS discount
    FROM Store_Product sp
    JOIN Product p ON sp.id_product = p.id_product
    JOIN Category c ON p.category_number = c.category_number
    LEFT JOIN Store_Product orig_sp ON sp.UPC_prom = orig_sp.UPC
    ORDER BY p.product_name
    """)
    List<StoreProductDetailsDTO> findAllDetails();

    @SqlQuery("""
    SELECT sp.UPC, p.product_name, sp.selling_price, sp.products_number, sp.promotional_product
    FROM Store_Product sp
    JOIN Product p ON sp.id_product = p.id_product
    WHERE 1=1
    <if(filter.upc)> AND sp.UPC LIKE :upc || '%' <endif>
    <if(filter.productName)> AND p.product_name = :productName <endif>
    <if(filter.categoryId)> AND p.category_number = :categoryId <endif>
    AND (:promotional IS NULL OR promotional_product = :promotional)
    ORDER BY <if(filter.sortBy)><filter.sortBy.column><else>p.product_name<endif>
    """)
    List<StoreProductListDTO> findByFilter(@BindBean @Define("filter") StoreProductFilter filter);

    @SqlUpdate("""
    UPDATE Store_Product SET products_number = products_number - :quantity
    WHERE UPC = :upc AND products_number >= :quantity
    """)
    void sellStoreProduct(@Bind("upc") String upc, @Bind("quantity") int quantity);

    @SqlQuery("SELECT EXISTS(SELECT 1 FROM Store_Product WHERE id_product = :id)")
    boolean existsByProductId(@Bind("id") int id);

    @SqlQuery("SELECT EXISTS(SELECT 1 FROM Store_Product WHERE UPC_prom = :upc)")
    boolean isUsedAsPromoBase(@Bind("upc") String upc);

    @SqlQuery("SELECT products_number FROM Store_Product WHERE UPC = :upc")
    int getQuantityByUPC(@Bind("upc") String upc);

    @SqlQuery("SELECT selling_price FROM Store_Product WHERE UPC = :upc")
    double getPriceByUPC(@Bind("upc") String upc);

    @SqlQuery("SELECT EXISTS(SELECT 1 FROM Store_Product WHERE UPC = :upc)")
    boolean existsByUPC(@Bind("upc") String upc);
}