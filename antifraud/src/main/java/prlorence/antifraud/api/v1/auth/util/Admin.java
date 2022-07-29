package prlorence.antifraud.api.v1.auth.util;

import prlorence.antifraud.api.v1.auth.entities.User;
import prlorence.antifraud.api.v1.auth.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Admin {
    boolean containsAdmin = false;
    @Autowired private UserRepository userRepo;

    public List<User> findAdmin() {
        return userRepo.findAllByRoleEqualsIgnoreCase("ADMINISTRATOR");
    }

    public void checkAdmin() {
        if (!findAdmin().isEmpty()) {
            containsAdmin = true;
        }
    }

}
