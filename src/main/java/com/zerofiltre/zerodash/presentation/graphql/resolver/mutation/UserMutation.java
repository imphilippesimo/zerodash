package com.zerofiltre.zerodash.presentation.graphql.resolver.mutation;

import com.coxautodev.graphql.tools.*;
import com.zerofiltre.zerodash.model.*;
import com.zerofiltre.zerodash.presentation.security.annotation.*;
import com.zerofiltre.zerodash.service.*;
import org.springframework.stereotype.*;

@Component
public class UserMutation implements GraphQLMutationResolver {

    private UserService userService;

    public UserMutation(UserService userService) {
        this.userService = userService;
    }

    @Unsecured
    public ZDUser createUser(String email, String phoneNumber, String password) {
        final ZDUser user = new ZDUser();
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        return userService.createUser(user);

    }

    public String securedResource() {
        return "Secured resource";
    }

}
