package prlorence.antifraud.api.v1.auth.util;

import prlorence.antifraud.api.v1.auth.entities.RequestUser;
import prlorence.antifraud.api.v1.auth.entities.User;
import prlorence.antifraud.api.v1.auth.model.UserRepository;
import prlorence.antifraud.api.v1.auth.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserControl {
    @Autowired private Admin admin;
    @Autowired private User user;
    @Autowired private MyUserDetailsService myUserDetailsService;
    @Autowired private UserRepository userRepo;

    @Transactional
    public void deleteUser(String username) {
        userRepo.deleteByUsername(username);
    }

    public User getUserDetails(String username) {
        return userRepo.findByUsername(username);
    }

    public boolean isExistingUser(String username) {
        return userRepo.findByUsername(username) != null;
    }

    public void addUser(RequestUser newUser) {
        admin.checkAdmin();

        if (admin.containsAdmin) {
            newUser.setRole("MERCHANT");
        } else {
            newUser.setRole("ADMINISTRATOR");
        }
        List<GrantedAuthority> userAuth = myUserDetailsService.buildUserAuthority("ROLE_" + newUser.getRole());
        user = myUserDetailsService.buildUserForAuthentication(newUser, userAuth);

        userRepo.save(user);
        validateUser(user.getUsername());
    }

    public void validateUser(String username) {
        user = userRepo.findByUsername(username);
        if (!user.getRole().equals("ADMINISTRATOR")) disableUser(username);
    }

    public void updateUserRole(String username, String newRole) {
        User userUpdate = userRepo.findByUsername(username);
        userUpdate.setRole(newRole);
        userRepo.save(userUpdate);
    }

    public void updateUserAccess(String username, String newAccess) {
        if ("UNLOCK".equals(newAccess)) {
            enableUser(username);
        } else {
            disableUser(username);
        }
    }

    public List<User> getUsers() {
        List<User> users = new CopyOnWriteArrayList<>();

        for (User thisUser : userRepo.findAll()) {
            users.add(thisUser);
        }

        return users;
    }

    public void disableUser(String username) {
        user = userRepo.findByUsername(username);
        user.setEnabled(false);
        user.setAccountNonExpired(false);
        user.setAccountNonLocked(false);
        user.setCredentialsNonExpired(false);
        userRepo.save(user);
    }

    public void enableUser(String username) {
        user = userRepo.findByUsername(username);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        userRepo.save(user);
    }

}
