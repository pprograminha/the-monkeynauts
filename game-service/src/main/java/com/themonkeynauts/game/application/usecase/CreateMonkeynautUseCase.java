package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.CreateMonkeynaut;
import com.themonkeynauts.game.application.port.in.CreateMonkeynautCommand;
import com.themonkeynauts.game.application.port.out.persistence.LoadMonkeynautPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveMonkeynautPort;
import com.themonkeynauts.game.application.port.out.random.RandomGeneratorPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.common.exception.MonkeynautRequiredAttributeException;
import com.themonkeynauts.game.common.exception.NumberOfMonkeynautsExceededException;
import com.themonkeynauts.game.domain.Monkeynaut;
import com.themonkeynauts.game.domain.MonkeynautClass;
import com.themonkeynauts.game.domain.MonkeynautRank;
import lombok.RequiredArgsConstructor;

@UseCaseAdapter
@RequiredArgsConstructor
public class CreateMonkeynautUseCase implements CreateMonkeynaut {

    private static final int MAX_MONKEYNAUTS_PER_PLAYER = 4;

    private final RandomGeneratorPort randomGenerator;
    private final SaveMonkeynautPort saveMonkeynaut;
    private final LoadMonkeynautPort loadMonkeynaut;

    @Override
    public Monkeynaut createRandom() {
        var classType = randomGenerator.monkeynautClassType();
        var rankType = randomGenerator.monkeynautRankType();
        var command = new CreateMonkeynautCommand(new MonkeynautClass(classType), new MonkeynautRank(rankType));
        return create(command);
    }

    @Override
    public Monkeynaut create(CreateMonkeynautCommand command) {
        validate(command);
        var firstName = randomGenerator.firstName();
        var lastName = randomGenerator.lastName();
        var attributes = randomGenerator.monkeynautAttributes(command.rank().getType());
        var monkeynaut = new Monkeynaut(firstName, lastName, command.clazz(), command.rank(), attributes);
        return saveMonkeynaut.save(monkeynaut);
    }

    private void validate(CreateMonkeynautCommand command) {
        if (command.clazz() == null) {
            throw new MonkeynautRequiredAttributeException("class");
        }
        if (command.rank() == null) {
            throw new MonkeynautRequiredAttributeException("rank");
        }
        var monkeynauts = loadMonkeynaut.all();
        if (monkeynauts.size() == MAX_MONKEYNAUTS_PER_PLAYER) {
            throw new NumberOfMonkeynautsExceededException();
        }
    }
}
