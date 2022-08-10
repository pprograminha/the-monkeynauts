package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.ListMonkeynaut;
import com.themonkeynauts.game.application.port.out.persistence.LoadMonkeynautPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.domain.Monkeynaut;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCaseAdapter
@RequiredArgsConstructor
public class ListMonkeynautUseCase implements ListMonkeynaut {

    private final LoadMonkeynautPort loadMonkeynaut;

    @Override
    public List<Monkeynaut> all() {
        return loadMonkeynaut.all();
    }
}
