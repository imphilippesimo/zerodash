package com.zerofiltre.zerodash.utils.error;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ZDUserNotFoundException extends UsernameNotFoundException {
    public ZDUserNotFoundException(String msg) {
        super(msg);
    }

}
