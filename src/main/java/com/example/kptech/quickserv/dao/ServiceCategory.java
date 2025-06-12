package com.example.kptech.quickserv.dao;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Data
@Document(collection="service_catagories")
public class ServiceCategory {

    @Id
    private String id;

    @Field("category_id")
    private Integer categoryId;

    @Field("category_name")
    private String categoryName;

    @Field("description")
    private String description;

    @Field("subcategories")
    private List<String> subcategories;






}
