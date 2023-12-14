package org.example.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TraineeDTO {

    private long id;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String address;

    private boolean isActive;

    private List<TrainerDTO> trainerList = new ArrayList<>();
}
