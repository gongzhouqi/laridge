
// Initiate
function init() {
    getInfo();
    getGames();
    $('#ChangeNameModal').on('show.bs.modal', function () {
        var nameInput = document.getElementById("uNameInput");
        nameInput.value = myName;
        nameInput.classList.remove("invalid-input");
    });
    $('#RoomCreateModal').on('show.bs.modal', function () {
        var roomInput = document.getElementById("roomNameInput");
        roomInput.value = "";
        roomInput.classList.remove("invalid-input");
        var gameSelect = document.getElementById("gameList");
        gameSelect.selectedIndex = "0";
        gameSelect.classList.remove("invalid-input");
    });
    updateRooms();
    window.setInterval(updateRooms, 60000);
}

var myName;
var myHeadId;
function getInfo() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/init", false);
    xmlHttp.send(null);
    var response = xmlHttp.responseText.split("\n");
    myName = response[0];
    myHeadId = response[1];
    changeNameLocal();
    changeHeadLocal();
}

var games;
function getGames() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/gameTypes", false);
    xmlHttp.send(null);
    var gameJson = xmlHttp.responseText;
    games = JSON.parse(gameJson);
    gameSelectsLocal();
}

function gameSelectsLocal() {
    var slt = document.getElementById("gameList");
    Object.values(games).forEach(g => {
        var opt = document.createElement("option");
        opt.value = g.id;
        opt.innerText = g.name;
        slt.appendChild(opt);
    });
}

function updateRooms() {
    var rooms = getRooms();
    if (rooms.startsWith("OK")) {
        rooms = rooms.substring(2);
    } else {
        return;
    }
    updateRoomsLocal(JSON.parse(rooms));
}

function updateRoomsLocal(currentRooms) {
    var tb = document.getElementById("roomListPlaceHolder");
    tb.innerHTML = "";
    currentRooms.forEach(room => {
        var row = document.createElement("tr");
        row.classList.add("table-light");

        var c1 = document.createElement("td");
        c1.innerText = room.roomName;

        var c2 = document.createElement("td");
        var gameId = room.gameId;
        c2.innerText = gameId;

        var c3 = document.createElement("td");
        var currUserNumber = room.userNumber;
        c3.innerText = currUserNumber + "/" + games[gameId].max;

        var c4 = document.createElement("td");
        c4.innerText = room.userName;

        var c5 = document.createElement("td");
        var joinButton = document.createElement("button");
        joinButton.classList.add("btn", "btn-outline-success", "btn-sm");
        joinButton.onclick = function() {
            tryJoinRoom(room.roomId, room.subIP, room.port, room.gameId, room.roomName);
        }
        var buttonIcon = document.createElement("i");
        buttonIcon.classList.add("fa", "fa-plus");
        joinButton.appendChild(buttonIcon);
        c5.appendChild(joinButton);
        
        row.appendChild(c1);
        row.appendChild(c2);
        row.appendChild(c3);
        row.appendChild(c4);
        row.appendChild(c5);

        tb.appendChild(row);
    });
}

function getRooms() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/getRooms", false);
    xmlHttp.send(null);
    return xmlHttp.responseText;
}

function changeName() {
    var nameInput = document.getElementById("uNameInput");
    var username = nameInput.value;
    if (isValidName(username)) {
        nameInput.classList.remove("invalid-input");
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", "/changeName?name="+username, false);
        xmlHttp.send(null);
        var response = xmlHttp.responseText;
        if (response == "OK") {
            $('#ChangeNameModal').modal('hide');
            myName = username;
            changeNameLocal(username);
        } else {
            errorAlert("昵称更改失败", "文件格式原因，更改失败。");
        }
    } else {
        nameInput.classList.add("invalid-input");
    }
}

function isValidName(username) {
    return username.match("^[A-Za-z0-9\u4e00-\u9fa5]+$");
}

function changeNameLocal() {
    document.getElementById("uName").innerHTML = myName;
}

function changeHeadLocal() {
    document.getElementById("headImage").src = "images/" + myHeadId + "-S.png";
}

function tryJoinRoom(roomId, ip, port, gameId, roomName) {
    showLoading();
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/guestRoom?roomId="+roomId+"&ip="+ip+"&port="+port, true);
    xmlHttp.onload = function() {
        hideLoading();
        processRoomAccess(xmlHttp.responseText, gameId, roomName);
    }
    xmlHttp.send(null);
}

function processRoomAccess(response, gameId, roomName) {
    if (response == "") {
        errorAlert("加入失败", "房间无响应。请重试。");
    } else if (response == "NO") {
        errorAlert("加入失败", "房间已满。");
    } else if (response == "OK") {
        createGuestRoom(gameId, roomName);
    }
}

function errorAlert(title, content) {
    document.getElementById("ErrorLogTitle").innerText = title;
    document.getElementById("ErrorLogContent").innerText = content;
    $('#ErrorLog').modal();
}
