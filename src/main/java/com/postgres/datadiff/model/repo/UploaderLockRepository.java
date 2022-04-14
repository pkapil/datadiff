package com.postgres.datadiff.model.repo;


import com.postgres.datadiff.model.UploaderLock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableJpaRepositories
public interface UploaderLockRepository extends CrudRepository<UploaderLock, Long> {
    @Query(value = "select distinct u.store from uploader_lock u", nativeQuery = true)
    List<String> getDistinctStore();
}
