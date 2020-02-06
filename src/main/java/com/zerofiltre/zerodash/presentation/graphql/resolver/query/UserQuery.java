package com.zerofiltre.zerodash.presentation.graphql.resolver.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.zerofiltre.zerodash.model.ZDUser;
import com.zerofiltre.zerodash.presentation.security.annotation.*;
import com.zerofiltre.zerodash.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserQuery implements GraphQLQueryResolver {

    @Autowired
    private UserService userService;


    @AdminSecured
    public List<ZDUser> allUsers(final int page, final int pageSize) {
        return userService.allUsers(page, pageSize).getContent();
    }
}
