package com.themonkeynauts.game.adapter.in.web.rpc.exception;

import com.themonkeynauts.game.adapter.in.web.rpc.context.GameContexts;
import com.themonkeynauts.game.common.exception.CredentialsAlreadyTakenException;
import com.themonkeynauts.game.common.exception.CredentialsRequiredException;
import com.themonkeynauts.game.common.exception.InvalidCredentialsException;
import com.themonkeynauts.game.common.exception.InvalidFormatException;
import com.themonkeynauts.game.common.exception.MonkeynautRequiredAttributeException;
import com.themonkeynauts.game.common.exception.NotFoundException;
import com.themonkeynauts.game.common.exception.TheMonkeynautsException;
import io.grpc.Status;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

@GrpcAdvice
@RequiredArgsConstructor
public class GrpcExceptionAdvice {

    private final MessageSource messageSource;

    @GrpcExceptionHandler
    public Status handleInvalidArgumentException(IllegalArgumentException e) {
        var locale = GameContexts.LOCALE_CONTEXT_KEY.get();
        return Status.INVALID_ARGUMENT.withDescription(messageSource.getMessage(e.getMessage(), null, locale)).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleTheMonkeynautsException(TheMonkeynautsException e) {
        var locale = GameContexts.LOCALE_CONTEXT_KEY.get();
        return Status.FAILED_PRECONDITION.withDescription(messageSource.getMessage(e.getMessage(), null, locale)).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleNotFoundException(NotFoundException e) {
        var locale = GameContexts.LOCALE_CONTEXT_KEY.get();
        var message = messageSource.getMessage(e.getMessage(), null, locale);
        var entity = messageSource.getMessage(e.getEntityName(), null, locale);
        return Status.FAILED_PRECONDITION.withDescription(message.formatted(entity)).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleCredentialsAlreadyTakenException(CredentialsAlreadyTakenException e) {
        var locale = GameContexts.LOCALE_CONTEXT_KEY.get();
        var message = messageSource.getMessage(e.getMessage(), null, locale).formatted(e.getData());
        return Status.INVALID_ARGUMENT.withDescription(message).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException e) {
        var locale = GameContexts.LOCALE_CONTEXT_KEY.get();
        var message = messageSource.getMessage("validation.authentication.unauthenticated", null, locale);
        return Status.UNAUTHENTICATED.withDescription(message).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleCredentialsRequiredException(CredentialsRequiredException e) {
        var locale = GameContexts.LOCALE_CONTEXT_KEY.get();
        var message = messageSource.getMessage(e.getMessage(), null, locale).formatted(e.getMessage());
        return Status.INVALID_ARGUMENT.withDescription(message).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleInvalidCredentialsException(InvalidCredentialsException e) {
        var locale = GameContexts.LOCALE_CONTEXT_KEY.get();
        var message = messageSource.getMessage(e.getMessage(), null, locale).formatted(e.getMessage());
        return Status.PERMISSION_DENIED.withDescription(message).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleAccessDeniedException(AccessDeniedException e) {
        return Status.PERMISSION_DENIED.withDescription(e.getMessage()).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleMonkeynautRequiredAttributeException(MonkeynautRequiredAttributeException e) {
        var locale = GameContexts.LOCALE_CONTEXT_KEY.get();
        var message = messageSource.getMessage(e.getMessage(), null, locale).formatted(e.getAttribute());
        return Status.INVALID_ARGUMENT.withDescription(message).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleInvalidFormatException(InvalidFormatException e) {
        var locale = GameContexts.LOCALE_CONTEXT_KEY.get();
        var message = messageSource.getMessage(e.getMessage(), null, locale).formatted(e.getType(), e.getData());
        return Status.INVALID_ARGUMENT.withDescription(message).withCause(e);
    }

}
