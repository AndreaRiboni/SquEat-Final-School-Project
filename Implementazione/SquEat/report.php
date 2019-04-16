<html>
    <head>
        <title>SquEat - Report</title>
        <?php
            include 'setup.php';
            if(empty($_GET["local"])){
                die('<h1>Non è stato specificato alcun locale.</h1>');
            }
            if(!is_numeric($_GET["local"])){
                die('<h1>ID locale non valido.</h1>');
            }
            if(empty($_SESSION["id"])){
                die('<h1>sessione scaduta.</h1>');
            }
            $conn = connect();
            $res = $conn -> query("select IDAdmin from Locale where IDAdmin = " . $_SESSION["id"]);
            if($res -> num_rows < 1){
                die('<h1>Non rientri tra gli amministratori di questo locale.</h1>');
            }
        ?>
    </head>
    <body>
        <?php
            include 'navbar.php';
        ?>
        <h1>Report in Tempo Reale</h1>
        <?php
            $conn = connect();
            $IDLocale = $_GET["local"];

            $res = $conn -> query("select Utente.ID as id, Utente.Nome as nome, Utente.Cognome as cognome, count(*) as num from Utente, Ordine where Utente.ID = Ordine.IDCliente and Ordine.IDLocale = ".$IDLocale." group by Utente.ID having num >= (select count(*) as x from Ordine where IDCliente = Utente.ID and IDLocale = ".$IDLocale. " order by x desc limit 1) order by num desc limit 1");
            if ($res->num_rows > 0) {
                echo 'Utente che ha ordinato di più';
                $row = $res->fetch_assoc();
                echo "<table><tr><td>Nome</td><td>".$row["nome"]." ".$row["cognome"]."</td></tr>";
                echo "<tr><td>Acquisti</td><td>".$row["num"]."</td></tr>";
            }


            $res = $conn -> query("select * from Ordine, Prodotto where IDLocale = ".$IDLocale." and month(date(Timestamp)) = MONTH(CURRENT_DATE()) and Ordine.IDProdotto = Prodotto.ID");
            if ($res->num_rows > 0) {
                echo '<br>Spesa media giornaliera<br><table>';
                while($row = $res->fetch_assoc()){
                    print_r($row);
                }
            }
        ?>
    </body>
</html>