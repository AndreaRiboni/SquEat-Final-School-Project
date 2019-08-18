<?php
    include 'setup.php';
?>
<head>
<?php
    include 'navbar.php';
?>
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
</head>

<body>

<?php
    $conn = connect();
    $orders = $conn->query(
        "select Costo, Prodotto.Nome as Prodotto, Locale.Nome as Locale, Ordine.IDOrdine as IDOrdine from Ordine, Prodotto, Locale where IDCliente = ".$_SESSION['id']." and Prodotto.ID = Ordine.IDProdotto and Ordine.IDLocale = Locale.ID order by IDOrdine desc;"
    );
    $profileinfo = $conn->query("select * from Utente where ID = ".$_SESSION['id']);
    $profile = mysqli_fetch_assoc($profileinfo);
    
    echo 
        "<div class='container text-center'>
        <h1>Profilo</h1>
        <table class='table'><tr><th>
        Nome </td><td>".$profile['Nome']."</td></tr><tr><th>
        Cognome </th><td>".$profile['Cognome']."</td></tr><tr><th>
        Cellulare </th><td>".$profile['Cellulare']."</td></tr><tr><th>
        Indirizzo </th><td>".request("32;" .str_replace(', ', ';', $profile['Indirizzo']))[1]."</td></tr><tr><th>
        Mail </th><td>".$profile['Mail']."</td></tr>
        </table>
        <h1>Ordini</h1>
        <table class='table'><tr><th>Prodotto</th><th>Locale</th><th>Costo</th><th>Recensisci</th></tr>";
    while ($order = mysqli_fetch_assoc($orders)) {
        $idordine = $order["IDOrdine"];

        echo "<tr><td>".$order['Prodotto']."</td><td>".$order['Locale']."</td><td>".money_format("%i", $order['Costo'])." €</td><td><a href='reviews.php?idordine=$idordine'>valuta</a></td></tr>";
    }
    echo "</table></div>";
    ?>
</body>