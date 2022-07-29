package prlorence.antifraud.api.v1.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
@JsonIgnoreProperties(value = {
        "password",
        "enabled",
        "authorities",
        "rolesAndAuthorities",
        "accountNonExpired",
        "credentialsNonExpired",
        "accountNonLocked"
}, allowSetters= true)
@Component
public class User implements UserDetails {

    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private int id;

    @NotEmpty
    @Column
    private String name;

    @NotEmpty
    @Column
    private String username;

    @NotEmpty
    @Column
    private String password;

    @Column
    private String role;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    @Transient
    private List<GrantedAuthority> rolesAndAuthorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAndAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public User(RequestUser user, boolean enabled, boolean accountNonExpired,
                boolean accountNonLocked, boolean credentialsNonExpired,
                List<GrantedAuthority> authorities) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.rolesAndAuthorities = authorities;
    }

    public User(User user, boolean enabled, boolean accountNonExpired,
                boolean accountNonLocked, boolean credentialsNonExpired,
                List<GrantedAuthority> authorities) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.rolesAndAuthorities = authorities;
    }
}
