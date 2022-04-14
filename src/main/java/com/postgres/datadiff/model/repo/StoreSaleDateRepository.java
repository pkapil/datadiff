package com.postgres.datadiff.model.repo;


import com.postgres.datadiff.model.StoreSaleDateData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableJpaRepositories
public interface StoreSaleDateRepository extends CrudRepository<StoreSaleDateData, Long> {
    @Query(value = "select distinct s.store from store_sale_date_data s", nativeQuery = true)
    List<String> getDistinctStore();

}
