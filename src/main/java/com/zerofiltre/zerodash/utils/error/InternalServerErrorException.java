package com.zerofiltre.zerodash.utils.error;

import com.zerofiltre.zerodash.utils.*;
import org.zalando.problem.*;

/**
 * Simple exception with a message, that returns an Internal Server Error code.
 */
public class InternalServerErrorException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InternalServerErrorException(String message) {
        super(Constants.DEFAULT_TYPE, message, Status.INTERNAL_SERVER_ERROR);
    }
}
