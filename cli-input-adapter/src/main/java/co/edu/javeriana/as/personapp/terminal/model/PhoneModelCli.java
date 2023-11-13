package co.edu.javeriana.as.personapp.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneModelCli {
    private String number;
    private String company;
    private Integer ownerCc;
    private String ownerName;
    private String ownerLastName;
    private String ownerGender;
    private Integer ownerAge;
}
