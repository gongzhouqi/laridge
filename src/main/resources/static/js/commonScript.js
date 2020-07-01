function pageSwitch(toShow, toHide) {
    document.getElementById(toShow).style.display = "block";
    document.getElementById(toHide).style.display = "none";
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}
