<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>SquEat - Gestore</title>
        <?php
        include 'setup.php';

        session_start();
        $_SESSION["IDLocale"] = $_GET["local"];

        if (empty($_SESSION["id"])) {
            die('<h1>sessione scaduta.</h1>');
        }

        $conn = connect();
        if (empty($_GET["local"])) {
            $res = $conn->query("select ID from Locale where IDAdmin = " . $_SESSION["id"]);
            if ($res->num_rows > 0) { //un solo locale a gestore
                $row = $res->fetch_assoc();
                $_SESSION["IDLocale"] = $row["ID"];
            } else {
                include 'navbar.php';
                die('<h1 class="text-center">non ti occupi di alcun locale</h1>');
            }
        }

        $res = $conn->query("select IDAdmin from Locale where IDAdmin = " . $_SESSION["id"] . " and ID = " . $_SESSION["IDLocale"]);
        if ($res->num_rows < 1) {
            die('<h1>Non rientri tra gli amministratori di questo locale.</h1>');
        }
        ?>
    </head>
    <body>
        <?php

        function getFattorino($id) {
            $conn2 = connect();
            $res2 = $conn2->query("select Nome, Cognome from Utente, Fattorini where IDCliente = ID and ID = " . $id . " and IDLocale = " . $_SESSION["IDLocale"]);
            $row2 = $res2->fetch_row();
            return $row2[0] . " " . $row2[1];
        }

        if (isset($_GET["RISTORANTETABELLA"])) {
            $res = $conn->query("select Ordine.IDOrdine, Utente.Nome, Utente.Cognome, Prodotto.Nome, Timestamp, Costo, IDFattorino, Ordine.Indirizzo from Ordine, Utente, Prodotto where IDLocale = " . $_SESSION["IDLocale"] . " and stato = 0 and IDCliente = Utente.ID and Prodotto.ID = Ordine.IDProdotto order by IDOrdine DESC;");
            echo "<div class='table-responsive'><table class='table' style='margin: 20px auto'><tr>";
            echo "<th><input type='checkbox' onclick='selectAll()'></th>";
            echo "<th>ID</th>";
            echo "<th>Nome</th>";
            echo "<th>Prodotto</th>";
            echo "<th>Data</th>";
            echo "<th>Costo</th>";
            echo "<th>Fattorino</th>";
            echo "<th>Indirizzo</th></tr>";
            if ($res->num_rows > 0) {
                // output data of each row
                while ($row = $res->fetch_row()) {
                    echo "<tr>";
                    echo "<td> <input type='checkbox' value = '$row[0]' name = 'OrderCheckb'> </td>";

                    echo "<td>" . $row[0] . "</td>";

                    echo "<td>" . $row[1] . " " . $row[2] . "</td>";

                    echo "<td>" . $row[3] . "</td>";

                    echo "<td>" . $row[4] . "</td>";

                    echo "<td>" .money_format("%i", $row[5]). " €</td>";

                    echo "<td>Rino Fatto</td>";

                    echo "<td>" . $row[7] . "</td>";
                    echo "</tr>";
                }
                echo '</table></div>';
            } else {
                echo "<tr><td colspan='8'><div class='text-center'>Nessun ordine in sospeso</div></td></tr></table></div>";
            }
        } else {

            include 'navbar.php';
            $conn = connect();
            $res = $conn->query("select Nome from Locale where ID = " . $_SESSION["IDLocale"]);
            $row = $res->fetch_assoc();
            $NomeLocale = $row["Nome"];

            $res = $conn->query("select NumRecensioni, Punteggio from Locale where ID = " . $_SESSION["IDLocale"]);
            $row = $res->fetch_assoc();
            $NumRece = $row["NumRecensioni"];

            $Points = $row["Punteggio"] / $row["NumRecensioni"];

            $res = $conn->query("select count(*) as c from Ordine where IDLocale = " . $_SESSION["IDLocale"]);
            $row = $res->fetch_assoc();
            $NumOrd = $row["c"];
            ?>
            <?php
                $res = $conn->query("select ID, Nome from Locale where IDAdmin = " . $_SESSION["id"]);
                echo "<select onchange='changeSession()' id='selezionatoreristoranti'  class='selectpicker show-menu-arrow form-control'>";
                while ($row = $res->fetch_assoc()) {
                    echo "<option value='".$row["ID"]."'>".$row["Nome"]."</option>";
                }
                echo "</select>";
            ?>
            <div class="container text-center">
            <h1><?php echo $NomeLocale; ?></h1>
            <div class='table-responsive'>
                <table class='table' style='margin: 20px auto'>
                    <tr>
                        <th>Numero Recensioni</th>
                        <th>Punteggio</th>
                        <th>Numero Ordini</th>
                    </tr>
                    <tr>
                        <td><?php echo $NumRece; ?></td>
                        <td>
                        <?php 
                            for($i = 0; $i < $Points; $i++){
                                echo '★';
                            }
                            echo '&nbsp<sub>('.money_format("%i", $Points).')</sub>';
                        ?>
                        </td>
                        <td><?php echo $NumOrd; ?></td>
                    </tr>
                </table>
            </div>
            <h1>Ordini</h1>
            </div>
            
            <div class = "container text-center">
                <div class="btn-group btn-group-inline" role="group">
                    <button class="btn btn-danger" onclick="setStatus(2)">completa</button>
                    <button class="btn btn-danger" onclick="setStatus(1)">annulla</button>
                </div>
                <br>
                <input type="checkbox" onclick="setRTUpdate()" id="RTUpdate">aggiornamento in tempo reale
            </div>
        </div>
        <div class = "container text-center" id="listaordini"></div>
        
        <hr>
        <div class = "container text-center">
            <h1>Ordini effettuati</h1>
        <?php
            $conn = connect();
            $res = $conn->query("select Ordine.IDOrdine, Utente.Nome, Utente.Cognome, Prodotto.Nome, Timestamp, Costo, IDFattorino, Ordine.Indirizzo from Ordine, Utente, Prodotto where IDLocale = " . $_SESSION["IDLocale"] . " and stato != 0 and IDCliente = Utente.ID and Prodotto.ID = Ordine.IDProdotto order by IDOrdine DESC;");
            echo "<div class='table-responsive'><table class='table' style='margin: 20px auto'><tr>";
            echo "<th>ID</th>";
            echo "<th>Nome</th>";
            echo "<th>Prodotto</th>";
            echo "<th>Data</th>";
            echo "<th>Costo</th>";
            echo "<th>Fattorino</th>";
            echo "<th>Indirizzo</th></tr>";
            if ($res->num_rows > 0) {
                // output data of each row
                while ($row = $res->fetch_row()) {
                    echo "<tr>";

                    echo "<td>" . $row[0] . "</td>";

                    echo "<td>" . $row[1] . " " . $row[2] . "</td>";

                    echo "<td>" . $row[3] . "</td>";

                    echo "<td>" . $row[4] . "</td>";

                    echo "<td>" .money_format("%i", $row[5]). " €</td>";

                    echo "<td>Rino Fatto</td>";

                    echo "<td>" . $row[7] . "</td>";
                    echo "</tr>";
                }
                echo '</table></div>';
            } else echo "<tr><td colspan='7'>nessun ordine</td></tr></table>";
        ?>
        </div>

        <script>
            var RTUpdate = true;

            document.getElementById("RTUpdate").checked = true;

            function setRTUpdate(){
                RTUpdate = !RTUpdate;
            }

            function selectAll(){
                let checks = document.getElementsByName("OrderCheckb");
                for(let i = 0; i < checks.length; i++){
                    checks[i].checked = !checks[i].checked;
                }
            }

            function loadDoc() {
                if(RTUpdate){
                    console.log("updated");
                    var xhttp = new XMLHttpRequest();
                    xhttp.onreadystatechange = function () {
                        if (this.readyState == 4 && this.status == 200) {
                            var scroll = document.body.scrollTop;
                            document.getElementById("listaordini").innerHTML = this.responseText;
                            document.body.scrollTop = scroll;
                        }
                    };
                    xhttp.open("GET", "locale.php?RISTORANTETABELLA=show&local=<?php echo $_SESSION["IDLocale"];
                    ?>", true);
                    xhttp.send();
                }
            }
                loadDoc();
                window.setInterval(loadDoc, 3000);

                function changeSession() {
                    var e = document.getElementById("selezionatoreristoranti");
                    var idlocale = e.options[e.selectedIndex].value;
                    var xhttp = new XMLHttpRequest();
                    xhttp.open("GET", "util.php?idlocale="+idlocale, true);
                    xhttp.send();
                    window.location.href = "locale.php?local="+idlocale;
                }

                function setStatus(status){
                    let checks = document.getElementsByName("OrderCheckb");
                    for(let i = 0; i < checks.length; i++){
                        if(checks[i].checked)
                            asyncStatus(status, checks[i].value);
                    }
                    RTUpdate = true;
                    loadDoc();
                    document.getElementById("RTUpdate").checked = true;
                }

                function asyncStatus(status, id){
                    console.log("util.php?idordine="+id+"&status="+status);
                    var xhttp = new XMLHttpRequest();
                    xhttp.open("GET", "util.php?idordine="+id+"&status="+status, true);
                    xhttp.send();
                }
            </script>

    <?php } ?>
</body>
</html>