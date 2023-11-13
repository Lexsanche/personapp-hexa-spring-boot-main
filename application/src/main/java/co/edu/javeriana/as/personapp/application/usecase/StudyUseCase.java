package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

    private StudyOutputPort studyPersintance;

    public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersintance) {
        this.studyPersintance = studyPersintance;
    }

    @Override
    public void setPersistence(StudyOutputPort studyOutputPort) {
        this.studyPersintance = studyOutputPort;
    }

    @Override
    public Study create(Study study) {
        log.debug("Into create on Application Domain");
        return this.studyPersintance.save(study);
    }

    @Override
    public Study edit(Integer identificationPerson, Integer identificationProfession, Study study)
            throws NoExistException {
        Study oldstudy = this.studyPersintance.findById(identificationPerson, identificationProfession);
        if (oldstudy != null)
            return this.studyPersintance.save(study);
        throw new NoExistException(
                "The study with person id " + identificationPerson + " and profession id " + identificationProfession
                        + " does not exists, cannot be edited");
    }

    @Override
    public Boolean drop(Integer identificationPerson, Integer identificationProfession) throws NoExistException {
        Study oldstudy = studyPersintance.findById(identificationPerson, identificationProfession);
        if (oldstudy != null)
            return studyPersintance.delete(identificationPerson, identificationProfession);
        throw new NoExistException(
                "The study with person id " + identificationPerson + " and profession id " + identificationProfession
                        + " does not exist into db, cannot be dropped");
    }

    @Override
    public List<Study> findAll() {
        log.info("Output: " + studyPersintance.getClass());
        return studyPersintance.find();
    }

    @Override
    public Study findOne(Integer identificationPerson, Integer identificationProfession) throws NoExistException {
        Study oldstudy = studyPersintance.findById(identificationPerson, identificationProfession);
        if (oldstudy != null)
            return oldstudy;
        throw new NoExistException(
                "The study with person id " + identificationPerson + " and profession id " + identificationProfession
                        + "does not exist into db, cannot be found");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

}
