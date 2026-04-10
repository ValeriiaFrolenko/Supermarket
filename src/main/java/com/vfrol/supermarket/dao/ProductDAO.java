package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.dto.product.ProductDetailsDTO;
import com.vfrol.supermarket.dto.product.ProductListDTO;
import com.vfrol.supermarket.dto.product.ProductNameDTO;
import com.vfrol.supermarket.entity.Product;
import com.vfrol.supermarket.filter.ProductFilter;
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
@RegisterConstructorMapper(Product.class)
@RegisterConstructorMapper(ProductDetailsDTO.class)
@RegisterConstructorMapper(ProductListDTO.class)
@RegisterConstructorMapper(ProductNameDTO.class)
public interface ProductDAO {

    @SqlUpdate("""
    INSERT INTO Product (category_number, product_name, characteristics)
    VALUES (:categoryId, :name, :characteristics)
    """)
    void create(@BindMethods Product product);

    @SqlUpdate("""
    UPDATE Product SET category_number = :categoryId, product_name = :name, characteristics = :characteristics
    WHERE id_product = :id
    """)
    void update(@BindMethods Product product);

    @SqlUpdate("DELETE FROM Product WHERE id_product = :id")
    void delete(@Bind("id") int id);

    @SqlQuery("""
    SELECT p.id_product, p.product_name, p.category_number, c.category_name, p.characteristics
    FROM Product p
    JOIN Category c ON p.category_number = c.category_number
    WHERE p.id_product = :id
    """)
    Optional<ProductDetailsDTO> findById(@Bind("id") int id);

    @SqlQuery("""
    SELECT id_product, product_name FROM Product
    WHERE product_name LIKE '%' || :name || '%'
    ORDER BY product_name
    """)
    List<ProductNameDTO> findByName(@Bind("name") String name);

    @SqlQuery("""
    SELECT p.id_product, p.product_name, c.category_name
    FROM Product p
    JOIN Category c ON p.category_number = c.category_number
    ORDER BY p.product_name
    """)
    List<ProductListDTO> findAll();

    @SqlQuery("""
    SELECT p.id_product, p.product_name
    FROM Product p
    ORDER BY p.product_name
    """)
    List<ProductNameDTO> findAllNames();

    @SqlQuery("""
    SELECT p.id_product, p.product_name, p.category_number, c.category_name, p.characteristics
    FROM Product p
    JOIN Category c ON p.category_number = c.category_number
    ORDER BY p.product_name
    """)
    List<ProductDetailsDTO> findAllDetails();

    @SqlQuery("""
    SELECT p.id_product, p.product_name, c.category_name
    FROM Product p
    JOIN Category c ON p.category_number = c.category_number
    WHERE 1=1
    <if(filter.name)> AND p.product_name LIKE '%' || :name || '%' <endif>
    <if(filter.categoryId)> AND p.category_number = :categoryId <endif>
    ORDER BY <if(filter.sortBy)><filter.sortBy.column><else>product_name<endif>
    """)
    List<ProductListDTO> findByFilter(@BindBean @Define("filter") ProductFilter filter);

    @SqlQuery("SELECT EXISTS (SELECT 1 FROM Product WHERE category_number = :id)")
    boolean existsByCategoryId(@Bind("id") int id);
}