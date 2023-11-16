package co.edu.javeriana.as.personapp.mongo.adapter;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import com.mongodb.MongoWriteException;
import com.mongodb.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StudyOutputAdapterMongo implements StudyOutputPort {

    private final EstudiosRepositoryMongo estudiosRepositoryMongo;
    private final EstudiosMapperMongo estudiosMapperMongo;

    @Autowired
    public StudyOutputAdapterMongo(EstudiosRepositoryMongo estudiosRepositoryMongo, EstudiosMapperMongo estudiosMapperMongo) {
        this.estudiosRepositoryMongo = estudiosRepositoryMongo;
        this.estudiosMapperMongo = estudiosMapperMongo;
    }

    @Override
    public Study save(Study study) {
        log.debug("Into save on Adapter MongoDB");
        try {
            EstudiosDocument persistedEstudios = estudiosRepositoryMongo.save(estudiosMapperMongo.fromDomainToAdapter(study));
            return estudiosMapperMongo.fromAdapterToDomain(persistedEstudios);
        } catch (MongoWriteException e) {
            log.warn(e.getMessage());
            return study;
        }
    }

    @Override
    public Boolean delete(Integer person_identification, Integer profession_identification) {
        log.debug("Into delete on Adapter MongoDB");
        estudiosRepositoryMongo.deleteById(validId(person_identification, profession_identification));
        return estudiosRepositoryMongo.findById(validId(person_identification, profession_identification)).isEmpty();
    }

    @Override
    public List<Study> find() {
        log.debug("Into find on Adapter MongoDB");
        return estudiosRepositoryMongo.findAll().stream()
                .map(estudiosMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer person_identification, Integer profession_identification) {
        log.debug("Into findById on Adapter MongoDB");
        return estudiosRepositoryMongo.findById(validId(person_identification, profession_identification))
                .map(estudiosMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }

    private String validId(@NonNull Integer identificationPerson, @NonNull Integer identificationProfession) {
        return identificationPerson + "-" + identificationProfession;
    }
}
