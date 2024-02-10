package nl.brandfonds.Brandfonds.services;

import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.repository.DepositRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositRequestService {

    @Autowired
    private DepositRequestRepository depositRequestRepository;

    public List<DepositRequest> getAll() {
        return depositRequestRepository.findAll();
    }

    public DepositRequest getById(Integer id) {
        return depositRequestRepository.findById(id).orElseThrow(() -> new NotFoundException.DepositRequestNotFoundException(id));
    }

    public void save(DepositRequest depositRequest) {
        depositRequestRepository.save(depositRequest);
    }
}
