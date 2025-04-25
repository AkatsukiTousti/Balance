const express = require("express");
const axios = require("axios");
const app = express();

require("dotenv").config(); // pour utiliser les variables d’environnement

let accessTokenGlobal = ""; // Pour test, en attendant une base de données

// Root
app.get("/", (req, res) => {
  res.send("Serveur en ligne 🚀 !");
});

// Redirection vers Withings (OAuth2)
app.get("/auth", (req, res) => {
  const authUrl = `https://account.withings.com/oauth2_user/authorize?response_type=code&client_id=${process.env.CLIENT_ID}&scope=user.info,user.metrics&redirect_uri=${process.env.REDIRECT_URI}`;
  res.redirect(authUrl);
});

// Callback de Withings
app.get("/callback", async (req, res) => {
  const { code } = req.query;
  if (!code) return res.status(400).send("Code manquant");

  try {
    const response = await axios.post("https://account.withings.com/oauth2/token", null, {
      params: {
        action: "requesttoken",
        grant_type: "authorization_code",
        client_id: process.env.CLIENT_ID,
        client_secret: process.env.CLIENT_SECRET,
        code,
        redirect_uri: process.env.REDIRECT_URI,
      },
    });

    const token = response.data.access_token;
    accessTokenGlobal = token; // Pour le test

    res.send("✅ Authentifié ! Vous pouvez fermer cette page et retourner dans l'app.");
  } catch (err) {
    console.error("Erreur:", err.response?.data || err.message);
    res.status(500).send("Erreur d'authentification.");
  }
});

// Route pour récupérer les mesures
app.get("/data", async (req, res) => {
  if (!accessTokenGlobal) return res.status(401).send("Non authentifié");

  try {
    const response = await axios.get("https://wbsapi.withings.net/measure", {
      params: {
        action: "getmeas",
        access_token: accessTokenGlobal,
      },
    });

    res.json(response.data);
  } catch (err) {
    console.error("Erreur mesure:", err.response?.data || err.message);
    res.status(500).send("Erreur de récupération.");
  }
});

// Utiliser le bon port sur Render
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`🚀 Serveur lancé sur http://localhost:${PORT}`);
});