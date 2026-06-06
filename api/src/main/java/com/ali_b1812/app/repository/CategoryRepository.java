package com.ali_b1812.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ali_b1812.app.model.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    
}
