package com.themonkeynauts.game.application.port.out.persistence;

import com.themonkeynauts.game.domain.Monkeynaut;

public interface SaveMonkeynautPort {

    Monkeynaut save(Monkeynaut monkeynaut);

}
