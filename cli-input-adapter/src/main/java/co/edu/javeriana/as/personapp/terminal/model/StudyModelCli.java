package co.edu.javeriana.as.personapp.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyModelCli {
    private Integer personIdentification;
    private String personFirstName;
    private String personLastName;
    private Integer professionIdentification;
    private String professionName;
    private String professionDescription;
    private LocalDate graduationDate;
    private String universityName;
}
