import path from 'node:path';
import fs from 'node:fs';
import { IStorageProvider } from '@shared/domain/providers/storage-provider';
import { storageConfig } from '../../../config/storage';

class DiskStorageProvider implements IStorageProvider {
  async saveFile(filename: string, folder: string): Promise<string> {
    const filePathWithFolder = path.resolve(
      storageConfig.paths.uploadsFolder,
      folder,
    );

    if (!fs.existsSync(filePathWithFolder)) {
      fs.mkdirSync(filePathWithFolder, {
        mode: 0o777,
      });
    }

    const oldFilePath = path.resolve(storageConfig.paths.tmpFolder, filename);
    const newFilePath = path.resolve(filePathWithFolder, filename);

    await fs.promises.rename(oldFilePath, newFilePath);

    return filename;
  }

  async deleteFile(filename: string, folder: string): Promise<void> {
    const filePath = path.resolve(
      storageConfig.paths.uploadsFolder,
      folder,
      filename,
    );

    try {
      await fs.promises.stat(filePath);
    } catch {
      return;
    }

    await fs.promises.unlink(filePath);
  }
}

export { DiskStorageProvider };
