import { IShip, Ship } from '@modules/ships/domain/entities/ship';
import { IShipsRepository } from '@modules/ships/domain/repositories/ships-repositories';
import {
  Ship as PrismaShip,
  Monkeynaut as PrismaMonkeynaut,
} from '@prisma/client';
import { prisma } from '@shared/infra/database/prisma/client';
import { AsyncMaybe } from '@shared/core/logic/maybe';
import { parseCrew } from '@modules/crews/infra/database/prisma/repositories/prisma-crews-repositories';
import { ICrew } from '@modules/crews/domain/entities/crew';
import {
  IMonkeynaut,
  Monkeynaut,
} from '@modules/monkeynauts/domain/entities/monkeynaut';

const parseShip = (ship: PrismaShip): IShip => {
  return new Ship(ship, {
    id: ship.id,
    createdAt: ship.createdAt,
    updatedAt: ship.updatedAt,
  }).ship;
};

const parseMonkeynaut = (monkeynaut: PrismaMonkeynaut): IMonkeynaut => {
  return new Monkeynaut(monkeynaut, {
    id: monkeynaut.id,
    createdAt: monkeynaut.createdAt,
    updatedAt: monkeynaut.updatedAt,
  }).monkeynaut;
};

class PrismaShipsRepository implements IShipsRepository {
  async saveMany(ship: Partial<IShip>): Promise<void> {
    await prisma.ship.updateMany({
      data: {
        ...ship,
      },
    });
  }

  async resetShipsFuel(): Promise<void> {
    await prisma.$queryRawUnsafe(`
      UPDATE "ships" 
        SET 
        fuel = ships."tankCapacity", 
        "canRefuelAtStation" = true
    `);
  }

  async findById<T extends boolean>(
    shipId: string,
    relationships?: T,
  ): Promise<
    T extends true ? AsyncMaybe<IShip & { crew: ICrew[] }> : AsyncMaybe<IShip>
  > {
    if (!relationships) {
      const ship = await prisma.ship.findUnique({
        where: {
          id: shipId,
        },
        include: {
          crew: true,
        },
      });

      if (!ship) {
        return null;
      }

      return parseShip(ship);
    }

    const ship = await prisma.ship.findUnique({
      where: {
        id: shipId,
      },
      include: {
        crew: {
          include: {
            monkeynaut: true,
          },
        },
      },
    });

    if (!ship) {
      return null;
    }

    const { crew, ...shipRest } = ship;

    return {
      ...parseShip(shipRest),
      crew: crew.map(parseCrew),
    } as unknown as Promise<
      T extends true ? AsyncMaybe<IShip & { crew: ICrew[] }> : AsyncMaybe<IShip>
    >;
  }

  async findByIdAndPlayerId(
    shipId: string,
    playerId: string,
  ): AsyncMaybe<IShip & { crew: ICrew[] }> {
    const ship = await prisma.ship.findFirst({
      where: {
        id: shipId,
        playerId,
      },
      include: {
        crew: {
          include: {
            monkeynaut: true,
          },
        },
      },
    });

    if (!ship) {
      return null;
    }

    const { crew, ...shipRest } = ship;

    return {
      ...parseShip(shipRest),
      crew: crew.map(parseCrew),
    };
  }

  async save(ship: Ship): Promise<void> {
    const { id: shipId, ...props } = ship;

    await prisma.ship.update({
      data: {
        ...props,
        updatedAt: new Date(),
      },
      where: {
        id: shipId,
      },
    });
  }

  async destroy(shipId: string): Promise<void> {
    await prisma.ship.delete({
      where: {
        id: shipId,
      },
    });
  }

  async create(ship: IShip): Promise<void> {
    const { id: shipId, ...props } = ship;

    await prisma.ship.create({
      data: {
        id: shipId,
        ...props,
      },
    });
  }

  async findMany(): Promise<IShip[]> {
    return prisma.ship.findMany();
  }

  async listAllShips(): Promise<
    (IShip & {
      crew: IMonkeynaut[];
    })[]
  > {
    const ships = await prisma.ship.findMany({
      include: {
        crew: {
          include: {
            monkeynaut: true,
          },
        },
      },
    });

    const shipsCustom = ships.map(async ship => {
      const { crew, ...shipRest } = ship;

      return {
        ...parseShip(shipRest),
        crew: crew.map(mapCrew => parseMonkeynaut(mapCrew.monkeynaut)),
      };
    });

    const result = await Promise.all(shipsCustom);

    return result;
  }

  async listAllShipsFromPlayer(playerId: string): Promise<
    (IShip & {
      crew: IMonkeynaut[];
    })[]
  > {
    const ships = await prisma.ship.findMany({
      where: {
        playerId,
      },
      include: {
        crew: {
          include: {
            monkeynaut: true,
          },
        },
      },
    });

    const shipsCustom = ships.map(async ship => {
      const { crew, ...shipRest } = ship;

      return {
        ...parseShip(shipRest),
        crew: crew.map(mapCrew => parseMonkeynaut(mapCrew.monkeynaut)),
      };
    });

    const result = await Promise.all(shipsCustom);

    return result;
  }
}

export { PrismaShipsRepository };
