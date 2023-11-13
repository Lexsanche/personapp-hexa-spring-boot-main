package co.edu.javeriana.as.personapp.application.port.in;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

import java.util.List;

public interface StudyInputPort {

    public void setPersistence(StudyOutputPort studyOutputPort);

    public Study create(Study study);

    public Study edit(Integer identificationPerson, Integer identificationProfession, Study study)
            throws NoExistException;

    public Boolean drop(Integer identificationPerson, Integer identificationProfession) throws NoExistException;

    public List<Study> findAll();

    public Study findOne(Integer identificationPerson, Integer identificationProfession) throws NoExistException;

    public Integer count();
}
