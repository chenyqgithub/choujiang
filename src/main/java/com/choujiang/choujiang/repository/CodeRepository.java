package com.choujiang.choujiang.repository;

import com.choujiang.choujiang.entity.CodeInfo;
import com.choujiang.choujiang.entity.LjInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends PagingAndSortingRepository<CodeInfo, Integer>, JpaSpecificationExecutor<CodeInfo> {

    int countAllByCode(String code);
}
