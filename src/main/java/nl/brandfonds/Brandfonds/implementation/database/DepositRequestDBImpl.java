package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IDepositRequestService;
import nl.brandfonds.Brandfonds.model.DepositRequest;
import nl.brandfonds.Brandfonds.repository.DepositRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositRequestDBImpl implements IDepositRequestService {

    @Autowired
    DepositRequestRepository depositRequestRepository;

    @Override
    public List<DepositRequest> GetAll() {
        return depositRequestRepository.findAll();
    }

    @Override
    public DepositRequest GetOne(Integer id) {
        return depositRequestRepository.getOne(id);
    }

    @Override
    public void Save(DepositRequest depositRequest) {
        depositRequestRepository.save(depositRequest);
    }

    @Override
    public void Delete(DepositRequest depositRequest) {
        depositRequestRepository.delete(depositRequest);
    }
}
