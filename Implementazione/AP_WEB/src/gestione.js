/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function checkForm() {
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
        okay = elements[i].value.length < 2 && okay;
        if (elements[i].value.length < 2) {
            elements[i].style.border = "2px solid red";
        } else {
            elements[i].style.border = "2px solid black";
        }
    }
    document.getElementById("submittt").disabled = !okay;
}
