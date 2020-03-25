package com.zerofiltre.zerodash.presentation.rest;

import com.zerofiltre.zerodash.model.*;
import com.zerofiltre.zerodash.service.*;
import com.zerofiltre.zerodash.utils.error.*;
import io.micrometer.core.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AccountResource {

    private UserService userService;

    public AccountResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping("/activate")
    @Timed
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<ZDUser> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this activation key");
        }
    }
}
