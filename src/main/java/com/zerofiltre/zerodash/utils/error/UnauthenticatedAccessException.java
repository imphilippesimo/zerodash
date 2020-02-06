package com.zerofiltre.zerodash.utils.error;

import graphql.*;
import graphql.language.*;

import java.util.*;

public class UnauthenticatedAccessException extends RuntimeException implements GraphQLError {


    public UnauthenticatedAccessException(String msg) {
        super(msg);
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
