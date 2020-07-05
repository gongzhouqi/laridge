
var game = {
    pageRoot : null,
    startGame : function(root) {
        this.pageRoot = root;
        var outer = document.createElement("div");
        outer.classList.add("game-page-bg");
        root.appendChild(outer);

        var container = document.createElement("div");
        container.id = "canvas-container";
        container.classList.add("canvas-container")
        outer.appendChild(container);

        init();
    },
    process : function(response) {
        processGameWait(response);
    }
}

var offLeft;
var offTop;
var pos = { x: 0, y: 0 };
var ctx;
var arr = [];
var counter = 0;

// var buffer;
// var pointer = 0;
// var history = [];

function init() {
    var canvas = document.createElement('canvas');
    canvas.id = "draw-canvas";
    var container = document.getElementById("canvas-container");
    container.appendChild(canvas);
    
    calculateOffset();

    ctx = canvas.getContext('2d');
    ctx.canvas.width = 900;
    ctx.canvas.height = 600;
    ctx.lineWidth = 5;
    ctx.lineCap = 'round';
    ctx.strokeStyle = '#c0392b';

    window.addEventListener('resize', calculateOffset);
}

function enableDraw() {
    clearAll();
    arr = [];
    counter = 0;
    document.addEventListener('mousemove', draw);
    // document.addEventListener('mousedown', startDraw);
    document.addEventListener('mousedown', setPosition);
    document.addEventListener('mouseenter', setPosition);
    // document.addEventListener('mouseup', endDraw);
}

function disableDraw() {
    clearAll();
    arr = [];
    counter = 0;
    document.removeEventListener('mousemove', draw);
    // document.removeEventListener('mousedown', startDraw);
    document.removeEventListener('mousedown', setPosition);
    document.removeEventListener('mouseenter', setPosition);
    // document.removeEventListener('mouseup', endDraw);
}

function calculateOffset() {
    offTop = $('#draw-canvas').offset().top;
    offLeft = $('#draw-canvas').offset().left;
}

// These functions are potentially useful if I add undo redo functionality

// function startDraw() {
//     refreshBuffer();
//     setPosition();
// }

// function refreshBuffer() {
//     buffer = {
//         pen: "red",
        
//     }
// }

function setPosition(e) {
    pos.x = e.clientX - offLeft;
    pos.y = e.clientY - offTop;
}

function draw(e) {
    if (e.buttons !== 1) return;

    ctx.beginPath();

    var start = {
        x : pos.x,
        y : pos.y
    };

    ctx.moveTo(pos.x, pos.y);
    setPosition(e);
    ctx.lineTo(pos.x, pos.y);

    var end = {
        x : pos.x,
        y : pos.y
    };

    ctx.stroke();

    var line = {
        order : counter,
        start : start,
        end : end
    }
    counter++;
    arr.push(line);
    var lineStr = JSON.stringify(line);
    lineStr = lineStr.replace(/\{/g, "%7B").replace(/\}/g, "%7D");
    inputToGame(
        "DRAW"+
        lineStr);
}

function clearAll() {
    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
}

function redraw() {
    arr.forEach(l => {
        ctx.beginPath();
        ctx.moveTo(l.start.x, l.start.y);
        ctx.lineTo(l.end.x, l.end.y);
        ctx.stroke();
    });
}

function processGameWait(response) {
    if (response.startsWith("ENABLE")) {
        enableDraw();
    } else if (response.startsWith("DISABLE")) {
        disableDraw();
    } else
     if (response.startsWith("DRAW"))
      {
        var newLine = JSON.parse(response.substring(4));
        arr[newLine.order] = newLine;
        while (arr[counter] != null) {
            var toDraw = arr[counter];
            ctx.beginPath();
            ctx.moveTo(toDraw.start.x, toDraw.start.y);
            ctx.lineTo(toDraw.end.x, toDraw.end.y);
            ctx.stroke();
            counter++;
        }
    }
}

function inputToGame(input) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "/gameInput?input="+input, true);
    xmlHttp.send(null);
}
