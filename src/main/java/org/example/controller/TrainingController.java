package org.example.controller;

import java.util.Date;
import java.util.List;

import org.example.dto.training.TrainingCreateDTO;
import org.example.dto.training.TrainingDTO;
import org.example.dto.trainingType.TrainingTypeDTO;
import org.example.enums.TrainingTypeName;
import org.example.model.Training;
import org.example.service.TrainingService;
import org.example.utils.TrainingConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/trainings", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class TrainingController {

    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping("/trainee")
    public List<TrainingDTO> getTraineeTrainingsList(
            @RequestParam String username,
            @RequestParam(required = false) Date periodFrom,
            @RequestParam(required = false) Date periodTo,
            @RequestParam(required = false) int trainingDuration,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) TrainingTypeName trainingType
    ) {
        List<Training> trainings = trainingService.getTraineeTrainingList(username, trainingDuration);
        return TrainingConverter.convertToDtoList(trainings);
    }

    @GetMapping("/trainer")
    public List<TrainingDTO> getTrainerTrainingsList(
            @RequestParam String username,
            @RequestParam(required = false) Date periodFrom,
            @RequestParam(required = false) Date periodTo,
            @RequestParam(required = false) int trainingDuration,
            @RequestParam(required = false) String traineeName
    ) {
        List<Training> trainings = trainingService.getTrainerTrainingList(username, trainingDuration);
        return TrainingConverter.convertToDtoList(trainings);
    }

    @PostMapping
    public ResponseEntity<Boolean> addTraining(TrainingCreateDTO trainingCreateDTO) {
        boolean successfullyAddedTraining = trainingService.createTraining(trainingCreateDTO);
        return (successfullyAddedTraining)
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().body(false);
    }

    @GetMapping("/training-types")
    public List<TrainingTypeDTO> getAllTrainingTypes() {
        return trainingService.finaAllTrainingTypes();
    }
}
