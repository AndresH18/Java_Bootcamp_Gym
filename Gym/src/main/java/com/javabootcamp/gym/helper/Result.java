package com.javabootcamp.gym.helper;

import java.util.Optional;
import java.util.function.Function;

public record Result<TValue, TError>(TValue value, TError error) {

    public static <TValue, TError> Result<TValue, TError> value(TValue value) {
        return new Result<>(value, null);
    }

    public static <TValue, TError> Result<TValue, TError> error(TError error) {
        return new Result<>(null, error);
    }

    public boolean isError() {
        return error != null;
    }

    public boolean isSuccess() {
        return !isError();
    }

    public Optional<TValue> getValue() {
        return Optional.ofNullable(value);
    }

    public Optional<TError> getError() {
        return Optional.ofNullable(error);
    }

    public <TResult> TResult match(Function<TValue, TResult> success, Function<TError, TResult> error) {
        return isError() ? error.apply(this.error) : success.apply(this.value);
    }
}

//public class Result<TValue, TError> {
//    public static <TValue, TError> Result<TValue, TError> value(TValue value) {
//        return new Result<>(value, null, false);
//    }
//
//    public static <TValue, TError> Result<TValue, TError> error(TError error) {
//        return new Result<>(null, error, true);
//    }
//
//    private final TValue value;
//    private final TError error;
//    private final boolean isError;
//
//    private Result(TValue value, TError error, boolean isError) {
//        this.value = value;
//        this.error = error;
//        this.isError = isError;
//    }
//
//
//    public boolean isError() {
//        return isError;
//    }
//
//    public boolean isSuccess() {
//        return !isError;
//    }
//
//    public Optional<TValue> getValue() {
//        return Optional.ofNullable(value);
//    }
//
//    public Optional<TError> getError() {
//        return Optional.ofNullable(error);
//    }
//
//    public <TResult> TResult match(Function<TValue, TResult> success, Function<TError, TResult> error) {
//        return isError ? error.apply(this.error) : success.apply(this.value);
//    }
//}