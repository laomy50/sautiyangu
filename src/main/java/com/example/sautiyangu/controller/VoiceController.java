package com.example.sautiyangu.controller;

import com.example.sautiyangu.Voice.VoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/voice")
public class VoiceController {
    @Autowired
    private VoiceService voiceService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVoice(@RequestParam("voiceData") MultipartFile voiceData) {
        try {
            //  store the voice data using the voiceService
            voiceService.storeVoiceData(voiceData.getBytes());
            return ResponseEntity.ok("Voice recording uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading voice recording");
        }
    }
}
