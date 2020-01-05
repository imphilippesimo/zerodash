package com.zerofiltre.zerodash.presentation.graphql.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.zerofiltre.zerodash.model.ZDUser;
import com.zerofiltre.zerodash.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMutation implements GraphQLMutationResolver {

    @Autowired
    private UserService userService;

    public ZDUser createUser(final String email, final String phoneNumber, final String password) throws Exception {
        final ZDUser user = new ZDUser();
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        return userService.createUser(user);

    }

}
