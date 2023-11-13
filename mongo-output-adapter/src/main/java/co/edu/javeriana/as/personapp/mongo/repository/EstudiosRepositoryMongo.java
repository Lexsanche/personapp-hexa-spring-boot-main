package co.edu.javeriana.as.personapp.mongo.repository;

import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EstudiosRepositoryMongo extends MongoRepository<EstudiosDocument, String> {

}
