package com.byma.back_acdits.infrastructure.adapter.out.persistance.repository;

import com.byma.back_acdits.infrastructure.adapter.out.persistance.entity.AcdiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcdiRepository extends JpaRepository<AcdiEntity, Long> {

}
