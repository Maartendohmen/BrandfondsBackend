package nl.brandfonds.Brandfonds.implementation.database;

import lombok.extern.slf4j.Slf4j;
import nl.brandfonds.Brandfonds.abstraction.IRegisterRequestService;
import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.repository.RegisterRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RegisterRequestDBImpl implements IRegisterRequestService {

    @Autowired
    RegisterRequestRepository registerRequestRepository;

    @Override
    public List<RegisterRequest> getAll() {
        return registerRequestRepository.findAll();
    }

    @Override
    public Optional<RegisterRequest> getOne(Integer id) {
        return registerRequestRepository.findById(id);
    }

    @Override
    public void save(RegisterRequest registerRequest) {
        registerRequestRepository.save(registerRequest);
        log.info("A register request for user {} {} with mailadres {} was created",
                registerRequest.getForename(), registerRequest.getSurname(), registerRequest.getMailadres());
    }

    @Override
    public void delete(RegisterRequest registerRequest) {
        registerRequestRepository.delete(registerRequest);
        log.info("A register request for user {} {} with mailadres {} was deleted",
                registerRequest.getForename(), registerRequest.getSurname(), registerRequest.getMailadres());
    }

    @Override
    public Optional<RegisterRequest> getByRandomString(String randomString) {
        return registerRequestRepository.getByRandomString(randomString);
    }
}
