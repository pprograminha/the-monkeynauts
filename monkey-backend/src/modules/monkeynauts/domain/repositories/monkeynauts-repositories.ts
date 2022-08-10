import { ICrew } from '@modules/crews/domain/entities/crew';
import { AsyncMaybe } from '@shared/core/logic/maybe';
import { IMonkeynaut } from '../entities/monkeynaut';

interface IMonkeynautsRepository {
  create(monkeynaut: IMonkeynaut): Promise<void>;
  save(monkeynaut: IMonkeynaut): Promise<void>;
  update(monkeynaut: IMonkeynaut): Promise<void>;
  destroy(monkeynautId: string): Promise<void>;
  listAllMonkeynauts(): Promise<IMonkeynaut[]>;
  listAllMonkeynautsFromPlayer(playerId: string): Promise<IMonkeynaut[]>;
  findById(monkeynautId: string): AsyncMaybe<IMonkeynaut>;

  findMany(): Promise<IMonkeynaut[]>;
  findManyByCrews(crews: ICrew[]): Promise<IMonkeynaut[]>;
}
export { IMonkeynautsRepository };
