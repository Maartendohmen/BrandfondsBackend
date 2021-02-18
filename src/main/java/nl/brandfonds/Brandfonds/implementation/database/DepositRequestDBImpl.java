package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IDepositRequestService;
import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.repository.DepositRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepositRequestDBImpl implements IDepositRequestService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordChangeRequestDBImpl.class);

    @Autowired
    DepositRequestRepository depositRequestRepository;

    @Override
    public List<DepositRequest> getAll() {
        return depositRequestRepository.findAll();
    }

    @Override
    public Optional<DepositRequest> getByID(Integer id) {
        return depositRequestRepository.findById(id);
    }

    @Override
    public void save(DepositRequest depositRequest) {
        depositRequestRepository.save(depositRequest);
        logger.info("A deposit request for user {} {} with mailadres {} was created for {} euro",
                depositRequest.getUser().getForname(), depositRequest.getUser().getSurname(),
                depositRequest.getUser().getEmailadres(), depositRequest.getAmount());
    }

    @Override
    public void delete(DepositRequest depositRequest) {
        depositRequestRepository.delete(depositRequest);
        logger.info("A deposit request for user {} {} with mailadres {} was deleted",
                depositRequest.getUser().getForname(), depositRequest.getUser().getSurname(),
                depositRequest.getUser().getEmailadres());
    }
}
