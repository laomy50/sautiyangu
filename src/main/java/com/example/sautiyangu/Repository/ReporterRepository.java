package com.example.sautiyangu.Repository;

import com.example.sautiyangu.model.Reporter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporterRepository extends JpaRepository<Reporter, Long> {

}
