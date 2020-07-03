var intervalID = -1;
function createRoom() {
    var roomInput = document.getElementById("roomNameInput");
    var gameSelect = document.getElementById("gameList");
    var roomName = roomInput.value;
    var gameId = gameSelect.value;
    var valid = true;
    if (isValidName(roomName)) {
        roomInput.classList.remove("invalid-input");
    } else {
        roomInput.classList.add("invalid-input");
        valid = false;
    }
    if (gameId != "-1") {
        gameSelect.classList.remove("invalid-input");
    } else {
        gameSelect.classList.add("invalid-input");
        valid = false;
    }
    if (valid) {
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", "/createRoom?roomName="+roomName+"&gameId="+gameId, false);
        xmlHttp.send(null);
        var response = xmlHttp.responseText;
        if (response == "OK") {
            $('#RoomCreateModal').modal('hide');
            createRoomLocal(roomName, gameId, true);
            intervalID = window.setInterval(broadcastRoom, 1000);
        } else {
            errorAlert("创建失败", "网络原因，创建失败");
        }
    }
}

function createGuestRoom(gameId, roomName) {
    createRoomLocal(roomName, gameId, false);
}

function broadcastRoom() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/broadcastRoom", false);
    xmlHttp.send(null);
}

function createRoomLocal(roomName, gameId, isOwner) {
    fillRoom(roomName, gameId, isOwner);
    pageSwitch("room-page", "hall-page");
    waitOnTheRoom(gameId);
}

function waitOnTheRoom(gameId) {
    var sse = new EventSource("/waitRoomUpdate");
    sse.onmessage = function (evt) {
        var response = evt.data;
        if (response == "QUIT") {
            sse.close();
        } else if (response == "START") {
            sse.close();
            processGameStart(gameId);
        } else if (response == "KICK") {
            sse.close();
        } else {
            processRoomWait(response);
        }
    };
}

function processRoomWait(response) {
    var arrange = JSON.parse(response);
    var names = arrange["names"];
    var headIds = arrange["headIds"];
    for (var i = 0; i < 8; i++) {
        sit(i+1, names[i], headIds[i]);
    }
}

function closeRoom() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/closeRoom", false);
    xmlHttp.send(null);
    if (intervalID != -1) {
        window.clearInterval(intervalID);
        intervalID = -1;
    }
    pageSwitch("hall-page", "room-page");
    updateRooms();
    unfillRoom();
}

function fillRoom(roomName, gameId, isOwner) {
    if (isOwner) {
        document.getElementById("room-page").innerHTML = roomTemplate;
        document.getElementById("room-footer").innerHTML = startGameButtonTemplate;
    } else {
        document.getElementById("room-page").innerHTML = roomTemplate;
    }
    document.getElementById("header-room-name").innerText = roomName;
    var gameName = games[gameId].name;
    document.getElementById("header-game-name").innerText = gameName;
}

function unfillRoom() {
    document.getElementById("room-page").innerHTML = "";
}

function sit(pos, name, headId) {
    var headImage = document.createElement("img");
    headImage.src = "images/" + headId + "-L.png";
    document.getElementById("roomPlayer" + pos).innerHTML = "";
    document.getElementById("roomPlayer" + pos).appendChild(headImage);
    if (name != "???" && name != "!!!") {
        document.getElementById("roomPlayerName" + pos).innerText = name;
    } else {
        document.getElementById("roomPlayerName" + pos).innerText = "";
    }
}

var roomTemplate = 
    "<header class=\"fixed-top header\">\n" +
    "            <div class=\"container-fluid\">\n" +
    "                <div class=\"row\">\n" +
    "                    <div class=\"col-md-4\">\n" +
    "                        <div class=\"main-header\">\n" +
    "                            <h3 id=\"header-room-name\"></h3>\n" +
    "                            <h3 id=\"header-game-name\"></h3>\n" +
    "                        </div>\n" +
    "                    </div>\n" +
    "                    <div class=\"col-md-1 ml-auto\">\n" +
    "                        <button type=\"button\" class=\"close room-main-close\" onclick=\"closeRoom()\">\n" +
    "                            <span>&times;</span>\n" +
    "                        </button>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </header>\n" +
    "        <div class=\"room-main-page\">\n" +
    "            <table class=\"room-player-table\">\n" +
    "                <tr>\n" +
    "                    <td>\n" +
    "                        <div id=\"roomPlayer1\"></div>\n" +
    "                        <p id=\"roomPlayerName1\"></p>\n" +
    "                    </td>\n" +
    "                    <td>\n" +
    "                        <div id=\"roomPlayer2\"></div>\n" +
    "                        <p id=\"roomPlayerName2\"></p>\n" +
    "                    </td>\n" +
    "                    <td>\n" +
    "                        <div id=\"roomPlayer3\"></div>\n" +
    "                        <p id=\"roomPlayerName3\"></p>\n" +
    "                    </td>\n" +
    "                    <td>\n" +
    "                        <div id=\"roomPlayer4\"></div>\n" +
    "                        <p id=\"roomPlayerName4\">人</p>\n" +
    "                    </td>\n" +
    "                </tr>\n" +
    "                <tr>\n" +
    "                    <td>\n" +
    "                        <div id=\"roomPlayer5\"></div>\n" +
    "                        <p id=\"roomPlayerName5\"></p>\n" +
    "                    </td>\n" +
    "                    <td>\n" +
    "                        <div id=\"roomPlayer6\"></div>\n" +
    "                        <p id=\"roomPlayerName6\"></p>\n" +
    "                    </td>\n" +
    "                    <td>\n" +
    "                        <div id=\"roomPlayer7\"></div>\n" +
    "                        <p id=\"roomPlayerName7\"></p>\n" +
    "                    </td>\n" +
    "                    <td>\n" +
    "                        <div id=\"roomPlayer8\"></div>\n" +
    "                        <p id=\"roomPlayerName8\"></p>\n" +
    "                    </td>\n" +
    "                </tr>" +
    "            </table>\n" +
    "        </div>\n" +
    "        <footer id=\"room-footer\" class=\"fixed-bottom footer\">\n" +
    "        </footer>";

    var startGameButtonTemplate = 
    "            <div class=\"container\">\n" +
    "                <div class=\"row\">\n" +
    "                    <div class=\"col-md-2 ml-auto\">\n" +
    "                        <button type=\"button\" class=\"btn btn-success\" onclick=\"gameStart()\">\n" +
    "                            开始游戏\n" +
    "                        </button>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "            </div>\n";
