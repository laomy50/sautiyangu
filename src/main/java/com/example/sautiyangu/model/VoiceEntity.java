package com.example.sautiyangu.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "voice_data")
public class VoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "voice_data", nullable = false)
    private byte[] voiceData;


    public VoiceEntity() {

    }

    public VoiceEntity(byte[] voiceData) {
        this.voiceData = voiceData;
    }
}
