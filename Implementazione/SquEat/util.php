<?php
    include 'setup.php';

    if(!empty($_GET["idlocale"])){
        $_SESSION["IDLocale"] = $_GET["idlocale"];
    }
    
    if(!empty($_GET["idordine"]) && !empty($_GET["status"])){
        $conn = connect();
        $conn -> query("update Ordine set stato = ".$_GET["status"]." where IDOrdine = ".$_GET["idordine"]);
    }

    if(!empty($_GET["idlocale"]) && !empty($_GET["idprodotto"]) && !empty($_GET["costo"])){
        if(!empty($_COOKIE["carrello"]))
            setcookie("carrello", $_COOKIE["carrello"] . $_GET["idlocale"] . "-" . $_GET["idprodotto"] . "-" . $_GET["costo"] . "-", time() + 3600 * 3);
        else 
            setcookie("carrello", $_GET["idlocale"] . "-" . $_GET["idprodotto"] . "-" . $_GET["costo"] . "-", time() + 3600 * 3);
    }

    if(!empty($_GET["buy"]) && !empty($_COOKIE["carrello"])){
        $IDCliente = trim($_SESSION["id"]);
        $carrello = &$_COOKIE["carrello"];
        $indirizzo = &$_SESSION["indirizzo"];

        echo "11;" . $IDCliente . ";" . $carrello . ";" . $indirizzo;


        $res = request("11;" . $IDCliente . ";" . $carrello . ";" . $indirizzo);

        echo "<br>" . $res;

        setcookie("carrello", "", 0);
    }

    if (!empty($_GET["SvuotaCarrello"])) {
        setcookie("carrello", "", 0);
    }

    if(!empty($_GET["review"]) && !empty($_GET["star"]) && !empty($_GET["idordine"])){
        $conn = connect();
        $res = $conn->query("select IDLocale from Ordine where IDOrdine = " . $_GET["idordine"]);
        $row = $res -> fetch_assoc();
        $star = &$_GET["star"];
        $conn -> query("update Locale set NumRecensioni = NumRecensioni + 1, Punteggio = Punteggio + $star where ID = ".$row["IDLocale"]);
    }

?>