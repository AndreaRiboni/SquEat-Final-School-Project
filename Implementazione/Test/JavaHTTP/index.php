<html>
    <head>
        <meta charset="UTF-8">
        <title>hey</title>
    </head>
    <body>
        <?php
        if (isset($_GET["nome"])) {
            echo "<p>questa persona si chiama <span id = \"nome\">" . $_GET["nome"] . "</span>.</p>";
        } else {
            echo "nessun nome specificato.";
        }
        ?>
    </body>
</html>
