function gameStart() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/gameStart", false);
    xmlHttp.send(null);
}

function processGameStart() {
    closeRoom();
    pageSwitch("game-page", "hall-page");
    showLoading();
    loadGamePageLocal();
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/gameReady", false);
    xmlHttp.send(null);
    if (xmlHttp.responseText == "OK") {
        console.log("OK!!!!!!!!!!!!");
        waitOnTheGame();
    }
}

function waitOnTheGame() {
    var sse = new EventSource("/waitGameUpdate");
    sse.onmessage = function (evt) {
        var response = evt.data;
        if (response == "QUIT") {
            sse.close();
        } else if (response == "GO") {
            hideLoading();
        } else {
            processGameWait(response);
        }
    };
}

function loadGamePageLocal() {
    // TODO:
}

function processGameWait(response) {
    // TODO:
    var e = document.getElementById("game-page");
    var h = document.createElement("h6");
    h.innerText = response;
    e.appendChild(h);
}
