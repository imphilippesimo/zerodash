package com.zerofiltre.zerodash.service;

import com.zerofiltre.zerodash.dao.UserRepository;
import com.zerofiltre.zerodash.model.ZDUser;
import com.zerofiltre.zerodash.utils.*;
import com.zerofiltre.zerodash.utils.error.AccountAlreadyExistsException;
import org.slf4j.*;
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

    Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    public ZDUser createUser(ZDUser user) {
        Optional<ZDUser> existingUser = userRepository.findOneByEmail(user.getEmail());
        ZDUser result;

        if (existingUser.isPresent() && user.getEmail() != null) {
            throw new AccountAlreadyExistsException(ACCOUNT_ALREADY_EXISTING, user.getEmail());
        } else if (userRepository.findOneByPhoneNumber(user.getPhoneNumber()).isPresent() && user.getPhoneNumber() != null) {
            throw new AccountAlreadyExistsException(ACCOUNT_ALREADY_EXISTING, user.getPhoneNumber());
        } else {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setRole(ROLE_USER);
            user.setActivated(false);
            user.setActivationKey(RandomUtil.generateActivationKey());
            result = userRepository.save(user);
            emailService.sendActivationEmail(user);
        }
        return result;
    }

    public Page<ZDUser> allUsers(int page, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        return userRepository.findAll(pageRequest);
    }


    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public Optional<ZDUser> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
                .map(user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }
}
