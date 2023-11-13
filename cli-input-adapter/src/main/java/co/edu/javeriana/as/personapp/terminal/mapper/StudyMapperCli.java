package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;

@Mapper
public class StudyMapperCli {

    public StudyModelCli fromDomainToAdapterCli(Study study) {
        StudyModelCli studyModelCli = new StudyModelCli();
        studyModelCli.setPersonIdentification(study.getPerson().getIdentification());
        studyModelCli.setPersonFirstName(study.getPerson().getFirstName());
        studyModelCli.setPersonLastName(study.getPerson().getLastName());
        studyModelCli.setProfessionIdentification(study.getProfession().getIdentification());
        studyModelCli.setProfessionName(study.getProfession().getName());
        studyModelCli.setProfessionDescription(study.getProfession().getDescription());
        studyModelCli.setGraduationDate(study.getGraduationDate());
        studyModelCli.setUniversityName(study.getUniversityName());
        return studyModelCli;
    }
}
