package teemoDevs.OAuth2AuthorizationServer.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 커스터마이징 UserDetails
 * {@link UserDetails} 인터페이스를 구현하여 추가적인 정보가 필요한 경우 넣어줄 수 있도록 함
 * 1. Authorization Server에 로그인 시 {@link UserDetails} 역할
 * 2. 로그인 성공 시 Client에 유저 정보(username, authorities)를 전송하는 DTO
 * 3. 회원가입 시 Resource Server에게 유저 정보(username, email)를 전송하는 DTO
 * */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    @Size(min = 4, max = 12)
    @Column(unique = true)
    private String username;

    @NotNull
    @Email
    @Transient
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for (String authority : this.authorities) {
            GrantedAuthority grantedAuthority = (GrantedAuthority) () -> authority;
            authorities.add(grantedAuthority);
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
