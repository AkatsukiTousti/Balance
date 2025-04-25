const express = require("express");
const axios = require("axios");
require("dotenv").config();

const app = express();
app.use(express.json());

let accessTokenGlobal = "";

// Root
app.get("/", (req, res) => {
  res.send("Serveur en ligne 🚀 !");
});

// OAuth Withings
app.get("/auth", (req, res) => {
  const authUrl = `https://account.withings.com/oauth2_user/authorize?response_type=code&client_id=${process.env.CLIENT_ID}&scope=user.metrics&redirect_uri=${process.env.REDIRECT_URI}`;
  res.redirect(authUrl);
});

// Callback Withings
app.get("/callback", async (req, res) => {
  const { code } = req.query;
  if (!code) return res.status(400).send("Code manquant");

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
    console.log("✅ Access token :", accessTokenGlobal);

    // Redirige vers ton appli Android (via Deep Link)
    res.redirect("balanceapp://connected");
  } catch (err) {
    console.error("Erreur OAuth :", err.response?.data || err.message);
    res.status(500).send("Erreur d'authentification.");
  }
});

// Données Withings
app.get("/data", async (req, res) => {
  if (!accessTokenGlobal) return res.status(403).send("Non authentifié");

  try {
    const response = await axios.get("https://wbsapi.withings.net/measure", {
      params: {
        action: "getmeas",
        access_token: accessTokenGlobal,
      },
    });

    res.json(response.data);
  } catch (err) {
    console.error("Erreur récupération données :", err.response?.data || err.message);
    res.status(500).send("Erreur récupération données.");
  }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`🚀 Serveur actif sur http://localhost:${PORT}`);
});
