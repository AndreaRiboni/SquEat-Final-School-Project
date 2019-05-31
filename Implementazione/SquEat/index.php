<html>
    <head>
        <?php
        include 'setup.php';
        ?>
        
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    </head>

    <body>
        <?php
        include 'navbar.php';
        ?>
        <div class="container center_div text-center">
            <p class="h1">Ordina con SquEat</p>
                    <form action="listrist.php" method="get" style="margin: 0 auto">
                    <div class="input-group">
                       <input type="text" name="address" id="address" placeholder="Indirizzo di consegna" class="form-control input-lg">
                        <span class="input-group-btn">
                        <input class="btn btn-danger btn-lg" type="submit">
                        </span>
                        </div>
                    </form>
        </div>
    </body>
</html>


<!-- 
    ESEMPIO DI COMUNICAZIONE CON IL SERVER DI PHP 
        print_r(request("0;admin@admin.it;admin"));
-->

<?php
//LOGIN
if (
        !empty($_POST["loginemail"]) &&
        !empty($_POST["loginpsw"])
) {
    $response = request("0;" . $_POST["loginemail"] . ";" . $_POST["loginpsw"]);
    if ($response[1] == "true") {
        $_SESSION["id"] = $response[2];
        svuota();
    } else {
        ?>
        <div class="alert alert-danger fixed-top" role="alert">
            <b>Login Errato</b>: i dati inseriti non sono corretti
        </div>
        <?php
    }
    
}

//REGISTRAZIONE
if (
        !empty($_POST["registername"]) &&
        !empty($_POST["registersurname"]) &&
        !empty($_POST["registerphone"]) &&
        !empty($_POST["registeraddress"]) &&
        !empty($_POST["registeremail"]) &&
        !empty($_POST["registerpassword"])
) {
    $response = implode(";", request("2;" . $_POST["registeremail"] . ";" . $_POST["registerpassword"] . ";" . $_POST["registername"] . ";" . $_POST["registersurname"] . ";" . $_POST["registerphone"] . ";" . $_POST["registeraddress"] . ";1"));

    if (str_contains($response, "false")) {
        ?>
        <div class="alert alert-danger fixed-top" role="alert">
            <b>Registrazione Errata</b>: i dati inseriti non sono validi
        </div>
        <?php
        echo $response;
    } else {
        echo '<div class="alert alert-success fixed-top" role="alert">
        <b>Registrazione Avvenuta</b>: esegui il login per usare squeat
    </div>';
       
    }
    
}

//LOGOUT
if (
        !empty($_GET["action"]) &&
        $_GET["action"] == "logout"
) {
    session_destroy();
    setcookie('carrello', "", 0);
    svuota();
}

function svuota() {
    unset($_POST["registername"]);
    unset($_POST["loginemail"]);
    header('Location: index.php');
    echo '<script>window.location.href = "index.php";</script>';
    die();
}

?>