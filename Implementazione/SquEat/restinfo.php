<html>
    <head>
        <?php
        include 'setup.php';
        $result = request("7;" . $_GET["rest"]);
        $nome = &$result[1];
        $lat = substr($result[2], 0, strpos($result[2], ","));
        $lon = str_replace($lat.", ", "", $result[2]);
        $address = request("32;" . $lat . ";" . $lon)[1];
        $punteggio = $result[3] == 0 ? "Nessuna recensione" : $result[3];
        $cell = &$result[4];
        echo '<title> SquEat - ' . $nome . "</title>"; 
        ?>

        <script>
            function addCart(idprod, idrest, nomeprod){
                let cart = getCookie("carrello");
                if(cart != undefined){
                    setCookie("carrello", cart + "#" + idprod + "--" + idrest + "--" + nomeprod, 3);
                } else {
                    setCookie("carrello", idprod + "--" + idrest + "--" + nomeprod, 3);
                }
                document.getElementById("NumProdCart").innerHTML = cart.split("#").length;
            }

            window.getCookie = function(name) {
                var match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
                if (match) return match[2];
            }

            function setCookie(name,value,days) {
                var expires = "";
                if (days) {
                    var date = new Date();
                    date.setTime(date.getTime() + (days*24*60*60*1000));
                    expires = "; expires=" + date.toUTCString();
                }
                document.cookie = name + "=" + (value || "")  + expires + "; path=/";
            }
        </script>
    </head>
    <body>
        <?php
        include 'navbar.php';
        ?>
        <h1><?php echo $nome ?></h1>
        <table>
            <tr><td>Indirizzo </td><td><?php echo $address?></td></tr>
            <tr><td>Cellulare </td><td><?php echo $cell?></td></tr>
            <tr><td>Punteggio &nbsp</td><td><?php echo $punteggio?></td></tr>
            <tr><td colspan = "2">Menu</td></tr>
            <tr><td colspan = "2">
                <?php
                $menu = explode("-", $result[5]);
                echo '<table>';
                foreach($menu as $product){
                    $data = explode("#", $product);
                    $idprod = &$data[0];
                    $costo = &$data[1];
                    $nomeprod = &$data[2];
                    $ingredients = &$data[3];
                    echo '<tr>'.
                    '<td><button onclick="addCart('.$idprod.','.$_GET["rest"].',\''.$nomeprod.'\')">+</button></td>
                    <td class="selectable"><b>' . $nomeprod . '</b></td><td>' . $costo . '$</td></tr>';
                    echo '<tr><td></td><td>' . $ingredients . '</td></tr>';
                    echo '<tr><td>&nbsp</td></tr>';
                }
                echo '</table>';
                ?>
            </td></tr>
        </table>
    </body>
</html>