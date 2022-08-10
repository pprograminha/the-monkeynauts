import { Commons } from '@shared/types/commons';
import { Maybe } from '@shared/core/logic/maybe';
import crypto from 'node:crypto';

type ShipPropsOmittedCommons = {
  ownerId: Maybe<string>;
  playerId: Maybe<string>;
  name: string;
  role: string;
  rank: string;
  bonusValue: number;
  bonusDescription: string;
  tankCapacity: number;
  crewCapacity: number;
  fuel: number;
  breedCount: number;
  onSale: boolean;
  canRefuelAtStation: boolean;
};

type ShipProps = ShipPropsOmittedCommons & Commons;

type ShipCommons = Partial<
  {
    id: string;
  } & Commons
>;

export interface IShip extends ShipProps {
  id: string;
}

export class Ship implements IShip {
  private _id: string;

  private _props: ShipProps;

  get ship(): IShip {
    return {
      id: this._id,
      ownerId: this._props.ownerId,
      playerId: this._props.playerId,
      name: this._props.name,
      role: this._props.role,
      rank: this._props.rank,
      bonusValue: this._props.bonusValue,
      bonusDescription: this._props.bonusDescription,
      tankCapacity: this._props.tankCapacity,
      crewCapacity: this._props.crewCapacity,
      fuel: this._props.fuel,
      breedCount: this._props.breedCount,
      onSale: this._props.onSale,
      canRefuelAtStation: this._props.canRefuelAtStation,
      createdAt: this._props.createdAt,
      updatedAt: this._props.updatedAt,
    };
  }

  set assign(props: Partial<ShipProps>) {
    this._props = {
      ...this._props,
      ...props,
    };
  }

  get id(): string {
    return this._id;
  }

  get ownerId(): Maybe<string> {
    return this._props.ownerId;
  }

  get playerId(): Maybe<string> {
    return this._props.playerId;
  }

  get name(): string {
    return this._props.name;
  }

  get role(): string {
    return this._props.role;
  }

  get rank(): string {
    return this._props.rank;
  }

  get bonusValue(): number {
    return this._props.bonusValue;
  }

  get bonusDescription(): string {
    return this._props.bonusDescription;
  }

  get tankCapacity(): number {
    return this._props.tankCapacity;
  }

  get crewCapacity(): number {
    return this._props.crewCapacity;
  }

  get fuel(): number {
    return this._props.fuel;
  }

  get breedCount(): number {
    return this._props.breedCount;
  }

  get onSale(): boolean {
    return this._props.onSale;
  }

  get canRefuelAtStation(): boolean {
    return this._props.canRefuelAtStation;
  }

  get updatedAt(): Date {
    return this._props.updatedAt;
  }

  get createdAt(): Date {
    return this._props.createdAt;
  }

  constructor(props: ShipPropsOmittedCommons, commons?: ShipCommons) {
    this._id = commons?.id || crypto.randomUUID();

    this._props = {
      ...props,
      createdAt: commons?.createdAt || new Date(),
      updatedAt: commons?.updatedAt || new Date(),
    };
  }
}
