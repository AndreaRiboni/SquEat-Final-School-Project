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
    <div style="margin: 0px auto" class='container center_div text-center'><h1 style="color: red; font-size: 350%">Carrello</h1>
        <div id="CartContent" style="margin: 0px auto" class='container center_div text-center'></div>
    </div>
    <script>

        function showCart(){
            cart = getCookie("carrello").split("#");
            var carttext = "Nessun prodotto nel carrello";
            if(cart[0].includes("--")){
                carttext = "<table style='margin: 0px auto' class='container center_div text-center'>";
                for(let i = 0; i < cart.length; i++){
                    let product = cart[i].split("--");
                    carttext += "<tr><td><button  style= 'border-radius: 50px; max-width: 25px; max-height: 25px' onclick='removeprod(\"" + cart[i] + "\")' class='btn btn-danger'>-</button>" + product[2] + "<td></tr>";
                }
                carttext += "</table>";
            }
            document.getElementById("CartContent").innerHTML = carttext;
        }

        showCart();

        function removeprod(prod){
        let cart = document.cookie.substring(0, document.cookie.indexOf(prod)) + document.cookie.substring(document.cookie.indexOf(prod) + prod.length + 1).split(";")[1];
        delete_cookie("carrello");
        setCookie("carrello", cart, 3);
        }

        function setCookie(name,value,days) {
                var expires = "";
                if(days < 0) {
                    
                } else {
                    var date = new Date();
                    date.setTime(date.getTime() + (days*24*60*60*1000));
                    expires = "; expires=" + date.toUTCString();
                    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
                }
            }

            function delete_cookie( name ) {
                document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
            }
    </script>
</body>
</html>