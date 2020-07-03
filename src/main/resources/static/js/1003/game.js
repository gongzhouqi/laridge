
var game = {
    pageRoot : null,
    startGame : function(root) {
        this.pageRoot = root;
    },
    processGameWait : function(response) {
        var h = document.createElement("h6");
        h.innerText = response;
        this.pageRoot.appendChild(h);
    },
}