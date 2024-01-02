package com.example.sautiyangu.Service;

import com.example.sautiyangu.DTOs.ReportReDto;
import com.example.sautiyangu.DTOs.ReportResDto;
import com.example.sautiyangu.Repository.ReportRepository;
import com.example.sautiyangu.controller.ResourceNotFoundException;
import com.example.sautiyangu.model.Report;
import com.example.sautiyangu.model.Reporter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private FileStorageService fileStorageService;
    private final ModelMapper modelmapper;

    public ReportService(ReportRepository reportRepository, FileStorageService fileStorageService, ModelMapper modelMapper) {
        this.reportRepository = reportRepository;
        this.fileStorageService = fileStorageService;
        this.modelmapper = modelMapper;

    }

    //    file information
//    public static final String DIRECTORY = System.getProperty("user.home") + "/Downloads";

    public List<ReportResDto> getAllReports( ) {

        List<ReportResDto> arrayList = new ArrayList<>();
        for (Report report :reportRepository.findAll()){
            ReportResDto reportResDto = modelmapper.map(report, ReportResDto.class);
//            ReportResDto uyu ana report model, ikisha unamuingiza reporter ktk table la report
            reportResDto.setReporterId(report.getReporter().getReporterId());
            reportResDto.setReporterType(report.getReporter().getReporterType());
            arrayList.add(reportResDto);
        }
        return arrayList;
    }

    public Report createReports(ReportReDto reportReDto) {
        // Validate if date is null
        if (reportReDto.getDate() == null) {
            throw new IllegalArgumentException("Date must be filled");
        }
        Report rp=modelmapper.map(reportReDto,Report.class);
        rp.setReporter(new Reporter(reportReDto.getReporter()));
        rp.setStatus("Pending");
        // Store the file
        MultipartFile caseFile = reportReDto.getCaseFile();
        if (caseFile != null) {
            String fileName = fileStorageService.storeFile(caseFile);
            rp.setCaseFileName(fileName);
        }

        // Save the report entity
        return reportRepository.save(rp);
    }

    public Report updateStatus(Long reportId, String status){
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid id"));

        report.setStatus(status);
        return reportRepository.save(report);
    }





}
