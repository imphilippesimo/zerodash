package com.zerofiltre.zerodash.service;

import com.zerofiltre.zerodash.dao.UserRepository;
import com.zerofiltre.zerodash.model.ZDUser;
import com.zerofiltre.zerodash.utils.error.ZDUserNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.zerofiltre.zerodash.utils.Constants.ZDUSER_NOT_FOUND;

@Component
public class ZDUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;


    public ZDUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrPhoneNumber) {

        Optional<ZDUser> existingUser = Optional.ofNullable(userRepository.findOneByEmail(emailOrPhoneNumber).orElse(
                userRepository.findOneByPhoneNumber(emailOrPhoneNumber).orElse(null)
        ));
        if (existingUser.isPresent()) {
            return ZDUserDetailsService.accept(existingUser.get());
        } else {
            // If user not found. Throw this exception.
            StringBuilder sb = new StringBuilder(ZDUSER_NOT_FOUND).append(emailOrPhoneNumber);
            throw new ZDUserNotFoundException(sb.toString());

        }
    }

    private static User accept(ZDUser zdUser) {
        // Remember that Spring needs roles to be in this format: "ROLE_" + userRole (i.e. "ROLE_ADMIN")
        // So, we need to set it to that format, so we can verify and compare roles (i.e. hasRole("ADMIN")).
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_" + zdUser.getRole());

        return new User(zdUser.getEmail(), zdUser.getPassword(), grantedAuthorities);
    }
}
