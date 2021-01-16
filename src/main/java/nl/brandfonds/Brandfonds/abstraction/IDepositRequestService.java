package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.DepositRequest;

import java.util.List;
import java.util.Optional;

public interface IDepositRequestService {

    List<DepositRequest> getAll();

    Optional<DepositRequest> getByID(Integer id);

    void save(DepositRequest depositRequest);

    void delete(DepositRequest depositRequest);
}
