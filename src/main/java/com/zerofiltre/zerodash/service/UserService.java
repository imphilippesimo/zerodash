package com.zerofiltre.zerodash.service;

import com.zerofiltre.zerodash.dao.UserRepository;
import com.zerofiltre.zerodash.model.ZDUser;
import com.zerofiltre.zerodash.utils.error.AccountAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.zerofiltre.zerodash.utils.Constants.ACCOUNT_ALREADY_EXISTING;
import static com.zerofiltre.zerodash.utils.Constants.ROLE_USER;

@Service
@Transactional
public class UserService{

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ZDUser createUser(ZDUser user) {
        Optional<ZDUser> existingUser = userRepository.findOneByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new AccountAlreadyExistsException(ACCOUNT_ALREADY_EXISTING, user.getEmail());
        } else if (userRepository.findOneByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new AccountAlreadyExistsException(ACCOUNT_ALREADY_EXISTING, user.getPhoneNumber());
        } else {
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
