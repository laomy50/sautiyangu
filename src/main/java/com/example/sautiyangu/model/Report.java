package com.example.sautiyangu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "reports")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportId;
    private String location;
    private String nameOfCrime;
    private String caseName;
    private String date;
    private String caseFileName;
    private String status;
    private String description;


    public long getReportId() {
        return reportId;
    }



    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    @ManyToOne
    private Reporter reporter;
    public Report(Long reportId){
        this.reportId=reportId;

//    Reporter mmoja anarepoti case zaidi ya moja , hivo basi ili relation ikamilike lazima awepo yule anaereletiwa (reporter)
    }


}
