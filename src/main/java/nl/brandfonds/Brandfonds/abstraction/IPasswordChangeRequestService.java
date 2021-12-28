package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;

import java.util.Optional;

public interface IPasswordChangeRequestService extends IBaseService<PasswordChangeRequest> {

    Optional<PasswordChangeRequest> getByRandomString(String randomString);
}
