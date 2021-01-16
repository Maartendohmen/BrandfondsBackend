package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;

import java.util.List;
import java.util.Optional;

public interface IPasswordChangeRequestService {

    List<PasswordChangeRequest> getAll();

    PasswordChangeRequest getOne(Integer id);

    void save(PasswordChangeRequest passwordChangeRequest);

    void delete(PasswordChangeRequest passwordChangeRequest);

    Optional<PasswordChangeRequest> getByrandomString(String randomString);
}
