package com.ionutradu.mongodb.employeeapp.services;

import com.ionutradu.mongodb.employeeapp.documents.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class IdGeneratorService {

    @Autowired
    MongoOperations mongoOperations;

    public IdGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public long generateSequence(String seqName) {

        IdGenerator counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq", 1), options().returnNew(true).upsert(true),
                IdGenerator.class);
        return !Objects.isNull(counter) ? counter. getSeq() : 1;

    }
}
