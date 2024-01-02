package com.example.sautiyangu.controller;


import com.example.sautiyangu.DTOs.ReportReDto;
import com.example.sautiyangu.Repository.ReportRepository;
import com.example.sautiyangu.Service.FileStorageService;
import com.example.sautiyangu.Service.ReportService;
import com.example.sautiyangu.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/api/v1")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ReportRepository reportRepository;

    public ReportController(ReportService reportService, FileStorageService fileStorageService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.fileStorageService = fileStorageService;
        this.reportRepository = reportRepository;
    }

    @GetMapping("/reports")
    public ResponseEntity<?> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable(value = "id") Long reportId)
            throws ResourceNotFoundException {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found for this id :: " + reportId));
        return ResponseEntity.ok().body(report);
    }

//    @PostMapping("/reports")
//    public Report createreports(@Valid @RequestBody ReportReDto reportReDto) {
//        return reportService.createReports( reportReDto);
//    }

    @PostMapping("/reports")
    public ResponseEntity<?> createReport(@ModelAttribute ReportReDto reportReDto) {
        try {
            Report createdReport = reportService.createReports(reportReDto);
            return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            byte[] content = fileStorageService.getFile(fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + fileName)
                    .body(content);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping("/reports/{reportId}")
    public ResponseEntity<Report> updateReport(@PathVariable("reportId") long reportId,@RequestBody Report report){
        Optional<Report> reportData = reportRepository.findById(reportId);

        if (reportData.isPresent()){
            Report report1 = reportData.get();
            report1.setStatus(report.getStatus());

            return new ResponseEntity<>(reportRepository.save(report1), HttpStatus.OK);
        }else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }



    @DeleteMapping("/reports/{id}")
    public Map<String, Boolean> deleteReport(@PathVariable(value = "id") Long reportId)
            throws ResourceNotFoundException {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found for this id :: " + reportId));

        reportRepository.delete(report);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @PutMapping("/report/status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long reportId, @RequestBody String status){
        return ResponseEntity.ok(reportService.updateStatus(reportId,status));
    }


//    image cotroller

//@Autowired
//public ReportService imageService;
//    @PostMapping(value ="upload")
//    public ResponseEntity uploadImage(@RequestParam MultipartFile file){
//        return this.imageService.uploadToLocalFileSystem(file);
//    }
//    @GetMapping(
//            value = "getImage/{imageName:.+}",
//            produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_PNG_VALUE,MediaType.APPLICATION_PDF_VALUE,MediaType.APPLICATION_STREAM_JSON_VALUE}
//    )
//    public @ResponseBody byte[] getImageWithMediaType(@PathVariable(name = "imageName") String fileName) throws IOException, IOException {
//        return this.imageService.getImageWithMediaType(fileName);
//    }
//    Define location to store files
    public static final String DIRECTORY = System.getProperty("user.home")+ "/Downloads/uploads";

//    Define method to upoad
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files")List<MultipartFile> multipartFiles) throws IOException {
        List<String> filenames = new ArrayList<>();
        for (MultipartFile file : multipartFiles){
//            save in computer
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            java.nio.file.Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
            copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
            filenames.add(filename);
        }
        return ResponseEntity.ok().body(filenames);
    }

//    Define method to download file
    @GetMapping("download/{filename}")
    public  ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        java.nio.file.Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
        if (!Files.exists(filePath)){
            throw new FileNotFoundException(filename + "was not found on the server");
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", filename);
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }
}
