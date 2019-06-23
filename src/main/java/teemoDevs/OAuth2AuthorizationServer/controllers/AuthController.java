package teemoDevs.OAuth2AuthorizationServer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import teemoDevs.OAuth2AuthorizationServer.auth.domain.CustomUserDetails;
import teemoDevs.OAuth2AuthorizationServer.auth.service.CustomUserDetailsService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AuthController {

    @Value("${security.oauth2.resource.email-duplicated-check-uri}")
    private String emailDuplicatedCheckUri;

    @Value("${security.oauth2.resource.email-add-uri}")
    private String emailAddUri;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("customUserDetails", new CustomUserDetails());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute @Valid CustomUserDetails customUserDetails, BindingResult bindingResult) {

        /** Data Validation **/

        // username이 이미 존재하는 경우 에러
        CustomUserDetails existCustomUserDetails = customUserDetailsService.loadUserByUsername(customUserDetails.getUsername());
        if( existCustomUserDetails != null) {
            bindingResult.addError(
                    new FieldError("customUserDetails", "username", "Username Duplicated")
            );
        }

        // email이 이미 존재하는 경우 에러

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response =
                restTemplate.postForEntity(emailDuplicatedCheckUri, customUserDetails.getEmail(), String.class);

        if(response.getStatusCode() != HttpStatus.OK) {
            bindingResult.addError(
                    new FieldError("customUserDetails", "email", "Email Duplicated")
            );
        }

        // 기본 Data Validation 실패 시 에러
        if ( bindingResult.hasErrors() ) {
            return "signup";
        }


        /** Data Validation Success : 회원가입 진행 **/

        // 패스워드 암호화
        customUserDetails.setPassword(
                bCryptPasswordEncoder
                        .encode(customUserDetails.getPassword())
        );

        // 권한 설정
        List<String> authorities = new ArrayList<>();
        authorities.add("ADMIN");
        authorities.add("USER");
        authorities.add("CUSTOM");
        customUserDetails.setAuthorities(authorities);

        // Activate
        customUserDetails.setAccountNonExpired(true);
        customUserDetails.setAccountNonLocked(true);
        customUserDetails.setEnabled(true);
        customUserDetails.setCredentialsNonExpired(true);

        // 이메일 등록
        Map<String, String> emailRegisterParameter = new HashMap<>();
        emailRegisterParameter.put("username", customUserDetails.getUsername());
        emailRegisterParameter.put("email", customUserDetails.getEmail());

        ResponseEntity<String> emailRegisterCheckResponse =
                restTemplate.postForEntity(emailAddUri, emailRegisterParameter, String.class);

        if(emailRegisterCheckResponse.getStatusCode() != HttpStatus.OK) {
            bindingResult.addError(
                    new FieldError("customUserDetails", "email", "Email Register Failed")
            );
            return "signup";
        }

        customUserDetailsService.save(customUserDetails);

        return "redirect:/login";
    }
}
