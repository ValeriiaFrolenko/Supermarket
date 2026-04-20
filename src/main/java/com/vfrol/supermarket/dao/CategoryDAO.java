package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.entity.Category;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterConstructorMapper(Category.class)
@RegisterConstructorMapper(CategoryListDTO.class)
public interface CategoryDAO {

    @SqlUpdate("""
    INSERT INTO Category (category_name)
    VALUES (:name)
    """)
    void create(@BindMethods Category category);

    @SqlUpdate("""
    UPDATE Category SET category_name = :name
    WHERE category_number = :id
    """)
    void update(@BindMethods Category category);

    @SqlUpdate("DELETE FROM Category WHERE category_number = :id")
    void delete(@Bind("id") int id);

    @SqlQuery("""
    SELECT category_number, category_name
    FROM Category WHERE category_number = :id
    """)
    Optional<CategoryListDTO> findById(@Bind("id") int id);

    @SqlQuery("""
    SELECT category_number, category_name
    FROM Category ORDER BY category_name
    """)
    List<CategoryListDTO> findAll();

    @SqlQuery("""
    SELECT category_number, category_name
    FROM Category WHERE category_name ILIKE '%' || :name || '%'
    """)
    List<CategoryListDTO> findByName(@Bind("name") String name);
}