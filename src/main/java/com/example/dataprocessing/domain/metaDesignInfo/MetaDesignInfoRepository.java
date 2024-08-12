package com.example.dataprocessing.domain.metaDesignInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetaDesignInfoRepository extends JpaRepository<MetaDesignInfo, Long> {
    List<MetaDesignInfo> findByApplicationNumber(String registrationNumber);
}
