package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IDepositRequestService;
import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.repository.DepositRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepositRequestDBImpl implements IDepositRequestService {

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
    }

    @Override
    public void delete(DepositRequest depositRequest) {
        depositRequestRepository.delete(depositRequest);
    }
}
