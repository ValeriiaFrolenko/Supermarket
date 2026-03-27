package com.vfrol.supermarket.dto.category;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public record CategoryListDTO(
        @ColumnName("category_number") int id,
        @ColumnName("category_name") String name
) {}