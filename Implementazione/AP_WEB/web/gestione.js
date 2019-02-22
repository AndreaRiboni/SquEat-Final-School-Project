/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function checkFormRegister() {
    var okay = true;
    var elements = [];
    elements.push(document.getElementsByName("nome")[0]);
    elements.push(document.getElementsByName("cognome")[0]);
    elements.push(document.getElementsByName("mail")[0]);
    elements.push(document.getElementsByName("password")[0]);
    elements.push(document.getElementsByName("cell")[0]);
    elements.push(document.getElementsByName("indirizzo")[0]);

//    elements.push(document.getElementsByName("tipocliente")[0]);
    for (var i = 0; i < elements.length; i++) {

        okay = elements[i].value.length > 2 && okay;
        if (elements[i].value.length < 2) {
            elements[i].style.border = "2px solid red";
        } else {
            elements[i].style.border = "2px solid black";
        }
        console.log(okay);
    }
    document.getElementById("sign").disabled = !okay;
}

function checkFormLogin() {
    var okay = true;
    var elements = [];
    elements.push(document.getElementsByName("user")[0]);
    elements.push(document.getElementsByName("psw")[0]);
    for (var i = 0; i < elements.length; i++) {
        okay = elements[i].value.length > 2 && okay;
        if (elements[i].value.length < 2) {
            elements[i].style.border = "2px solid red";
        } else {
            elements[i].style.border = "2px solid black";
        }
    }
    document.getElementById("log").disabled = !okay;
}

function getFormLogin() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("demo").innerHTML = this.responseText;
        }
    };
    xhttp.open("POST", "ajax_info.txt", true);
    xhttp.send();
}

function getFormRegister() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("demo").innerHTML = this.responseText;
        }
    };
    xhttp.open("POST", "ajax_info.txt", true);
    xhttp.send();
}
