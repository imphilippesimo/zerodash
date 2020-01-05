package com.zerofiltre.zerodash.service;

import com.zerofiltre.zerodash.dao.UserRepository;
import com.zerofiltre.zerodash.model.ZDUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.zerofiltre.zerodash.Utils.Constants.ACCOUNT_ALREADY_EXISTING;

@Service
@Transactional
public class UserService {

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
            return userRepository.save(user);
        }
    }
    public Page<ZDUser> allUsers(int page, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        return userRepository.findAll(pageRequest);
    }

}
