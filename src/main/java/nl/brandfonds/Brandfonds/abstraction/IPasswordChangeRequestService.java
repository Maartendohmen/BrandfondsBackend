package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.Day;
import nl.brandfonds.Brandfonds.model.PasswordChangeRequest;

import java.util.List;

public interface IPasswordChangeRequestService {

     public abstract List<PasswordChangeRequest> GetAll();

     public abstract PasswordChangeRequest GetOne(Integer id);

     public abstract void Save(PasswordChangeRequest passwordChangeRequest);

     public abstract void Delete(PasswordChangeRequest passwordChangeRequest);

     public  abstract PasswordChangeRequest GetByrandomString(String randomString);
}
