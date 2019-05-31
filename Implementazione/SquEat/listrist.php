<html>
    <head>
        <?php
        include 'setup.php';
        ?>
        <style>
            .vise {
                margin: auto;
                width: 100% !important; 
             } 
        </style>
        
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    </head>
    <body>
        <form action="restinfo.php" method="get">
            <?php
            include 'navbar.php';
            echo "
            <div class='container center_div text-center'><h1>―ELENCO RISTORANTI―</h1>";
            if (!empty($_GET["address"])) {
                $result = request("29;" . $_GET["address"]);
                $response = request("5;" . $result[1]);
                $_SESSION["indirizzo"] = $result[1];
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
                    $latlon = str_replace(",%20", ";", $indirizzolatlon);
                     $indir = request("32;" .$latlon)[1];
                    
                     
                    echo "<table class='vise' style='background-color: white; box-shadow: 2px 10px 10px 2px #cccccc; margin: 30px auto'><tr><td><div class='container img'><img class='img-responsive center-block' src='https://maps.googleapis.com/maps/api/streetview?size=600x400&location=".$indirizzolatlon."&fov=90&heading=235&pitch=10&key=AIzaSyAfc1SJtwKkoBUoy6Ri73hg-HUgfhphjHU' style='padding: 10px'></div></td></tr>";
                    echo "<tr><td style='text-align: center'><h1 style='margin: 0px'>" . $response[$i] . "</h1></td></tr>"
                    . "<tr><td style='text-align: center'><h5 style='margin: 0px'>" . $indir . "</h5></td></tr>"
                    . "<tr><td style='text-align: center'><h1 style='margin: 0px'>" . $stelline . "</h1></td></tr>"
                    . "<tr><td><div class='text-center'><button class='btn btn-danger' name='rest' type='submit' value='" . $response[$i + 3] . "' style='margin: 15px'>RICHIEDI INFO</button></div></td></tr></table><br>";
                }
                if(count($response) < 2){
                	echo "<div>Nessun ristorante nelle vicinanze</div>";
                }
            } else {
                reindirizza("index.php");
            }
            ?>
            </div>
        </form>
    </body>
</html>