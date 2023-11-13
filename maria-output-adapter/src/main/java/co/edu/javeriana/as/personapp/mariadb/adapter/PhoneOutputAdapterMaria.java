package co.edu.javeriana.as.personapp.mariadb.adapter;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter("phoneOutputAdapterMaria")
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

    @Autowired
    private TelefonoRepositoryMaria telefonoRepositoryMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    @Override
    public Phone save(Phone phone) {
        log.debug("Iniciando save en Adaptador MariaDB");
        TelefonoEntity persistedTelefono = telefonoRepositoryMaria.save(telefonoMapperMaria.fromDomainToAdapter(phone));
        return telefonoMapperMaria.fromAdapterToDomain(persistedTelefono);
    }

    @Override
    public Boolean delete(String number) {
        log.debug("Iniciando delete en Adaptador MariaDB");
        telefonoRepositoryMaria.deleteById(number);
        return telefonoRepositoryMaria.findById(number).isEmpty();
    }

    @Override
    public List<Phone> find() {
        log.debug("Iniciando find en Adaptador MariaDB");
        return telefonoRepositoryMaria.findAll().stream().map(telefonoMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findById(String number) {
        log.debug("Iniciando findById en Adaptador MariaDB");
        if (telefonoRepositoryMaria.findById(number).isEmpty()) {
            return null;
        } else {
            return telefonoMapperMaria.fromAdapterToDomain(telefonoRepositoryMaria.findById(number).get());
        }
    }
}