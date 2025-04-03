const express = require('express');
const axios = require('axios');
const app = express();

const clientID = '82108352dc5fb2557c0686664160c4dcd047357dd0bb9110d3383bd9f0f1e3de';  // Remplacez par votre client_id
const clientSecret = 'bd6bad12b42dc4b2b435f87e361b3d92942d683bac8dbe1d7c5fd922af67cf19';  // Remplacez par votre client_secret
const redirectUri = 'https://balance-xln2.onrender.com';  // Remplacez par l'URL publique de votre serveur

// Étape 1 : Rediriger l'utilisateur vers l'authentification Withings
app.get('/auth', (req, res) => {
  const authUrl = `https://account.withings.com/oauth2_user/authorize?response_type=code&client_id=${clientID}&scope=user.info&redirect_uri=${redirectUri}`;
  res.redirect(authUrl);
});

// Étape 2 : Gérer la redirection de Withings après autorisation
app.get('/callback', (req, res) => {
  const { code } = req.query; // Récupérer le code de la redirection
  console.log(`Code reçu : ${code}`);

  // Échanger le code contre un access_token
  axios.post('https://account.withings.com/oauth2/token', null, {
    params: {
      client_id: clientID,
      client_secret: clientSecret,
      code,
      grant_type: 'authorization_code',
      redirect_uri: redirectUri,
    },
  })
  .then(response => {
    const accessToken = response.data.access_token;
    console.log('Token d\'accès :', accessToken);
    res.send(`Token d'accès obtenu : ${accessToken}`);
  })
  .catch(error => {
    console.error('Erreur de récupération du token :', error);
    res.send('Erreur lors de l\'authentification.');
  });
});
// Démarrer le serveur sur le port 3000
app.listen(3000, () => {
  console.log('Serveur démarré sur http://localhost:3000');
});

