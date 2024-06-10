package com.smart.config;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

   @Autowired
   private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.getUserByName(username);
        System.out.println("user:"+user);
        if(user==null){
            System.out.println("Error");
            throw new UsernameNotFoundException("User Not Found");

        }
        UserDetailsImpl userDetailsImpl=new UserDetailsImpl(user);
        return userDetailsImpl;
    }
}
