<html>
    <head>
    <?php
    include 'setup.php';
    ?>
    
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>SquEat - Carrello</title>
</head>
<body>
    <?php
    include 'navbar.php';
    ?>
    <div style="margin: 0px auto" class='container center_div text-center'><h1 style="color: red; font-size: 350%">Carrello</h1>
        <div id="CartContent" style="margin: 0px auto" class='container center_div text-center'></div>
    </div>
    <div id='chart'>
    
    <?php
    function getName($index, $table) {
        $conn = connect();
        $namedata = $conn->query("select Nome from ".$table." where ID = ".$index);
        $name = mysqli_fetch_assoc($namedata);
        return $name["Nome"];
    }
    if (strlen($_COOKIE['carrello']) > 1) {
        echo "<table class='table'><tr><th>Locale</th><th>Prodotto</th><th>Costo</th></tr>";
        $cart = explode("-",substr($_COOKIE['carrello'], 0, -1));
        for ($i = 0; $i < count($cart); $i+=3) {
            echo "<tr><td>".getName($cart[$i], "Locale")."</td><td>".getName($cart[$i+1], "Prodotto")."</td><td>".money_format("%i", $cart[$i+2])." €</td></tr>";
        }
        echo '</table><div class="container text-center">
        <div class="btn-group btn-group-inline" role="group">
<button class="btn btn-danger" onclick="buy()">compra</button>
<button class="btn btn-danger" onclick="svuota()">svuota</button>
</div></div>';
    } else {
        echo "<div class='text-center'><h1>Carrello Vuoto</h1></div>";
    }
    ?>
    </div>
    <
    <script>
            function buy(){
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (this.readyState == 4 && this.status == 200) {
                        console.log(this.responseText);
                    }
                };
                xhttp.open("GET", "util.php?buy=true", true);
                xhttp.send();
                document.getElementById('chart').innerHTML = "<div class='text-center'><h1>Acquisto avvenuto</h1></div>";
                document.getElementById('NumProdCart').innerHTML = 0;
            }

            function svuota(){
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (this.readyState == 4 && this.status == 200) {
                        console.log(this.responseText);
                    }
                };
                xhttp.open("GET", "util.php?SvuotaCarrello=pieno", true);
                xhttp.send();
                document.getElementById('chart').innerHTML = "<div class='text-center'><h1>Carrello vuoto</h1></div>";
                document.getElementById('NumProdCart').innerHTML = 0;
            }

    </script>
</body>
</html>