package prlorence.antifraud.api.v1.auth.util;

import prlorence.antifraud.api.v1.auth.entities.ChangeUserAccess;
import prlorence.antifraud.api.v1.auth.entities.ChangeUserRole;
import prlorence.antifraud.api.v1.auth.entities.User;
import prlorence.antifraud.api.v1.auth.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class ResolveUser {
    @Autowired private User user;
    @Autowired private UserControl userControl;
    @Autowired ChangeUserAccess request;
    @Autowired UserRepository userRepo;
    public void resolve(ChangeUserAccess request) throws ResponseStatusException {
        user = userRepo.findByUsername(request.getUsername());
        boolean isAdmin =  user.getRole().equals("ADMINISTRATOR");
        List<String> availableOperations = List.of("LOCK", "UNLOCK");

        if (!userControl.isExistingUser(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (user.getRole().equals(request.getOperation())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (!availableOperations.contains(request.getOperation()) || isAdmin) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void resolve(@Autowired ChangeUserRole request) throws ResponseStatusException {
        user = userRepo.findByUsername(request.getUsername());
        List<String> availableRoles = List.of("SUPPORT", "MERCHANT");

        if (!userControl.isExistingUser(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (user.getRole().equals(request.getRole())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!availableRoles.contains(request.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
