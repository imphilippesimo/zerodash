package com.zerofiltre.zerodash.utils;

import java.net.*;

public class Constants {

    public static final String ACCOUNT_ALREADY_EXISTING = "An Account is already registered for this email or phone number," +
            " please try another one";
    public static final String INVALID_IDENTIFIER = "invalidId";
    public static final String ZDUSER_NOT_FOUND = "We were unable to find a user with the provided credentials: ";
    public static final String UNAUTHORIZED_ACCESS_DENIED = "Sorry, you do not have enough rights to do that!";
    public static final String UNAUTHENTICATED_ACCESS_DENIED = "Sorry, you should log in first to do that!";

    public static final String TEST_EMAIL = "name@zerodash.com";
    public static final String ANOTHER_TEST_EMAIL = "anothername@zerodash.com";
    public static final String TEST_PHONE_NUMBER = "88544565";
    public static final String ANOTHER_TEST_PHONE_NUMBER = "4588544565";
    public static final String TEST_PASSWORD = "mypassword";
    public static final String ROLE_USER = "USER";
    public static final String TEST_UNKNOWN_EMAIL = "unknown@zerofiltre.tech";
    public static final String ACCOUNT_VALIDATION_EMAIL_SUBJECT = "Validate your account";
    public static final String TEST_ACTIVATION_KEY = "XXXdkro45";


    /**
     * Error Constants
     */
    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI PARAMETERIZED_TYPE = URI.create(PROBLEM_BASE_URL + "/parameterized");
    public static final URI ENTITY_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/entity-not-found");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");
    public static final URI EMAIL_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/email-not-found");
    public static final URI ACTIVATION_KEY_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/activation-key-not-found");
    public static final URI PHONE_NUMBER_ALREADY_USE_TYPE = URI.create(PROBLEM_BASE_URL + "/phone-number-already-used");

    private Constants() {
    }
}
