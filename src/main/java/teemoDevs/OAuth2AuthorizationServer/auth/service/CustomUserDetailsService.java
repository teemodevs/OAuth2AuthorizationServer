package teemoDevs.OAuth2AuthorizationServer.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import teemoDevs.OAuth2AuthorizationServer.auth.domain.CustomUserDetails;
import teemoDevs.OAuth2AuthorizationServer.auth.repository.UserRepository;

/**
 * {@link CustomUserDetails}를 다루기 위한 Repository
 * 1. 회원가입 시 {@link CustomUserDetails} 정보를 저장
 * 2. 로그인 시 {@link CustomUserDetails} 정보를 검색
 * */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public void save(CustomUserDetails customUserDetails) {
        userRepository.save(customUserDetails);
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
