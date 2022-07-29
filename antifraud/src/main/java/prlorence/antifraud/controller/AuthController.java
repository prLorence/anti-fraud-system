package prlorence.antifraud.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import prlorence.antifraud.api.v1.auth.entities.ChangeUserAccess;
import prlorence.antifraud.api.v1.auth.entities.ChangeUserRole;
import prlorence.antifraud.api.v1.auth.entities.RequestUser;
import prlorence.antifraud.api.v1.auth.entities.User;
import prlorence.antifraud.api.v1.auth.model.UserRepository;
import prlorence.antifraud.api.v1.auth.services.MyUserDetailsService;
import prlorence.antifraud.api.v1.auth.util.UtilityConfig;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@ComponentScan({"antifraud.api.auth.services", "auth.model", "auth.service"})
@Setter(onMethod_={@Autowired})
public class AuthController {
    private PasswordEncoder encoder;
    private MyUserDetailsService myUserDetailsService;
    private UserRepository userRepo;
    private UtilityConfig utilityConfig;


    // testing method:
    // first user should have role : "ROLE_ADMIN" in the response 201 body
    // everything else should have : "ROLE_MERCHANT" in 201 response body
    // list should output only one Admin
    @PostMapping("/api/auth/user")
    public ResponseEntity<Object> addUser(@RequestBody @Valid RequestUser requestUser) {
        try {
            if (utilityConfig.userControl().isExistingUser(requestUser.getUsername())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            requestUser.setPassword(encoder.encode(requestUser.getPassword()));
            utilityConfig.userControl().addUser(requestUser);
            return new ResponseEntity<>(
                    utilityConfig.userControl().getUserDetails(requestUser.getUsername()),
                            HttpStatus.CREATED);

        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping( "/api/auth/list")
    public List<User> getAllUsers() {
        return utilityConfig.userControl().getUsers();
    }

    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable String username) {
        if (!utilityConfig.userControl().isExistingUser(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        utilityConfig.userControl().deleteUser(username);

        return new ResponseEntity<>(
                Map.of("username", username,
                        "status", "Deleted successfully!"),
                HttpStatus.OK);
    }

    @PutMapping("/api/auth/role")
    public ResponseEntity<Object> changeUserRole(@RequestBody ChangeUserRole updateUser) {
        utilityConfig.resolveUser().resolve(updateUser);

        User user = userRepo.findByUsername(updateUser.getUsername());

        utilityConfig.userControl().updateUserRole(updateUser.getUsername(), updateUser.getRole());

        return new ResponseEntity<>(
                utilityConfig.userControl().getUserDetails(user.getUsername()),
                HttpStatus.OK);
    }

    @PutMapping("/api/auth/access")
    public ResponseEntity<Object> changeUserAccess(@RequestBody ChangeUserAccess request) {
        final Map<String, String> userAccessMessage = new ConcurrentHashMap<>();

        utilityConfig.resolveUser().resolve(request);

        // gets the operation value and lowercase it
        userAccessMessage.put("status", String.format("User %s %s", request.getUsername(),
                request.getOperation().toLowerCase() + "ed!"));

        utilityConfig.userControl().updateUserAccess(request.getUsername(), request.getOperation());

        return new ResponseEntity<>(userAccessMessage, HttpStatus.OK);
    }


}