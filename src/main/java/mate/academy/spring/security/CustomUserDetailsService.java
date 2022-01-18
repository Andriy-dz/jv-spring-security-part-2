package mate.academy.spring.security;

import mate.academy.spring.model.User;
import mate.academy.spring.service.UserService;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userService.findByEmail(userEmail).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));
        UserBuilder builder =
                org.springframework.security.core.userdetails.User.withUsername(userEmail);
        builder.password(user.getPassword());
        builder.roles(user.getRoles().stream()
                .map(role -> role.getRoles().toString())
                .toArray(String[]::new));
        return builder.build();
    }
}