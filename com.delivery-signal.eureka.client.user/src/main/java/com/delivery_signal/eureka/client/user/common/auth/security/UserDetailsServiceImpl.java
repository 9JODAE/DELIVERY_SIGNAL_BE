package com.delivery_signal.eureka.client.user.common.auth.security;

import com.delivery_signal.eureka.client.user.application.exception.ErrorCode;
import com.delivery_signal.eureka.client.user.application.exception.ServiceException;
import com.delivery_signal.eureka.client.user.domain.entity.User;
import com.delivery_signal.eureka.client.user.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));


        return new UserDetailsImpl(user);
    }
}
