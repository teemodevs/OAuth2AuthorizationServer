package teemoDevs.OAuth2AuthorizationServer.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import teemoDevs.OAuth2AuthorizationServer.auth.User;

@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
}
