package com.example.sautiyangu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "reporters")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Reporter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reporterId;
    private String reporterType;


    public Reporter(Long reporterId){
        this.reporterId=reporterId;
    }

}
