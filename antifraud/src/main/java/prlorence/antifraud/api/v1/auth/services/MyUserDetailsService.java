package prlorence.antifraud.api.v1.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import prlorence.antifraud.api.v1.auth.entities.RequestUser;
import prlorence.antifraud.api.v1.auth.entities.User;
import prlorence.antifraud.api.v1.auth.model.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
@ComponentScan("com.example.model")
public class MyUserDetailsService implements UserDetailsService {
    @Autowired private UserRepository userRepo;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }

        List<GrantedAuthority> authorities = buildUserAuthority("ROLE_" + user.getRole());

        return buildUserForAuthentication(user, authorities);
    }

    /**
        Auth for new users
    */
    public User buildUserForAuthentication(RequestUser user, List<GrantedAuthority> authorities) {
        return new User(user,
                true,
                true,
                true, 
                true, authorities);
    }

    /**
        Overloaded auth for existing users
    */
    private User buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new User(user,
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                authorities);
    }

    /**
        Grants authorities to users
     */
    public List<GrantedAuthority> buildUserAuthority(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
}

