package com.zerofiltre.zerodash.presentation.graphql.resolver.query;

import com.coxautodev.graphql.tools.*;
import com.zerofiltre.zerodash.model.*;
import com.zerofiltre.zerodash.presentation.security.annotation.*;
import com.zerofiltre.zerodash.service.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class UserQuery implements GraphQLQueryResolver {

    private UserService userService;

    public UserQuery(UserService userService) {
        this.userService = userService;
    }

    @AdminSecured
    public List<ZDUser> allUsers(final int page, final int pageSize) {
        return userService.allUsers(page, pageSize).getContent();
    }
}
