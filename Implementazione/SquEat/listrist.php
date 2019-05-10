<html>
    <head>
        <?php
        include 'setup.php';
        ?>
        <style>
            .vise {
                margin: auto;
                width: 50% !important; 
             } 
        </style>
    </head>
    <body>
        <form action="restinfo.php" method="get">
            <?php
            include 'navbar.php';
            echo "
            <div class='container center_div text-center'><h1 style='font-size: 500%'>―ELENCO RISTORANTI―</h1>";
            if (!empty($_GET["address"])) {
                $result = request("29;" . $_GET["address"]);
                $response = request("5;" . $result[1]);
                for ($i = 1; $i <= count($response) - 4; $i += 4) {
                    $stelline = "<span style='color: #ffcc00'>";
                    for($o = 0; $o < $response[$i +2]; $o++){
                        $stelline .= '★';
                    }
                    $stelline .= "</span><span style='color: black'>";
                    for($o = 0; $o < 5 - $response[$i +2]; $o++){
                        $stelline .= '★';
                    }
                    $stelline .= "</span>";
                    $indirizzolatlon = str_replace(" ", "%20", $response[$i + 1]);
                    echo "<table class='vise' style='background-color: white; box-shadow: 2px 10px 10px 2px #cccccc; margin: 30px auto'><tr><td colspan='2' style='text-align: center'><img src='https://maps.googleapis.com/maps/api/streetview?size=600x400&location=".$indirizzolatlon."&fov=90&heading=235&pitch=10&key=AIzaSyAfc1SJtwKkoBUoy6Ri73hg-HUgfhphjHU' style='margin: 15px'></td></tr>";
                    echo "<tr><td style='text-align: center'><h1 style='margin: 0px'><span style='color: red; font-size: 85%'>Nome</span>:</td><td> " . $response[$i] . "</h1></td></tr>"
                    . "<tr><td style='text-align: center'><h1 style='margin: 0px'><span style='color: red; font-size: 85%'>Coordinate</span>:</td><td> " . $response[$i + 1] . "</h1></td></tr>"
                    . "<tr><td style='text-align: center'><h1 style='margin: 0px'><span style='color: red; font-size: 85%'>Stelle</span>:</td><td> " . $stelline . "</h1></td></tr>"
                    . "<tr><td colspan='2'><div class='text-center'><button class='btn btn-danger' name='rest' type='submit' value='" . $response[$i + 3] . "' style='margin: 15px'>RICHIEDI INFO</button></div></td></tr></table><br>";
                }
            } else {
                reindirizza("index.php");
            }
            ?>
            </div>
        </form>
    </body>
</html>