package com.example.sautiyangu.Repository;

import com.example.sautiyangu.model.VoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceRepository extends JpaRepository<VoiceEntity, Long> {

}