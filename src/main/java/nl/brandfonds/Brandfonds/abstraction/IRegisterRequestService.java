package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;
import nl.brandfonds.Brandfonds.model.RegisterRequest;

import java.util.List;

public interface IRegisterRequestService {

    public abstract List<RegisterRequest> GetAll();

    public abstract RegisterRequest GetOne(Integer id);

    public abstract void Save(RegisterRequest registerRequest);

    public abstract void Delete(RegisterRequest registerRequest);

    public abstract RegisterRequest GetByrandomString(String randomString);

}
