var stripe = require('stripe')(
  "sk_test_Xw2AJmaRSh1FiFCjBl6O6WsB"
);

exports.handlePayment = (req, res) => {
  res.set('Access-Control-Allow-Origin', '*');

  if (req.method === 'OPTIONS') {
    // Send response to OPTIONS requests
    res.set('Access-Control-Allow-Methods', 'POST, GET');
    res.set("Access-Control-Allow-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
    res.set('Access-Control-Max-Age', '3600');
    res.status(204).send('');
  } else {
    // Set CORS headers for the main request
    res.set('Access-Control-Allow-Origin', '*');
    console.log("RequestBody: ", req.body);
	stripe.charges.create(req.body)
  	.then(charge => res.send(charge))
  	.catch(err => {
    console.log("Error:", err);
    res.status(500).send({error: "Purchase Failed"});
  });
  }
};

