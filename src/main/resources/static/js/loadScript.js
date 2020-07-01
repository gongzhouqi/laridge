
function showLoading() {
    var root = document.getElementById("load-page");
    addLoader();
    root.style.display = "block";
}

function addLoader() {
    var root = document.getElementById("load-page");
    var container = document.createElement("div");
    container.classList.add("load-container");
    
    var spinner = document.createElement("div");
    spinner.classList.add("spinner-border");
    spinner.style.width = "50px";
    spinner.style.height = "50px";
    root.appendChild(container)
    container.appendChild(spinner);
}

function hideLoading() {
    var root = document.getElementById("load-page");
    document.getElementById("load-page").innerHTML = "";
    root.style.display = "none";
}