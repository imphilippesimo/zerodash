package com.zerofiltre.zerodash.utils.error;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zerofiltre.zerodash.utils.Constants.INVALID_IDENTIFIER;

public class AccountAlreadyExistsException extends RuntimeException implements GraphQLError {

    final Map<String, Object> extensions = new HashMap<>();

    public AccountAlreadyExistsException(String message, String identifier) {
        super(message);
        extensions.put(INVALID_IDENTIFIER, identifier);
    }

    @Override
    public List<SourceLocation> getLocations() {
        return new ArrayList<>();
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.ValidationError;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return extensions;
    }
}
