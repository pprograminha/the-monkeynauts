import { inject, injectable } from 'tsyringe';

import { ICrew, Crew } from '@modules/crews/domain/entities/crew';

import { IPlayersRepository } from '@modules/players/domain/repositories/players-repository';

import { AppError } from '@shared/errors/app-error';

import { Log } from '@modules/logs/domain/entities/log';
import { ILogsRepository } from '@modules/logs/domain/repositories/logs-repositories';
import { IMonkeynautsRepository } from '@modules/monkeynauts/domain/repositories/monkeynauts-repositories';
import { IShipsRepository } from '@modules/ships/domain/repositories/ships-repositories';
import { Either, right } from '@shared/core/logic/either';

import { ICrewsRepository } from '../../domain/repositories/crews-repositories';

export type CreateCrewRequestDTO = {
  shipId: string;
  monkeynautId: string;
  playerId: string;
};

type CreateCrewResponse = Either<
  Error,
  {
    crew: ICrew;
  }
>;

@injectable()
class CreateCrewBusinessLogic {
  constructor(
    @inject('CrewsRepository')
    private crewsRepository: ICrewsRepository,

    @inject('MonkeynautsRepository')
    private monkeynautsRespository: IMonkeynautsRepository,

    @inject('ShipsRepository')
    private shipsRespository: IShipsRepository,

    @inject('PlayersRepository')
    private playersRepository: IPlayersRepository,

    @inject('LogsRepository')
    private logsRepository: ILogsRepository,
  ) {}

  async execute({
    monkeynautId,
    shipId,
    playerId,
  }: CreateCrewRequestDTO): Promise<CreateCrewResponse> {
    const foundPlayer = await this.playersRepository.findById(playerId);

    if (!foundPlayer) {
      throw new AppError('Player does not exist', 404);
    }

    const foundShip = await this.shipsRespository.findById(shipId, true);

    if (!foundShip) {
      throw new AppError('Ship does not exist', 404);
    }

    if (foundShip.crew.length === foundShip.crewCapacity) {
      throw new AppError(
        `You cannot exceed the maximum crew capacity(${foundShip.crewCapacity})`,
        403,
      );
    }

    const foundMonkeynaut = await this.monkeynautsRespository.findById(
      monkeynautId,
    );

    if (!foundMonkeynaut) {
      throw new AppError('Monkeynaut does not exist', 404);
    }

    const foundCrewByMonkeynautId =
      await this.crewsRepository.findUniqueByMonkeynautId(monkeynautId);

    if (foundCrewByMonkeynautId) {
      throw new AppError('Monkeynaut is already in a crew', 403);
    }

    const { crew } = new Crew({
      monkeynautId,
      shipId,
    });

    await this.crewsRepository.create(crew);

    const { log } = new Log({
      action: `Crew has created on player account. crewId: ${crew.id}`,
      playerId,
      txHash: null,
    });

    await this.logsRepository.create(log);

    return right({
      crew,
    });
  }
}

export { CreateCrewBusinessLogic };
