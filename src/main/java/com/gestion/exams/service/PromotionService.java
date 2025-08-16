package com.gestion.exams.service;

import com.gestion.exams.entity.Promotion;
import com.gestion.exams.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id).orElseThrow();
    }
}
