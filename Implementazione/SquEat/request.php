<html>
    <head>
        <title>
            Richieste Utenti
        </title>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    </head>
    <body>
        <?php
            include 'setup.php';
            include 'navbar.php';

            echo "<div class='text-center'><h1>Richieste Utenti</h1></div>";

            if(!empty($_SESSION['id'])){

                $conn = connect();
                $res = $conn->query("select Privilegio from Utente where ID = ".$_SESSION["id"]);
                $row = $res -> fetch_assoc();
                
                if(!empty($_POST["testorichiesta"])){
                    $text = htmlspecialchars($_POST["testorichiesta"]);
                    $idutente = &$_SESSION["id"];
                    $esito = $conn -> query("insert into Richieste (richiesta, IDUtente) values ('$text', $idutente)");
                    if($esito) {
                        echo '<div class="alert alert-success fixed-top" role="alert">
                                <b>Richiesta Effettuata</b>: riceverai risposta nelle prossime ore
                            </div>'; 
                    } else {
                        echo '<div class="alert alert-danger fixed-top" role="alert">
                                <b>Errore</b>: non è stato possibile inviare la tua richiesta
                            </div>';
                    }
                    unset($_POST);
                }
                if(!empty($_POST["IDRichiesta"]) && !empty($_POST["testorisposta"])){
                    $text = htmlspecialchars($_POST["testorisposta"]);
                    $req = explode("_", $_POST["IDRichiesta"]);
                    $richiesta = &$req[0];
                    $idrichiedente = &$req[1];
                    $esito = $conn -> query("insert into Risposte (IDRichiesta, risposta, IDUtente) values ($richiesta, '$text', $idrichiedente)");
                    if($esito) {
                        echo '<div class="alert alert-success fixed-top" role="alert">
                                <b>Risposta Inviata</b>
                            </div>'; 
                            $conn -> query("update Richieste set stato = 1 where ID = $richiesta");
                    } else {
                        echo '<div class="alert alert-danger fixed-top" role="alert">
                                <b>Risposta già inviata</b>
                            </div>';
                    }
                }


                if($row["Privilegio"] == 0){ //admin
                    
                    $res = $conn -> query("select Richieste.ID, Utente.Nome, Utente.Cognome, Richieste.data, Richieste.richiesta, Utente.Mail, Utente.ID from Richieste, Utente where Richieste.IDUtente = Utente.ID and Stato = 0");
                    if($res -> num_rows > 0){
                        echo "
                        <form method='POST' action='request.php'><div class='table-responsive'><table class='table' style='margin: 20px auto'><tr>";
                        echo "<th>&nbsp&nbsp</th>";
                        echo "<th>Utente</th>";
                        echo "<th>Data</th>";
                        echo "<th>Richiesta</th></tr>";
                        while($row = $res -> fetch_row()){
                            echo "<tr>";
                            echo "<td> <input type='radio' value = '$row[0]_$row[6]' name = 'IDRichiesta'> </td>";

                            echo "<td>" . $row[1] . " " . $row[2] . " (". $row[5] . ")</td>";

                            echo "<td>" . $row[3] . "</td>";

                            echo "<td>" . $row[4] . "</td>";

                            echo "</tr>"; 
                        }
                        echo "</table>";
                        ?>
                            <div class="form-group container">
                                <label>Risposta</label>
                                <textarea class="form-control" placeholder="Risposta" name="testorisposta" style="max-width: 75%"></textarea>
                                <button type="submit" class="btn btn-danger">Invia</button>
                            </div>
                        </form>
                    <?php 

                    } else echo "<tr><td colspan='4'>nessun risultato</td></tr></table>";
                } else { //utente
                    ?>
                    <form method="POST" action="request.php">
                        <div class="form-group container">
                            <label>Richiesta</label>
                            <textarea class="form-control" placeholder="Richiesta" name="testorichiesta" style="max-width: 75%"></textarea>
                            <button type="submit" class="btn btn-danger">Invia</button>
                        </div>
                    </form>
                    <?php
                    
                    $idutente = &$_SESSION["id"];
                    $res = $conn->query("select richiesta, risposta, Risposte.stato from Risposte, Richieste where IDRichiesta in (select ID from Richieste where IDUtente = $idutente) and Richieste.ID = Risposte.IDRichiesta order by IDRichiesta desc");
                    echo "<div class='table-responsive'><table class='table' style='margin: 20px auto'><tr>";
                        echo "<th>Richiesta</th>";
                        echo "<th>Risposta</th>";
                        if($res->num_rows > 0){
                            while($row = $res -> fetch_assoc()){
                                if($row["stato"] == 0) echo "<tr style = 'background-color: #bb5555; color: white'>";
                                else echo "<tr>";

                                echo "<td>" . $row["richiesta"] . "</td>";

                                echo "<td>" . $row["risposta"] . "</td>";

                                echo "</tr>"; 
                            }
                        } else echo "<tr><td colspan='2'>Non hai ancora ricevuto risposte</td></tr>";
                        $conn->query("update Risposte set stato = 1 where IDUtente = $idutente");
                        echo "</table>";
                }
                
            } else die("<h1 class='text-center'>devi eseguire il login</h1>");
        ?>
    </body>
</html>