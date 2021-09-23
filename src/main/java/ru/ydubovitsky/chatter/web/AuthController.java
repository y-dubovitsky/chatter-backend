package ru.ydubovitsky.chatter.web;

import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ydubovitsky.chatter.payload.request.LoginRequest;
import ru.ydubovitsky.chatter.payload.request.SingUpRequest;
import ru.ydubovitsky.chatter.payload.response.JwtTokenSuccessResponse;
import ru.ydubovitsky.chatter.payload.response.MessageResponse;
import ru.ydubovitsky.chatter.security.JwtTokenProvider;
import ru.ydubovitsky.chatter.security.SecurityConstants;
import ru.ydubovitsky.chatter.service.UserService;

import javax.validation.Valid;

@CrossOrigin //TODO ?
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()") //TODO ?
public class AuthController {

    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private UserService userService;

    public AuthController(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateWebToken(authentication);
        System.out.println("Hello");
        return ResponseEntity.ok(new JwtTokenSuccessResponse(true, jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SingUpRequest singUpRequest, BindingResult bindingResult) throws Exception {
        userService.createUser(singUpRequest);
        return ResponseEntity.ok(new MessageResponse("success"));
    }
}
