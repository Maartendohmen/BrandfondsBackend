package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.DepositRequest;

import java.util.List;

public interface IDepositRequestService {

    public abstract List<DepositRequest> GetAll();

    public abstract DepositRequest GetOne(Integer id);

    public abstract void Save(DepositRequest depositRequest);

    public abstract void Delete(DepositRequest depositRequest);
}
