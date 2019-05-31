<?php
//P5JS
echo '<script src="https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.7.3/p5.js"></script>';
echo '<script src="http://cdnjs.cloudflare.com/ajax/libs/p5.js/0.5.6/addons/p5.dom.js"></script>';
//CSS
echo '<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">';
//jQuery
echo '<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>';
//Bootstrap
echo '<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>';
//Font
echo "<link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>";
//Mio CSS
echo '<link rel="stylesheet" href="./mystyle.css" type="text/css">';
//Sfondo
//echo '<script src = "background.js"></script>';

echo '<title>SquEat</title>';

echo '<link rel="icon" href="data/logo/squeatlogo.ico" />';

session_start();

function connect(){
    $conn = new mysqli('51.68.124.94', 'root', 'Az-AreaProgetto', 'areaprogetto');
    if ($conn->connect_error) {
     die("Connection failed: " . $conn->connect_error);
    } 
    return $conn;
}

function request($msg){
    $url = 'http://51.68.124.94:8080/AP_WS/NewServlet';
    $data = array('req' => $msg);

    $options = array(
        'http' => array(
            'header'  => "Content-type: application/x-www-form-urlencoded\r\n",
            'method'  => 'POST',
            'content' => http_build_query($data)
        )
    );
    $context  = stream_context_create($options);
    $result = file_get_contents($url, false, $context);
    if ($result === FALSE) {
        return explode(";", "null");
    }

    return explode(";", $result);
}

function refresh_page(){
    echo '<script>location.reload();</script>';
}

function reindirizza($link) {
    echo '<script> window.location.href = "'.$link.'";</script>';
    
}

function str_contains($str, $sub){
    return (strpos($str, $sub) !== false);
}

function getProductsInCartNumber(){
    $cart = explode("#", $_COOKIE["carrello"]);
    return count($cart);
}

?>