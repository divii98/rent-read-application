package com.crio.rentread.security;

import com.crio.rentread.constant.Constants;
import com.crio.rentread.entity.User;
import com.crio.rentread.exception.NotFoundException;
import com.crio.rentread.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with given email"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRole().name().equals("ADMIN") ?
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN")) :
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
