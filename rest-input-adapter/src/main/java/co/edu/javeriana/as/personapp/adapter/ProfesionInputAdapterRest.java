package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort outputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort outputPortMongo;

    @Autowired
    private ProfesionMapperRest mapperRest;

    ProfessionInputPort inputPort;

    private String setOutputPortInjection(String option) throws InvalidOptionException {
        if (option.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            inputPort = new ProfessionUseCase(outputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (option.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            inputPort = new ProfessionUseCase(outputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + option);
        }
    }

    public List<ProfesionResponse> Historial(String option) {
        log.info("Into Historial ProfesionEntity in  Adapter");
        try {
            if (setOutputPortInjection(option).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return inputPort.findAll().stream().map(mapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return inputPort.findAll().stream().map(mapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }

        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

    public ProfesionResponse CreateProfesion(ProfesionRequest request) {
        try {
            String option = setOutputPortInjection(request.getDatabase());
            Profession profession = inputPort.create(mapperRest.fromAdapterToDomain(request));
            if (option.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return mapperRest.fromDomainToAdapterRestMaria(profession);
            } else {
                return mapperRest.fromDomainToAdapterRestMongo(profession);
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ProfesionResponse("", "", "", "", "");
        }
    }

    public ProfesionResponse GetProfesion(String option, Integer id) {
        log.info("Into ObtenerProfesion ProfesionEntity in  Adapter");
        try {
            if (setOutputPortInjection(option).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return mapperRest.fromDomainToAdapterRestMaria(inputPort.findOne(id));
            } else {
                return mapperRest.fromDomainToAdapterRestMongo(inputPort.findOne(id));
            }
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new ProfesionResponse("", "", "", "", "");
        }
    }

    public ProfesionResponse EditProfesion(ProfesionRequest request) {
        log.info("IntoEditProfesion ProfesionEntity in  Adapter");
        try {
            String option = setOutputPortInjection(request.getDatabase());
            Profession profession = inputPort.edit(Integer.parseInt(request.getIdentification()),
                    mapperRest.fromAdapterToDomain(request));
            if (option.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return mapperRest.fromDomainToAdapterRestMaria(profession);
            } else {
                return mapperRest.fromDomainToAdapterRestMongo(profession);
            }
        } catch (InvalidOptionException | NumberFormatException | NoExistException e) {
            log.warn(e.getMessage());
            return new ProfesionResponse("", "", "", "", "");
        }
    }

    public Boolean DeleterProfesion(String option, Integer id) {
        log.info("Into EliminarProfesion ProfesionEntity in  Adapter");
        try {
            setOutputPortInjection(option);
            return inputPort.drop(id);
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return false;
        }
    }
}
