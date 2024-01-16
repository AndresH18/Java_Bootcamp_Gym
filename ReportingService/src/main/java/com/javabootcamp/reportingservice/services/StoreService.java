package com.javabootcamp.reportingservice.services;

import com.javabootcamp.reportingservice.data.TrainingMessage;
import com.javabootcamp.reportingservice.data.mongo.TrainingSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class StoreService implements IStoreService<TrainingMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreService.class);

    private final MongoTemplate template;

    @Autowired
    public StoreService(MongoTemplate template) {
        this.template = template;
    }

    @Override
    public boolean store(TrainingMessage trainingMessage) {
        LOGGER.info("Store message: {}", trainingMessage);
        Query query = new Query(Criteria.where("_id").is(trainingMessage.trainerUsername()));


        Update update = getUpdate(trainingMessage);

        var result = template.upsert(query, update, TrainingSummary.class);

        return result.wasAcknowledged();
    }

    private Update getUpdate(TrainingMessage message) {
        Update u = new Update();

//        u.set("_id", message.trainerUsername());
        u.set("firstName", message.trainerFirstName());
        u.set("lastName", message.trainerLastName());
        u.set("active", message.active());
        u.inc("summary." + message.year() + "." + message.month(), (message.delete() ? -1 : 1) * message.duration());

        return u;
    }

//    void m() {
//        Query query = new Query(Criteria.where("username").is("username-value"));
//
//        Update update = new Update().inc("my.value", 1);
//
//        template.updateFirst(query, update, Class);
//    }
}
