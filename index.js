const http = require('http');

// Render impose l'utilisation de process.env.PORT
const PORT = process.env.PORT || 3000;

const server = http.createServer((req, res) => {
  res.writeHead(200, { 'Content-Type': 'text/plain' });
  res.end('Hello depuis Render 🚀');
});

server.listen(PORT, () => {
  console.log(`Serveur démarré sur le port ${PORT}`);
});
