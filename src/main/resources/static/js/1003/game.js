
var container;

var game = {
    pageRoot : null,
    startGame : function(root) {
        this.pageRoot = root;
        container = document.createElement("div");
        container.style.overflow = "auto";
        container.style.maxHeight = "100%";
        root.appendChild(container);
    },
    process : function(response) {
        processGameWait(response);
    }
}


function processGameWait(response) {
    if (response == "QUIT") {
        alert("100个数全部展示完毕！");
        endGame();
    } else {
        var h = document.createElement("h6");
        h.innerText = response;
        container.appendChild(h);
        container.scrollTop = container.scrollHeight;
    }
}