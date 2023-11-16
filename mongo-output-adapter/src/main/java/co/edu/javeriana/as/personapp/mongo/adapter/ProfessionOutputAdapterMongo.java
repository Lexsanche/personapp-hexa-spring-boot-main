package co.edu.javeriana.as.personapp.mongo.adapter;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import com.mongodb.MongoWriteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {

    private final ProfesionRepositoryMongo profesionRepositoryMongo;
    private final ProfesionMapperMongo profesionMapperMongo;

    @Autowired
    public ProfessionOutputAdapterMongo(ProfesionRepositoryMongo profesionRepositoryMongo, ProfesionMapperMongo profesionMapperMongo) {
        this.profesionRepositoryMongo = profesionRepositoryMongo;
        this.profesionMapperMongo = profesionMapperMongo;
    }

    @Override
    public Profession save(Profession profession) {
        log.debug("Into save on Adapter MongoDB");
        try {
            ProfesionDocument persistedProfesion = profesionRepositoryMongo.save(profesionMapperMongo.fromDomainToAdapter(profession));
            return profesionMapperMongo.fromAdapterToDomain(persistedProfesion);
        } catch (MongoWriteException e) {
            log.warn(e.getMessage());
            return profession;
        }
    }

    @Override
    public Boolean delete(Integer identification) {
        log.debug("Into delete on Adapter MongoDB");
        profesionRepositoryMongo.deleteById(identification);
        return profesionRepositoryMongo.findById(identification).isEmpty();
    }

    @Override
    public List<Profession> find() {
        log.debug("Into find on Adapter MongoDB");
        return profesionRepositoryMongo.findAll().stream()
                .map(profesionMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer identification) {
        log.debug("Into findById on Adapter MongoDB");
        return profesionRepositoryMongo.findById(identification)
                .map(profesionMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }
}
