const express = require("express");
const axios = require("axios");
require("dotenv").config(); // Pour charger les variables d'environnement

const app = express();

// Utilisation des variables d'environnement pour plus de sécurité
const clientID = 82108352dc5fb2557c0686664160c4dcd047357dd0bb9110d3383bd9f0f1e3de;
const clientSecret = bd6bad12b42dc4b2b435f87e361b3d92942d683bac8dbe1d7c5fd922af67cf19;
const redirectUri = https://balance-xln2.onrender.com;

// Vérification des variables
if (!clientID || !clientSecret) {
  console.error("❌ ERREUR: CLIENT_ID ou CLIENT_SECRET non défini !");
  process.exit(1); // Arrête le serveur si les variables ne sont pas définies
}

// Étape 1 : Rediriger l'utilisateur vers l'authentification Withings
app.get("/auth", (req, res) => {
  const authUrl = `https://account.withings.com/oauth2_user/authorize?response_type=code&client_id=${clientID}&scope=user.info&redirect_uri=${redirectUri}`;
  res.redirect(authUrl);
});

// Étape 2 : Gérer la redirection après autorisation
app.get("/callback", async (req, res) => {
  const { code } = req.query;
  if (!code) return res.status(400).send("Code de validation manquant.");

  try {
    const response = await axios.post("https://account.withings.com/oauth2/token", null, {
      params: {
        client_id: clientID,
        client_secret: clientSecret,
        code,
        grant_type: "authorization_code",
        redirect_uri: redirectUri,
      },
    });

    const accessToken = response.data.access_token;
    console.log("✔️ Token d'accès obtenu :", accessToken);
    res.send(`Token d'accès obtenu : ${accessToken}`);
  } catch (error) {
    console.error("❌ Erreur de récupération du token :", error.response?.data || error.message);
    res.status(500).send("Erreur lors de l'authentification.");
  }
});

// Utiliser le port de Render
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`🚀 Serveur démarré sur http://localhost:${PORT}`);
});
