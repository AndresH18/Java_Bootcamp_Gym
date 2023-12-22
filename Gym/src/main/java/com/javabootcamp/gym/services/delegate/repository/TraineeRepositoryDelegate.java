package com.javabootcamp.gym.services.delegate.repository;

import com.javabootcamp.gym.data.model.Trainee;
import com.javabootcamp.gym.data.model.User;
import com.javabootcamp.gym.data.repository.TraineeRepository;
import com.javabootcamp.gym.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Supplier;

@Component
@Transactional
public class TraineeRepositoryDelegate implements IRepositoryDelegate<Trainee> {
    private final TraineeRepository repository;

    @Autowired
    public TraineeRepositoryDelegate(TraineeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Trainee save(Trainee trainee) {
        return repository.save(trainee);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return repository.findFirstByUserUsername(username);
    }

    @Override
    public void delete(Trainee trainee) {
        repository.delete(trainee);
    }

    public Trainee saveWith(Trainee trainee, Supplier<User> userSupplier) {
        var user = userSupplier.get();
        trainee.setUser(user);
        return save(trainee);
    }
}
