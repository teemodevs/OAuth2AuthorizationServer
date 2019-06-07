package teemoDevs.OAuth2AuthorizationServer.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teemoDevs.OAuth2AuthorizationServer.auth.domain.CustomUserDetails;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<CustomUserDetails, Long> {
    CustomUserDetails findByUsername(String username);
}