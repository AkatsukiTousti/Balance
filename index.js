const express = require("express");
const axios = require("axios");
require("dotenv").config(); // Charge les variables d’environnement

const app = express();
app.use(express.json());

let accessTokenGlobal = "";

// Route racine
app.get("/", (req, res) => {
  res.send("Serveur en ligne 🚀 !");
});

// Auth Withings
app.get("/auth", (req, res) => {
  const clientID = process.env.CLIENT_ID;
  const redirectUri = process.env.REDIRECT_URI;
  const authUrl = `https://account.withings.com/oauth2_user/authorize?response_type=code&client_id=${clientID}&scope=user.metrics&redirect_uri=${redirectUri}`;
  res.redirect(authUrl);
});

// Callback Withings
app.get("/callback", async (req, res) => {
  const code = req.query.code;
  if (!code) return res.status(400).send("Code de validation manquant.");

  try {
    const response = await axios.post("https://account.withings.com/oauth2/token", null, {
      params: {
        grant_type: "authorization_code",
        client_id: process.env.CLIENT_ID,
        client_secret: process.env.CLIENT_SECRET,
        code,
        redirect_uri: process.env.REDIRECT_URI,
      },
    });

    accessTokenGlobal = response.data.access_token;
    console.log("✔️ Access token :", accessTokenGlobal);
    res.send("✅ Connecté ! Vous pouvez retourner dans l'application.");
  } catch (err) {
    console.error("❌ Erreur callback :", err.response?.data || err.message);
    res.status(500).send("Erreur d’authentification.");
  }
});

// Route pour récupérer les mesures
app.get("/data", async (req, res) => {
  if (!accessTokenGlobal) return res.status(403).send("Token non trouvé.");

  try {
    const response = await axios.get("https://wbsapi.withings.net/measure", {
      params: {
        action: "getmeas",
        access_token: accessTokenGlobal,
      },
    });
    res.json(response.data);
  } catch (err) {
    console.error("Erreur API Withings :", err.response?.data || err.message);
    res.status(500).send("Erreur récupération données.");
  }
});

// Lancer le serveur
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`🚀 Serveur en ligne sur http://localhost:${PORT}`);
});