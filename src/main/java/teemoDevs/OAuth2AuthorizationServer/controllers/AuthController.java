package teemoDevs.OAuth2AuthorizationServer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import teemoDevs.OAuth2AuthorizationServer.auth.domain.CustomUserDetails;
import teemoDevs.OAuth2AuthorizationServer.auth.service.CustomUserDetailsService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

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
        if ( bindingResult.hasErrors() ) {
            return "signup";
        }

        // 패스워드 암호화
        customUserDetails.setPassword(
                bCryptPasswordEncoder
                        .encode(customUserDetails.getPassword())
        );

        // 권한 설정
        List<String> authorities = new ArrayList<>();
        //authorities.add("ADMIN");
        authorities.add("USER");
        customUserDetails.setAuthorities(authorities);

        // Activate
        customUserDetails.setAccountNonExpired(true);
        customUserDetails.setAccountNonLocked(true);
        customUserDetails.setEnabled(true);
        customUserDetails.setCredentialsNonExpired(true);

        customUserDetailsService.save(customUserDetails);

        return "redirect:/login";
    }
}
