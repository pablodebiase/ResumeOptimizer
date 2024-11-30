package org.resumeoptimizer.services;

import lombok.Getter;
import lombok.Setter;
import org.resumeoptimizer.entities.User;
import org.resumeoptimizer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    // Load user for Spring Security
    // Method to save new user
    // Inject PasswordEncoder
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Getter
    @Setter
    private String guestUsername = "";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User" + username + " not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                getAuthority(user)
        );
    }

    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User getGuestUser() {
        User user = new User();
        user.setUsername(guestUsername);
        user.setRole("GUEST");
        return user;
    }

    private Collection<? extends GrantedAuthority> getAuthority(User user) {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null && !guestUsername.isEmpty()) {
            user = getGuestUser();
        }
        return user;
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}