package teemoDevs.OAuth2AuthorizationServer.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import teemoDevs.OAuth2AuthorizationServer.auth.domain.CustomUserDetails;
import teemoDevs.OAuth2AuthorizationServer.auth.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public void save(CustomUserDetails customUserDetails) {
        userRepository.save(customUserDetails);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
