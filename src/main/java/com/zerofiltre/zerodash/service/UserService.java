package com.zerofiltre.zerodash.service;

import com.zerofiltre.zerodash.dao.UserRepository;
import com.zerofiltre.zerodash.model.ZDUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.zerofiltre.zerodash.Utils.Constants.ACCOUNT_ALREADY_EXISTING;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ZDUser createUser(ZDUser user) throws Exception {
        Optional<ZDUser> existingUser = Optional.ofNullable(userRepository.findOneByEmail(user.getEmail()).orElse(
                userRepository.findOneByPhoneNumber(user.getPhoneNumber()).orElse(null)
        ));
        if (existingUser.isPresent()) throw new Exception(ACCOUNT_ALREADY_EXISTING);
        else {
            user.setRole("USER");
            return userRepository.save(user);
        }
    }

    public Page<ZDUser> allUsers(int page, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        return userRepository.findAll(pageRequest);
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrPhoneNumber) throws UsernameNotFoundException {

        Optional<ZDUser> existingUser = Optional.ofNullable(userRepository.findOneByEmail(emailOrPhoneNumber).orElse(
                userRepository.findOneByPhoneNumber(emailOrPhoneNumber).orElse(null)
        ));
        existingUser.ifPresent(UserService::accept);

        // If user not found. Throw this exception.
        throw new UsernameNotFoundException("User with email or phone number: " + emailOrPhoneNumber + " not found");

    }

    private static User accept(ZDUser zdUser) {
        // Remember that Spring needs roles to be in this format: "ROLE_" + userRole (i.e. "ROLE_ADMIN")
        // So, we need to set it to that format, so we can verify and compare roles (i.e. hasRole("ADMIN")).
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_" + zdUser.getRole());

        return new User(zdUser.getEmail(), zdUser.getPassword(), grantedAuthorities);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
