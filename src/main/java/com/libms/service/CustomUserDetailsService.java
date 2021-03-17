package com.libms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
        UserDetails user = userService.selectUser(user_id);
        if(user == null){
            throw new UsernameNotFoundException(user_id);
        }
        System.out.println("CustomUserDetailsService user = " + user.getUsername());
        return user;
    }
}
