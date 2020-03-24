package com.ionutradu.mongodb.employeeapp.documents;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "idGenerator")
public class IdGenerator {

    @Id
    private String id;

    private long seq;

    public IdGenerator() {
    }

    public IdGenerator(long seq) {
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }
}
