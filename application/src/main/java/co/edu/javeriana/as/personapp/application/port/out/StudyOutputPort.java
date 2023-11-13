package co.edu.javeriana.as.personapp.application.port.out;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Study;

import java.util.List;

@Port
public interface StudyOutputPort {
    public Study save(Study study);

    public Boolean delete(Integer person_identification, Integer profession_identification);

    public List<Study> find();

    public Study findById(Integer person_identification, Integer profession_identification);
}
