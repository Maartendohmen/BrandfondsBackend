package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.RegisterRequest;

import java.util.Optional;

public interface IRegisterRequestService extends IBaseService<RegisterRequest> {

    Optional<RegisterRequest> getByRandomString(String randomString);

}
