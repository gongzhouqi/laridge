
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
    document.addEventListener('mousemove', draw);
    // document.addEventListener('mousedown', startDraw);
    document.addEventListener('mousedown', setPosition);
    document.addEventListener('mouseenter', setPosition);
    // document.addEventListener('mouseup', endDraw);
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
        start : start,
        end : end
    }
    arr.push(line);
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
