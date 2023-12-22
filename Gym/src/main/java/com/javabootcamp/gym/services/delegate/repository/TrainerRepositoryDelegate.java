package com.javabootcamp.gym.services.delegate.repository;

import com.javabootcamp.gym.data.model.Trainer;
import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Supplier;

@Component
@Transactional
public class TrainerRepositoryDelegate implements IRepositoryDelegate<Trainer> {
    private final TrainerRepository repository;

    @Autowired
    public TrainerRepositoryDelegate(TrainerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Trainer save(Trainer trainer) throws IllegalArgumentException, OptimisticLockingFailureException {
        return repository.save(trainer);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return repository.findFirstByUserUsername(username);
    }

    @Override
    public void delete(Trainer trainer)throws IllegalArgumentException, OptimisticLockingFailureException {
        repository.delete(trainer);
    }

    public Trainer saveWith(Trainer trainer, Supplier<User> userSupplier) {
        var user = userSupplier.get();
        trainer.setUser(user);
        return save(trainer);
    }
}
