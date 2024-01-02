package com.example.sautiyangu.DTOs;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReportResDto {

    private long reportId;
    private String location;
    private String nameOfCrime;
    private String caseName;
    private String date;
    private MultipartFile caseFile;
    private String status;
    private String description;

    private Long reporterId;
    private String reporterType;

//    private ReporterResDto reporter;
}
