package com.themonkeynauts.game.adapter.in.web.rpc.exception;

import com.themonkeynauts.game.adapter.in.web.rpc.context.GameContexts;
import com.themonkeynauts.game.common.exception.CredentialsAlreadyTakenException;
import com.themonkeynauts.game.common.exception.CredentialsRequiredException;
import com.themonkeynauts.game.common.exception.InvalidCredentialsException;
import com.themonkeynauts.game.common.exception.InvalidFormatException;
import com.themonkeynauts.game.common.exception.MonkeynautRequiredAttributeException;
import com.themonkeynauts.game.common.exception.NotFoundException;
import com.themonkeynauts.game.common.exception.TheMonkeynautsException;
import io.grpc.Context;
import io.grpc.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrpcExceptionAdviceTest {

    @Mock
    private MessageSource messageSource;

    private GrpcExceptionAdvice grpcExceptionAdvice;

    @BeforeEach
    public void setUp() {
        Context.current().withValue(GameContexts.LOCALE_CONTEXT_KEY, Locale.US).attach();
        grpcExceptionAdvice = new GrpcExceptionAdvice(messageSource);
    }

    @Test
    public void shouldHandleInvalidArgumentException() {
        when(messageSource.getMessage("key", null, Locale.US)).thenReturn("Message");

        var exception = new IllegalArgumentException("key");
        var result = grpcExceptionAdvice.handleInvalidArgumentException(exception);

        assertThat(result.getCode()).isEqualTo(Status.INVALID_ARGUMENT.getCode());
        assertThat(result.getDescription()).isEqualTo("Message");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    public void shouldHandleTheMonkeynautsException() {
        when(messageSource.getMessage("key", null, Locale.US)).thenReturn("Message");

        var exception = new TheMonkeynautsException("key");
        var result = grpcExceptionAdvice.handleTheMonkeynautsException(exception);

        assertThat(result.getCode()).isEqualTo(Status.FAILED_PRECONDITION.getCode());
        assertThat(result.getDescription()).isEqualTo("Message");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    public void shouldHandleNotFoundException() {
        when(messageSource.getMessage("validation.entity.not-found", null, Locale.US)).thenReturn("Message %s");
        when(messageSource.getMessage("entity", null, Locale.US)).thenReturn("Entity");

        var exception = new NotFoundException("entity");
        var result = grpcExceptionAdvice.handleNotFoundException(exception);

        assertThat(result.getCode()).isEqualTo(Status.FAILED_PRECONDITION.getCode());
        assertThat(result.getDescription()).isEqualTo("Message Entity");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    public void shouldHandleUsernameAlreadyTakenException() {
        when(messageSource.getMessage("validation.duplicate.credentials", null, Locale.US)).thenReturn("%s");

        var exception = new CredentialsAlreadyTakenException("Email");
        var result = grpcExceptionAdvice.handleCredentialsAlreadyTakenException(exception);

        assertThat(result.getCode()).isEqualTo(Status.INVALID_ARGUMENT.getCode());
        assertThat(result.getDescription()).isEqualTo("email");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    public void shouldHandleAuthenticationCredentialsNotFoundException() {
        when(messageSource.getMessage("validation.authentication.unauthenticated", null, Locale.US)).thenReturn("Message");

        var exception = new AuthenticationCredentialsNotFoundException("");

        var result = grpcExceptionAdvice.handleAuthenticationCredentialsNotFoundException(exception);

        assertThat(result.getCode()).isEqualTo(Status.UNAUTHENTICATED.getCode());
        assertThat(result.getDescription()).isEqualTo("Message");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    public void shouldHandleMonkeynautRequiredAttributeException() {
        when(messageSource.getMessage("validation.required.monkeynaut", null, Locale.US)).thenReturn("Message %s");

        var exception = new MonkeynautRequiredAttributeException("class");

        var result = grpcExceptionAdvice.handleMonkeynautRequiredAttributeException(exception);

        assertThat(result.getCode()).isEqualTo(Status.INVALID_ARGUMENT.getCode());
        assertThat(result.getDescription()).isEqualTo("Message class");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    public void shouldHandleCredentialsRequiredException() {
        when(messageSource.getMessage("validation.required.credentials", null, Locale.US)).thenReturn("Message");

        var exception = new CredentialsRequiredException();

        var result = grpcExceptionAdvice.handleCredentialsRequiredException(exception);

        assertThat(result.getCode()).isEqualTo(Status.INVALID_ARGUMENT.getCode());
        assertThat(result.getDescription()).isEqualTo("Message");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    public void shouldHandleInvalidCredentialsException() {
        when(messageSource.getMessage("validation.authentication.invalid-credentials", null, Locale.US)).thenReturn("Message");

        var exception = new InvalidCredentialsException();

        var result = grpcExceptionAdvice.handleInvalidCredentialsException(exception);

        assertThat(result.getCode()).isEqualTo(Status.PERMISSION_DENIED.getCode());
        assertThat(result.getDescription()).isEqualTo("Message");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    public void shouldHandleInvalidFormatException() {
        when(messageSource.getMessage("validation.attribute.invalid", null, Locale.US)).thenReturn("Message %s %s");

        var exception = new InvalidFormatException("type", "data");

        var result = grpcExceptionAdvice.handleInvalidFormatException(exception);

        assertThat(result.getCode()).isEqualTo(Status.INVALID_ARGUMENT.getCode());
        assertThat(result.getDescription()).isEqualTo("Message type data");
        assertThat(result.getCause()).isEqualTo(exception);
    }

}