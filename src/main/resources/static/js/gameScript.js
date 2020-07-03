function gameStart() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/gameStart", false);
    xmlHttp.send(null);
}

function processGameStart(gameId) {
    closeRoom();
    pageSwitch("game-page", "hall-page");
    showLoading();
    loadGamePageLocal(gameId);
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/gameReady", false);
    xmlHttp.send(null);
    if (xmlHttp.responseText == "OK") {
        console.log("OK!!!!!!!!!!!!");
        waitOnTheGame();
    }
}

var gameInstance;

function waitOnTheGame() {
    var sse = new EventSource("/waitGameUpdate");
    sse.onmessage = function (evt) {
        var response = evt.data;
        if (response == "QUIT") {
            sse.close();
        } else if (response == "GO") {
            hideLoading();
        } else {
            gameInstance.process(response);
        }
    };
}

function loadGamePageLocal(gameId) {
    dynamicallyLoadScript(gameId);
}

function dynamicallyLoadScript(gameId) {
    var s = document.createElement("script");
    var url = "../js/" + gameId + "/game.js";
    s.src = url;
    document.head.appendChild(s);
    s.addEventListener('load', () => {
        gameInstance = game;
        gameInstance.startGame(document.getElementById("game-page"));
    });
}
