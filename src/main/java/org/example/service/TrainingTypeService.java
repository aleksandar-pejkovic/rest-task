package org.example.service;

import org.example.dao.TrainingTypeDAO;
import org.example.model.TrainingType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrainingTypeService {

    private final TrainingTypeDAO trainingTypeDAO;

    public TrainingTypeService(TrainingTypeDAO trainingTypeDAO) {
        this.trainingTypeDAO = trainingTypeDAO;
    }

    @Transactional(readOnly = true)
    public TrainingType findTrainingTypeById(long id) {
        return trainingTypeDAO.findTrainingTypeById(id);
    }
}
