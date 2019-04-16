<html>
    <head>
    <?php
    include 'setup.php';
    ?>
<title>SquEat - Carrello</title>
</head>
<body>
    <?php
    include 'navbar.php';
    ?>
    <h1>Carrello</h1>
    <div id="CartContent"></div>

    <script>

        function showCart(){
            cart = getCookie("carrello").split("#");
            var carttext = "Nessun prodotto nel carrello";
            if(cart[0].includes("--")){
                carttext = "<table>";
                for(let i = 0; i < cart.length; i++){
                    let product = cart[i].split("--");
                    carttext += "<tr><td>" + product[2] + "<td></tr>";
                }
                carttext += "</table>";
            }
            document.getElementById("CartContent").innerHTML = carttext;
            console.log(carttext);
        }

        showCart();
    </script>
</body>
</html>