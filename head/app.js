var express = require('express');
var nodemailer = require("nodemailer");
var request = require('request');

var app = express();

var oneDay = 86400000;

var handleNextCharacters = function (req, res) {
    var word = req.query.word;
    console.log(word);
    var last_letter = word.slice(-1);
    var pre_last_letter = word.slice(-2, -1);
    if (last_letter == ' ') {
        last_letter = '_';
    }
    if (pre_last_letter == ' ') {
        pre_last_letter = '_';
    }
    request({
            url: 'http://localhost:8080/nlp-service/probability/get',
            method: 'POST',
            json: true,
            headers: {
                'content-type': 'application/json'
            }, body: {l1: last_letter}
        },
        function (error, response, body) {
            if (!error && response.statusCode == 200) {
                //console.log(body)
                var probability_map = body;
                var letterToScore = {};
                var alphabet = "AĄBCĆDEĘFGHIJKLŁMNŃOÓPQRSŚTUVWXYZŹŻ ".split("");
                alphabet.forEach(function (letter) {
                    var letter_probability = 0.0;
                    if (probability_map.hasOwnProperty(letter)) {
                        letter_probability = probability_map[letter];
                    }
                    var probabilityScore = Math.max(Math.min(letter_probability * 1000, 70), 20);
                    console.log(probabilityScore);
                    if (letter != " ") {
                        letterToScore["letter_score_" + letter] = probabilityScore;
                    } else {
                        letterToScore["letter_score_"] = probabilityScore;
                    }
                });
                console.log(letterToScore);
                res.send(JSON.stringify(letterToScore));
            }
        });
    if (pre_last_letter) {
        request({
                url: 'http://localhost:8080/nlp-service/probability/add',
                method: 'POST',
                json: true,
                headers: {
                    'content-type': 'application/json'
                },
                body: {
                    l1: pre_last_letter,
                    l2: last_letter
                }
            },
            function (error, response, body) {
                if (!error && response.statusCode == 200) {
                    console.log(body);
                } else {
                    console.log(error);
                }
            }
        )
        ;
    }

}

app.use(function (req, res, next) {
    if (req.url.startsWith("/getCharacters?")) {
        return handleNextCharacters(req, res);
    }
    return next();
});


app.use(express.compress());
app.use(express.static(__dirname + '/', {maxAge: oneDay}));

app.use(express.bodyParser());
var port = 8081;
app.listen(process.env.PORT || port);
