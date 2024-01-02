package com.example.sautiyangu.Service;

import com.example.sautiyangu.Repository.ReporterRepository;
import com.example.sautiyangu.model.Reporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReporterService {

    @Autowired
    private ReporterRepository reporterRepository;

    public ReporterService(ReporterRepository reporterRepository) {
        this.reporterRepository = reporterRepository;
    }

    public Reporter addReporterType(Reporter reporter){
        return reporterRepository.save(reporter);
    }
}
