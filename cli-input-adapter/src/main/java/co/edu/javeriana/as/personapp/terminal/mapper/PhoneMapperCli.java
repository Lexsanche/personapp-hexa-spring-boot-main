package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.PhoneModelCli;

@Mapper
public class PhoneMapperCli {

    public PhoneModelCli fromDomainToAdapterCli(Phone phone) {
        PhoneModelCli phoneModelCli = new PhoneModelCli();
        phoneModelCli.setNumber(phone.getNumber());
        phoneModelCli.setCompany(phone.getCompany());
        phoneModelCli.setOwnerCc(phone.getOwner().getIdentification());
        phoneModelCli.setOwnerName(phone.getOwner().getFirstName());
        phoneModelCli.setOwnerLastName(phone.getOwner().getLastName());
        phoneModelCli.setOwnerGender(phone.getOwner().getGender().toString());
        phoneModelCli.setOwnerAge(phone.getOwner().getAge());
        return phoneModelCli;
    }
}
