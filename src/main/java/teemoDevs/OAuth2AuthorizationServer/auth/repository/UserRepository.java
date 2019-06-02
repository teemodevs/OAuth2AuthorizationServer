package teemoDevs.OAuth2AuthorizationServer.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teemoDevs.OAuth2AuthorizationServer.auth.domain.User;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
}