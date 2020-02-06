package com.zerofiltre.zerodash.utils.error;

import graphql.*;
import graphql.language.*;

import java.util.*;

public class UnauthorizedAccessException extends RuntimeException implements GraphQLError {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    @Override
    public List<SourceLocation> getLocations() {
        return new ArrayList<>();
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.ValidationError;
    }
}
