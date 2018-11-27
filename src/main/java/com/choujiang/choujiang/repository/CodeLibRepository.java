package com.choujiang.choujiang.repository;

import com.choujiang.choujiang.entity.CodeInfo;
import com.choujiang.choujiang.entity.CodeLib;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeLibRepository extends PagingAndSortingRepository<CodeLib, String>, JpaSpecificationExecutor<CodeLib> {

    int countById(String id);
}
