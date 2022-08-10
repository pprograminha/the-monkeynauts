package com.themonkeynauts.game.application.usecase;

import com.google.common.base.Strings;
import com.themonkeynauts.game.application.port.in.CreateAccount;
import com.themonkeynauts.game.application.port.in.CreatePlayer;
import com.themonkeynauts.game.application.port.in.CreatePlayerCommand;
import com.themonkeynauts.game.application.port.out.persistence.LoadUserPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveUserPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.common.exception.CredentialsAlreadyTakenException;
import com.themonkeynauts.game.common.exception.CredentialsRequiredException;
import com.themonkeynauts.game.common.exception.InvalidFormatException;
import com.themonkeynauts.game.domain.Role;
import com.themonkeynauts.game.domain.User;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.regex.Pattern;

@Transactional
@UseCaseAdapter
@RequiredArgsConstructor
public class CreatePlayerUseCase implements CreatePlayer {

    private final CreateAccount createAccount;
    private final SaveUserPort saveUser;
    private final LoadUserPort loadUser;

    @Override
    public User create(CreatePlayerCommand command) {
        validate(command);

        var account = createAccount.create();

        var player = new User(command.email(), command.nickname(), command.password(), account);
        addPlayerRoles(player);

        var savedPlayer = saveUser.save(player);

        return savedPlayer;
    }

    private void addPlayerRoles(User player) {
        player.addRole(Role.ADMIN);
        player.addRole(Role.PLAYER);
    }

    private void validate(CreatePlayerCommand command) {
        if (Strings.isNullOrEmpty(command.email()) || Strings.isNullOrEmpty(command.nickname()) || Strings.isNullOrEmpty(command.password())) {
            throw new CredentialsRequiredException();
        }
        var regexPattern = "^(.+)@(\\S+)$";
        if (!Pattern.compile(regexPattern).matcher(command.email()).matches()) {
            throw new InvalidFormatException("email", command.email());
        }
        var userByEmail = loadUser.byEmail(command.email());
        if (userByEmail.isPresent()) {
            throw new CredentialsAlreadyTakenException(command.email());
        }
        var userByNickname = loadUser.byNickname(command.nickname());
        if (userByNickname.isPresent()) {
            throw new CredentialsAlreadyTakenException(command.nickname());
        }
    }
}
