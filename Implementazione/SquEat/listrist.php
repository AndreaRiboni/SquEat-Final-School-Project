<html>
    <head>
        <?php
        include 'setup.php';
        ?>
    </head>
    <body>
        <form action="restinfo.php" method="get">
            <?php
            include 'navbar.php';
            echo "<h2>ELENCO RISTORANTI</h2>";
            if (!empty($_GET["address"])) {
                $result = request("29;" . $_GET["address"]);
                $response = request("5;" . $result[1]);
                for ($i = 1; $i <= count($response) - 4; $i += 4) {
                    echo "Nome: " . $response[$i] . "<br>"
                    . "Coordinate: " . $response[$i + 1] . "<br>"
                    . "Stelle: " . $response[$i + 2] . "<br>"
                    . "<button name='rest' type='submit' value='" . $response[$i + 3] . "'>RICHIEDI INFO</button><br><br>";
                }
            } else {
                reindirizza("index.php");
            }
            ?>
        </form>
    </body>
</html>