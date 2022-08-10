import { CreateMonkeynautSaleBusinessLogic } from '../core/business-logic/create-monkeynaut-sale';
import { CreatePackSaleBusinessLogic } from '../core/business-logic/create-pack-sale';
import { CreateShipSaleBusinessLogic } from '../core/business-logic/create-ship-sale';
import { PackType } from '../domain/enums/pack-type';
import { SaleCrypto } from '../domain/enums/sale-crypto';
import { SaleType } from '../domain/enums/sale-type';

export type MonkeynautSaleUniqueProps = {
  private: number;
  sergeant: number;
  captain: number;
  major: number;
};

export type ShipSaleUniqueProps = {
  rankB: number;
  rankA: number;
  rankS: number;
};

export type PackSaleUniqueProps = {
  type: PackType;
};

export type SaleCommons = {
  crypto: SaleCrypto;
  price: number;
  startDate: Date;
  endDate?: Date;
  quantity: number;
  totalUnitsSold: number;
  active?: boolean;
  currentQuantityAvailable?: number;
};

type CreateSaleRequestDTO = SaleCommons & {
  type: SaleType;
  adminId: string;
  sale:
    | CreateMonkeynautSaleBusinessLogic
    | CreateShipSaleBusinessLogic
    | CreatePackSaleBusinessLogic;
  saleMonkeynaut?: MonkeynautSaleUniqueProps;
  saleShip?: ShipSaleUniqueProps;
  salePack?: PackSaleUniqueProps;
};

export { CreateSaleRequestDTO };
