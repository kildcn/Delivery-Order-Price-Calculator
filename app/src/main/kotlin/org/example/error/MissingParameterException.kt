package org.example.error

class MissingParameterException(parameterName: String) :
    RuntimeException("Required parameter '$parameterName' is missing")
