<html>
    <head>
        <?php
        include 'setup.php';
        $result = request("7;" . $_GET["rest"]);
        $nome = &$result[1];
        $lat = substr($result[2], 0, strpos($result[2], ","));
        $lon = str_replace($lat.", ", "", $result[2]);
        $address = request("32;" . $lat . ";" . $lon)[1];
        $punteggio = $result[3];
        $stelline = "<span style='color: #ffcc00'>";
                    for($o = 0; $o < $punteggio; $o++){
                        $stelline .= '★';
                    }
                    $stelline .= "</span><span style='color: black'>";
                    for($o = 0; $o < 5 - $punteggio; $o++){
                        $stelline .= '★';
                    }
                    $stelline .= "</span>";
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
                document.getElementById("contentcarrello").innerHTML = cart.split("#").length;

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
        
        <style>
            h1 {
                font-size: 130%;
            }
            #map {
                height: 100%;
            }
        </style>
    </head>
    <body>
        <?php
        include 'navbar.php';
        ?>
        <?php echo "<div class='container center_div text-center'><h1 style='font-size: 500%'>" . $nome . "</h1>"; ?>

        <div id="map" style="max-width: 600px; max-height: 500px; margin: 0px auto"></div>
        <table style="margin: 0px auto">
            <tr><td><h1><span style='color: red;'>Indirizzo </span></h1></td><td><h1><?php echo $address?></h1></td></tr>
            <tr><td><h1><span style='color: red;'>Cellulare </span></h1></td><td><h1><?php echo $cell?></h1></td></tr>
            <tr><td><h1><span style='color: red;'>Punteggio &nbsp</span></h1></td><td><h1><?php echo $stelline?></h1></td></tr>
            <tr><td colspan = "2"><h1>Menu</h1></td></tr>
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
                    '<td><button class="btn btn-danger" style="border-radius: 100%; font-size: 100%" onclick="addCart('.$idprod.','.$_GET["rest"].',\''.$nomeprod.'\')">+</button>&nbsp&nbsp</td>
                    <td class="selectable"><b>' . $nomeprod . '</b></td><td>' . number_format((float)$costo, 2, ",", "") . '€</td></tr>';
                    echo '<tr><td></td><td>' . $ingredients . '</td></tr>';
                    echo '<tr><td>&nbsp</td></tr>';
                }
                echo '</table></div>';
                ?>
            </td></tr>
        </table>
        <div class="btn btn-danger" style="border-radius: 20%; font-size: 100%; position: fixed; bottom: 50px; right: 50px" href="carrello.php">
            <img src="https://png.pngtree.com/svg/20170728/cart_white_1020224.png" width="32px">
            <span id="contentcarrello"></span>
        </div>

        <script>
            let cart = getCookie("carrello");
            document.getElementById("contentcarrello").innerHTML = cart.split("#").length;
        </script>
        
        
    <script>

      function initMap() {
        var myLatLng = {lat: <?php echo $lat; ?>, lng: <?php echo $lon; ?>};

        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 20,
          center: myLatLng
        });

        var marker = new google.maps.Marker({
          position: myLatLng,
          map: map,
          title: 'Locale',
          animation: google.maps.Animation.DROP,
        });

        var infowindow = new google.maps.InfoWindow({
            content: <?php echo '"'.$nome.'"'; ?>
        });

        marker.addListener('click', function() {
            infowindow.open(map, marker);
        });
      }

      function toggleBounce() {
        if (marker.getAnimation() !== null) {
            marker.setAnimation(null);
        } else {
            marker.setAnimation(google.maps.Animation.BOUNCE);
        }
    }
    </script>
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAfc1SJtwKkoBUoy6Ri73hg-HUgfhphjHU&callback=initMap">
    </script>
        
    </body>
</html>