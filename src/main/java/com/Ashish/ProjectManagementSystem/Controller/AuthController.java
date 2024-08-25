package com.Ashish.ProjectManagementSystem.Controller;

import com.Ashish.ProjectManagementSystem.AppConfig.JwtProvider;
import com.Ashish.ProjectManagementSystem.Response.AuthResponse;
import com.Ashish.ProjectManagementSystem.Services.CustomerUserDetailsImpl;
import com.Ashish.ProjectManagementSystem.UserRepository.UserRepository;
import com.Ashish.ProjectManagementSystem.modal.User;
import com.Ashish.ProjectManagementSystem.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerUserDetailsImpl customerUserDetailsImpl;

    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> CreateUserHandler(@RequestBody User user) throws Exception {

        User isUserExist = userRepository.findByEmail(user.getEmail());
        if(isUserExist != null){
            throw new Exception("Email already exist with somother Account ");
        }
        User createdUser = new User();
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());

        User savedUser = userRepository.save(createdUser);

        Authentication authentication  = new UsernamePasswordAuthenticationToken(user.getEmail() , user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();

        res.setMessage("Sign-UP Successfully...");
        res.setJwt(jwt);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest) {

        String userName = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication  = authenticate(userName , password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();

        res.setMessage("Sign-IN Successfully...");
        res.setJwt(jwt);

        return new ResponseEntity<>(res , HttpStatus.CREATED);

    }

    private Authentication authenticate(String userName, String password) {
        UserDetails  userDetails = customerUserDetailsImpl.loadUserByUsername(userName);

        if(userDetails== null){
            throw new BadCredentialsException("Invalid UserName...");
        }
        if (!passwordEncoder.matches(password , userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password...");
        }
        return new UsernamePasswordAuthenticationToken(userDetails , null ,userDetails.getAuthorities());
    }

}
