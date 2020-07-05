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
        waitOnTheGame();
    }
}

var gameInstance;
var sse = null;

function waitOnTheGame() {
    sse = new EventSource("/waitGameUpdate");
    sse.onmessage = function (evt) {
        var response = evt.data;
        if (response == "GO") {
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
    var st = document.createElement("link");
    st.rel = "stylesheet";
    var styleUrl = "../js/" + gameId + "/game.css";
    st.href = styleUrl;
    st.id = "game-entity-css";
    document.head.appendChild(st);
    
    var sc = document.createElement("script");
    var scriptUrl = "../js/" + gameId + "/game.js";
    sc.src = scriptUrl;
    sc.id = "game-entity-script";
    document.head.appendChild(sc);

    sc.addEventListener('load', () => {
        gameInstance = game;
        gameInstance.startGame(document.getElementById("game-page"));
    });
}

function endGame() {
    sse.close();
    sse = null;
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/endGame", false);
    xmlHttp.send(null);
    document.getElementById("game-page").innerHTML = "";
    document.getElementById("game-entity-script").remove();
    pageSwitch("hall-page", "game-page");
}


