package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterRest  {

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort outputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort outputPortMongo;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    private TelefonoMapperRest mapperRest;

    PersonInputPort inputPort;
    PhoneInputPort phoneInputPort;

    private String setOutputPortInjection(String option) throws InvalidOptionException {
        if (option.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            inputPort = new PersonUseCase(outputPortMaria);
            phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (option.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            inputPort = new PersonUseCase(outputPortMongo);
            phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + option);
        }
    }

    public List<TelefonoResponse> Historial(String option) {
        log.info("Into confusingHistorial Entity in Confusing Adapter");
        try {
            if (setOutputPortInjection(option).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return phoneInputPort.findAll().stream().map(mapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return phoneInputPort.findAll().stream().map(mapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }

        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

    public TelefonoResponse CreateTelefono(TelefonoRequest request) {
        try {
            String option = setOutputPortInjection(request.getDatabase());
            Person person = inputPort.findOne(Integer.valueOf(request.getOwnerId()));
            Phone phone = phoneInputPort.create(mapperRest.fromAdapterToDomain(request, person));
            if (option.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return mapperRest.fromDomainToAdapterRestMaria(phone);
            } else {
                return mapperRest.fromDomainToAdapterRestMongo(phone);
            }
        } catch (InvalidOptionException | NumberFormatException | NoExistException e) {
            log.warn(e.getMessage());
            return new TelefonoResponse("", "", "", "", "");
        }
    }

    public TelefonoResponse GetTelefono(String option, String number) {
        log.info("Into ObtenerTelefono Entity in  Adapter");
        try {
            if (setOutputPortInjection(option).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return mapperRest.fromDomainToAdapterRestMaria(phoneInputPort.findOne(number));
            } else {
                return mapperRest.fromDomainToAdapterRestMongo(phoneInputPort.findOne(number));
            }
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new TelefonoResponse("", "", "", "", "");
        }
    }

    public TelefonoResponse EditTelefono(TelefonoRequest request) {
        log.info("Into EditTelefono Entity in  Adapter");
        try {
            String option = setOutputPortInjection(request.getDatabase());
            Person person = inputPort.findOne(Integer.valueOf(request.getOwnerId()));
            Phone phone = phoneInputPort.edit(request.getNumber(), mapperRest.fromAdapterToDomain(request, person));
            if (option.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return mapperRest.fromDomainToAdapterRestMaria(phone);
            } else {
                return mapperRest.fromDomainToAdapterRestMongo(phone);
            }
        } catch (InvalidOptionException | NumberFormatException | NoExistException e) {
            log.warn(e.getMessage());
            return new TelefonoResponse("", "", "", "", "");
        }
    }

    public Boolean DeleteTelefono (String option, String number) {
        log.info("Into EliminarTelefono Entity in  Adapter");
        try {
            setOutputPortInjection(option);
            return phoneInputPort.drop(number);
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return false;
        }
    }
}
