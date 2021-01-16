package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.RegisterRequest;

import java.util.List;
import java.util.Optional;

public interface IRegisterRequestService {

    List<RegisterRequest> getAll();

    RegisterRequest getOne(Integer id);

    void save(RegisterRequest registerRequest);

    void delete(RegisterRequest registerRequest);

    Optional<RegisterRequest> getByrandomString(String randomString);

}
