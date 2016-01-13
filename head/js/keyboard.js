var idToScore = {};
var scoreLabelToLetter = {};
var scores;
var headControlledPointer;
var typed_text;

var initiateScore = function(letterToScore) {
	for (var letter in letterToScore) {
		idToScore[letter] = letterToScore[letter];
	}
	scores.each(function(){
		scoreLabelToLetter[this.id] = document.getElementById((this.id).replace('_score', ""));
		var currentScore = idToScore[this.id];
		$(scoreLabelToLetter[this.id]).height(currentScore * 3 - 40);
		$(scoreLabelToLetter[this.id]).css('font-size', Math.floor(currentScore * 3 - 40) + "px");
	});
}

var changeSizeOfLabels = function() {
	var total = 0;
	for (var key in idToScore) {
	    total += idToScore[key];
	}
	for (var key in idToScore) {
		var width = idToScore[key] * 88.0 / total + "%";
	    var id = key.replace("_score", "");
	    $('#' + id).width(width);
	}

}

var scaleValues = function() {
	for (var key in idToScore) {
		idToScore[key] = Math.max(parseInt(idToScore[key] * 0.01, 10), 2);
	}
}

var initNextNLPCharacters = function() {
	$.ajax({
	  url: "/getCharacters",
	  data: "word=" + encodeURIComponent(typed_text.val()),
	  success: function(data){
		var letterToScore = JSON.parse(data);
		initiateScore(letterToScore);
		changeSizeOfLabels();	  	
	  }
	});
}

var getLetterIfApply = function() {
	for (var key in idToScore) {
		if (idToScore[key] > 120 ) {
			var text = key.replace('letter_score_', "");
 			if (text == "") {
 				text = " ";
 			}
 			typed_text.val( typed_text.val() + text);
			initNextNLPCharacters();
		}
	}
}

var getXCenter = function(element){
	return element.position().left + element.width() / 2;
}

var updateScore = function() {
	scores.each(function(score){
		var id = scores[score].id;
		var letter = $(scoreLabelToLetter[id]);
		var gainedScore = getGainedScore(letter);
		var currentScore = idToScore[id];
		
		$(scoreLabelToLetter[id]).height(currentScore * 3 - 40);
		$(scoreLabelToLetter[id]).css('font-size', Math.floor(currentScore * 3 - 40) + "px");

		if (currentScore < 20 && gainedScore < 0) {
			return;
		}

		idToScore[id] += gainedScore * 0.01;
		$(this).text(Math.round(idToScore[id] * 100) / 100);
	});
	changeSizeOfLabels();
	getLetterIfApply();
}


var getGainedScore = function(letter) {
	var pointerXCenter = getXCenter(headControlledPointer);	
	var letterXCenter = getXCenter(letter);
	var distance = Math.abs(pointerXCenter - letterXCenter);

	if (distance < letter.width() / 2 ) {
		return 1.5 * (headControlledPointer.position().top - 100);
	} else if (distance < 500) { 
		return 0.01 * (500 - distance) - 50;
	} else {
		return -80.0;	
	}
}

var x_delta = 1.0;

var min_x = 0;
var max_x = screen.width;

function ColorGenerator() {
	this.self = this;
	var color = 1;

	this.getR = function() {
		return (color * 123 + 124) % 255;
	}
	this.getG = function() {
		return (color * 97 + 343) % 255 ;
	}
	this.getB = function() {
		return (color * 23 + 171) % 255;
	}
	this.getColor = function() {
		color += 1;
		return 'rgba(' + this.getR() + ", "  + this.getG() + ", " + this.getB() + ', 0.7)';
	}
}

var getInitialScore = function() {
	var letterToScore = {};
	scores.each(function(){
		letterToScore[this.id] = 20;
	})
	return letterToScore; 
}

$(document).ready(function(){
	typed_text = $('#typed_text');
	scores = $('.letter_scores .letter_score');
	headControlledPointer = $("#target");
	var colorGenerator = new ColorGenerator();
	$(".letters > .letter").each(function(e, v){$(v).css("background-color", colorGenerator.getColor())});
	
	var letterToScore = getInitialScore();
	initiateScore(letterToScore);

  	var HeadKeyboard = {
	    update : function() {
		        var x = xLabs.getConfig( "state.head.x" );
		        var y = xLabs.getConfig( "state.head.y" );

		        headControlledPointer.css('left', (parseFloat(x) + x_delta) * screen.width);
		        // headControlledPointer.css('top', y * screen.height / 2 + 200);
				headControlledPointer.css('top',  250);

		        updateScore();
	      },

	    ready : function() {
	          xLabs.setConfig( "system.mode", "head" );
	          xLabs.setConfig( "browser.canvas.paintHeadPose", "0" );
	          window.addEventListener( "beforeunload", function() {
	              xLabs.setConfig( "system.mode", "off" );
	          });
	    }
  	};
	

  	xLabs.setup( HeadKeyboard.ready, HeadKeyboard.update, null, "<INSERT TOKEN HERE>" );
});
