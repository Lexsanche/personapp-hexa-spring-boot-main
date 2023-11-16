package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudiosMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import co.edu.javeriana.as.personapp.model.response.EstudiosResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudiosInputAdapterRest {

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    private EstudiosMapperRest mapperRest;

    PersonInputPort personInputPort;

    ProfessionInputPort professionInputPort;

    StudyInputPort studyInputPort;

    private String setStudyOutputPortInjection(String option) throws InvalidOptionException {
        if (option.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMaria);
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (option.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMongo);
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + option);
        }
    }

    public List<EstudiosResponse> Historial(String option) {
        log.info("Into Historial EstudiosEntity in  Adapter");
        try {
            if (setStudyOutputPortInjection(option).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyInputPort.findAll().stream().map(mapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return studyInputPort.findAll().stream().map(mapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }

        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<EstudiosResponse>();
        }
    }

    public EstudiosResponse CreateEstudios(EstudiosRequest request) {
        try {
            String option = setStudyOutputPortInjection(request.getDatabase());
            Person person = personInputPort.findOne(Integer.valueOf(request.getPersonCC()));
            Profession profession = professionInputPort.findOne(Integer.valueOf(request.getProfessionId()));
            Study study = studyInputPort.create(mapperRest.fromAdapterToDomain(request, person, profession));
            if (option.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return mapperRest.fromDomainToAdapterRestMaria(study);
            } else {
                return mapperRest.fromDomainToAdapterRestMongo(study);
            }
        } catch (InvalidOptionException | NumberFormatException | NoExistException e) {
            log.warn(e.getMessage());
            return new EstudiosResponse(null, null, "", "", "", "");
        }
    }

    public EstudiosResponse GetEstudios(String option, String idProf, String ccPer) {
        log.info("Into ObtenerEstudios EstudiosEntity in  Adapter");
        try {
            if (setStudyOutputPortInjection(option).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return mapperRest.fromDomainToAdapterRestMaria(
                        studyInputPort.findOne(Integer.parseInt(idProf), Integer.parseInt(ccPer)));
            } else {
                return mapperRest.fromDomainToAdapterRestMongo(
                        studyInputPort.findOne(Integer.parseInt(idProf), Integer.parseInt(ccPer)));
            }
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new EstudiosResponse(null, null, "", "", "", "");
        }
    }

    public EstudiosResponse EditEstudios(EstudiosRequest request) {
        log.info("Into EditEstudios EstudiosEntity in  Adapter");
        try {
            String option = setStudyOutputPortInjection(request.getDatabase());
            Person person = personInputPort.findOne(Integer.valueOf(request.getPersonCC()));
            Profession profession = professionInputPort.findOne(Integer.valueOf(request.getProfessionId()));
            Study phone = studyInputPort.edit(Integer.parseInt(request.getProfessionId()),
                    Integer.parseInt(request.getPersonCC()),
                    mapperRest.fromAdapterToDomain(request, person, profession));
            if (option.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return mapperRest.fromDomainToAdapterRestMaria(phone);
            } else {
                return mapperRest.fromDomainToAdapterRestMongo(phone);
            }
        } catch (InvalidOptionException | NumberFormatException | NoExistException e) {
            log.warn(e.getMessage());
            return new EstudiosResponse(null, null, "", "", "", "");
        }
    }

    public Boolean DeleteEstudios(String option, String idProf, String ccPer) {
        log.info("Into EliminarEstudios EstudiosEntity in  Adapter");
        try {
            setStudyOutputPortInjection(option);
            return studyInputPort.drop(Integer.parseInt(idProf), Integer.parseInt(ccPer));
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return false;
        }
    }
}
