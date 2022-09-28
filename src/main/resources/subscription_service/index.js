'use strict';

const express = require('express');

// Constants
const PORT = 8080;
const HOST = '0.0.0.0';

// App
const app = express();
app.get('/', (req, res) => {
  res.sendStatus(200);
});
app.get('/:userId', (req, res) => {
  res.send('73afb3d7-930d-452f-8827-82bac8bb2af2');
});

app.listen(PORT, HOST);
console.log(`Running on http://${HOST}:${PORT}`);
