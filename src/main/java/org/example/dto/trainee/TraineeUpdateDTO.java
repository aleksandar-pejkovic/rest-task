package org.example.dto.trainee;

import java.util.Date;

import lombok.Getter;

@Getter
public class TraineeUpdateDTO {

    private long id;

    private String username;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String address;

    private boolean isActive;
}
