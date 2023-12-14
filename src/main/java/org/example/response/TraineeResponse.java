package org.example.response;

import java.util.Date;
import java.util.List;

import org.example.dto.TrainerDTO;

import lombok.Builder;

@Builder
public class TraineeResponse {

    private long id;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String address;

    private boolean isActive;

    private List<TrainerDTO> trainerDTOList;

}
