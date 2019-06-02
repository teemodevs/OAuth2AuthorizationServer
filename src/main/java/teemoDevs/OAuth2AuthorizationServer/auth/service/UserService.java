package teemoDevs.OAuth2AuthorizationServer.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teemoDevs.OAuth2AuthorizationServer.auth.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
