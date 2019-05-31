<?php include "setup.php"; ?>
<html>
    <head>
        <?php include "navbar.php"; ?>
        <title>Recensioni</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    </head>
<body>
    <div class="text-center">
        <h1 class="h1">Recensisci</h1>
        <div id="containerstars">
            <?php
            if (!empty($_GET["idordine"])) {
                $idordine = $_GET["idordine"];
                $conn = connect();
                $res = $conn->query("select * from Ordine where IDOrdine=$idordine and IDCliente=".$_SESSION['id']);
                if ($res->num_rows > 0) {
                    $idlocale = $res->fetch_assoc()['IDLocale'];
                    $res2 = $conn->query("select * from Locale where ID=$idlocale");
                    if ($res2->num_rows > 0) {
                        for($i = 0; $i < 5; $i++){
                            $index = $i+1;
                            if($i == 0) echo "<span id = 'star$i' class='h1' onmouseover='review($index)' style='color: #ffcc00' onclick='sendReview($index)'>★</span>";
                            else echo "<span id = 'star$i' class='h1' onmouseover='review($index)' onclick='sendReview($index)'>★</span>";
                        }
                    }  else echo "<div>Errore.</div>";
                } else echo "<div>Non hai effettuato questo ordine.</div>";
            } 
            ?>
        </div>
    </div>

    <script> 
        var HasClicked = false;

        function review(num) {
            if(!HasClicked){
                for (let i = 0; i < num; i++){
                    document.getElementById("star"+(i)).style.color = '#ffcc00'
                }
                for(let i = num; i < 5; i++){
                    document.getElementById("star"+(i)).style.color = ''
                }
            }
        }

        function sendReview(num){
            HasClicked = true
            let stars = "<span class='h1' style='color: #ffcc00'>"
            for (let i = 0; i < num; i++){
                stars += "★"
            }
            document.getElementById("containerstars").innerHTML = stars + "</span><br><span>inviata!</span>"
            console.log("OKAYYYY: " + num)
            document.getElementById("containerstars").style.opacity = "1"
            unfade(document.getElementById("containerstars"))

            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    //document.getElementById("demo").innerHTML = this.responseText;
                }
            };
            xhttp.open("GET", "util.php?review=true&star="+num+"&idordine="+<?php echo $_GET["idordine"]; ?>, true);
            xhttp.send();
        }

        function unfade(element) {
            var op = 0.1;  // initial opacity
            element.style.display = 'block';
            var timer = setInterval(function () {
                if (op >= 1){
                    clearInterval(timer);
                }
                element.style.opacity = op;
                element.style.filter = 'alpha(opacity=' + op * 100 + ")";
                op += op * 0.1;
            }, 10);
        }


    </script>
</body>
</html>
