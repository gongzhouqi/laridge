
var game = {
    pageRoot : null,
    startGame : function(root) {
        this.pageRoot = root;
        root.innerHTML = gamePageTemplate;
    },
    process : function(response) {
        processGameWait(response);
    },
}

function inputToGame(input) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/gameInput?input="+input, true);
    xmlHttp.send(null);
}

function processGameWait(response) {
    if (response == "ENABLE") {
        alert("Your turn");
        document.getElementById("number-guess").disabled = false;
        document.getElementById("guess-submit-button").disabled = false;
    } else if (response.startsWith("WINNER")) {
        alert(response);
    } else {
        var h = document.createElement("h6");
        h.innerText = response;
        game.pageRoot.appendChild(h);
    }
}

function makeAGuess() {
    var textArea = document.getElementById("number-guess");
    var guess = textArea.value;
    textArea.value = "";
    textArea.disabled = true;
    document.getElementById("guess-submit-button").disabled = true;
    inputToGame(guess);
}

var gamePageTemplate = 
    "        <input id=\"number-guess\" type=\"text\" disabled>\n" +
    "        <button id=\"guess-submit-button\" disabled onclick=\"makeAGuess()\">猜</button>";
