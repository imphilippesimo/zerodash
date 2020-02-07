package com.zerofiltre.zerodash.service;

import com.zerofiltre.zerodash.dao.UserRepository;
import com.zerofiltre.zerodash.model.ZDUser;
import com.zerofiltre.zerodash.utils.error.AccountAlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.zerofiltre.zerodash.utils.Constants.ACCOUNT_ALREADY_EXISTING;
import static com.zerofiltre.zerodash.utils.Constants.ROLE_USER;

@Service
@Transactional
public class UserService {


    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public ZDUser createUser(ZDUser user) {
        Optional<ZDUser> existingUser = userRepository.findOneByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new AccountAlreadyExistsException(ACCOUNT_ALREADY_EXISTING, user.getEmail());
        } else if (userRepository.findOneByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new AccountAlreadyExistsException(ACCOUNT_ALREADY_EXISTING, user.getPhoneNumber());
        } else {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setRole(ROLE_USER);
            return userRepository.save(user);
        }
    }

    public Page<ZDUser> allUsers(int page, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        return userRepository.findAll(pageRequest);
    }


    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
