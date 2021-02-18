package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IRegisterRequestService;
import nl.brandfonds.Brandfonds.model.RegisterRequest;
import nl.brandfonds.Brandfonds.repository.RegisterRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegisterRequestDBImpl implements IRegisterRequestService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterRequest.class);

    @Autowired
    RegisterRequestRepository registerRequestRepository;

    @Override
    public List<RegisterRequest> getAll() {
        return registerRequestRepository.findAll();
    }

    @Override
    public RegisterRequest getOne(Integer id) {
        return registerRequestRepository.getOne(id);
    }

    @Override
    public void save(RegisterRequest registerRequest) {
        registerRequestRepository.save(registerRequest);
        logger.info("A register request for user {} {} with mailadres {} was created",
                registerRequest.getForname(), registerRequest.getSurname(), registerRequest.getEmailadres());
    }

    @Override
    public void delete(RegisterRequest registerRequest) {
        registerRequestRepository.delete(registerRequest);
        logger.info("A register request for user {} {} with mailadres {} was deleted",
                registerRequest.getForname(), registerRequest.getSurname(), registerRequest.getEmailadres());
    }

    @Override
    public Optional<RegisterRequest> getByrandomString(String randomString) {
        return registerRequestRepository.getByrandomString(randomString);
    }
}
