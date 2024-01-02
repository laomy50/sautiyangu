package com.example.sautiyangu.DTOs;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReportReDto {
    private String location;
    private String nameOfCrime;
    private String caseName;
    private String date;
    private MultipartFile caseFile;
    private String description;
    private String status;

    private  Long reporter;


}
