package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@UseCase
public class ProfessionUseCase implements ProfessionInputPort {

    private ProfessionOutputPort professionPersintance;

    public ProfessionUseCase(@Qualifier("professionOutputAdapterMaria") ProfessionOutputPort professionPersintance) {
        this.professionPersintance = professionPersintance;
    }

    @Override
    public void setPersistence(ProfessionOutputPort professionOutputPort) {
        this.professionPersintance = professionOutputPort;
    }

    @Override
    public Profession create(Profession profession) {
        log.debug("Into create on Application Domain");
        return this.professionPersintance.save(profession);
    }

    @Override
    public Profession edit(Integer identification, Profession profession) throws NoExistException {
        Profession oldprofession = this.professionPersintance.findById(identification);
        if (oldprofession != null)
            return this.professionPersintance.save(profession);
        throw new NoExistException("The profession with id " + identification + " does not exists, cannot be edited");
    }

    @Override
    public Boolean drop(Integer identification) throws NoExistException {
        Profession oldprofession = professionPersintance.findById(identification);
        if (oldprofession != null)
            return professionPersintance.delete(identification);
        throw new NoExistException(
                "The profession with id " + identification + " does not exist into db, cannot be dropped");
    }

    @Override
    public List<Profession> findAll() {
        log.info("Output: " + professionPersintance.getClass());
        return professionPersintance.find();
    }

    @Override
    public Profession findOne(Integer identification) throws NoExistException {
        Profession oldprofession = professionPersintance.findById(identification);
        if (oldprofession != null)
            return oldprofession;
        throw new NoExistException(
                "The profession with id " + identification + " does not exist into db, cannot be found");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public List<Study> getStudies(Integer identification) throws NoExistException {
        Profession profession = professionPersintance.findById(identification);
        if (profession != null)
            return profession.getStudies();
        throw new NoExistException(
                "The profession with id " + identification + " does not exist into db, cannot get studies");
    }

}
