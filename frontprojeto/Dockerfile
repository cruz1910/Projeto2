# Imagem base
FROM node:18

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos de dependências primeiro (para aproveitar o cache)
COPY package*.json ./

# Instala as dependências
RUN npm install

# Copia o restante do código-fonte
COPY . .

# Expõe a porta padrão (React ou Next.js geralmente usam 3000)
EXPOSE 3000

# Comando padrão ao iniciar o container
CMD ["npm", "run", "start"]
