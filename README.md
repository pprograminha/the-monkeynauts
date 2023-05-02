
## Para iniciar o projeto, execute os seguintes comandos:

**-- web**

Instale as dependências:
```zsh 
yarn
```
Inicie o ReactJS:
```zsh
yarn dev
// npm run dev
// pnpm dev
```


**-- server**

Copie o .env.example em .env:
```zsh 
cp .env.example .env
```
Inicie o container Postgres:
```zsh 
docker-compose up -d
```
Instale as dependências:
```zsh 
yarn
```
Execute as migrations do Prisma ORM:
```zsh
yarn prisma migrate dev
// npx prisma migrate dev
// pnpm prisma migrate dev
```
Execute as seeders do Prisma ORM:
```zsh
yarn prisma db seed
// npx prisma db seed
// pnpm prisma db seed
```
Inicie servidor:
```zsh
yarn dev:server
// npm run dev
// pnpm dev
```
