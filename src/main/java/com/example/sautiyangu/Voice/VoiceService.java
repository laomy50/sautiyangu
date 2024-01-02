package com.example.sautiyangu.Voice;

import com.example.sautiyangu.Repository.VoiceRepository;
import com.example.sautiyangu.model.VoiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class VoiceService {
    private final VoiceRepository voiceRepository;

    @Autowired
    public VoiceService(VoiceRepository voiceRepository) {
        this.voiceRepository = voiceRepository;
    }

    @Transactional
    public void storeVoiceData(byte[] voiceData) {
        VoiceEntity voiceEntity = new VoiceEntity(voiceData);
        voiceRepository.save(voiceEntity);
    }
}
