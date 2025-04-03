const express = require("express");
const app = express();

// Utiliser le port fourni par Render
const PORT = process.env.PORT || 3000;

app.get("/", (req, res) => {
  res.send("Hello depuis Render !");
});

app.listen(PORT, () => {
  console.log(`Serveur démarré sur le port ${PORT}`);
});
