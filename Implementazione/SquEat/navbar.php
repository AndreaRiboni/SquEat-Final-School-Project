<nav class=" navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.php">
                <img src="data/logo/squeatcolor.png" width="100px">
            </a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li role="separator" class="divider"></li>
                <li><a href="index.php">Trova ristoranti</a></li>
                <?php
                    if(sessionValid()){
                        echo "<li><a href='locale.php'>Hai un locale?</a></li>";
                    }
                ?>
            </ul>
            <ul class="nav navbar-nav navbar-right">
            <?php if(sessionValid()){ ?>
                <li><a href="profile.php">Profilo e Ordini</a></li>
                <li><a href = "carrello.php">Carrello (<span id="NumProdCart"><?php echo ($_COOKIE["carrello"]) == "" ? '0' : count(explode("-", substr($_COOKIE['carrello'], 0, -1)))/3; ?></span>)</a></li>
                <li><a href="request.php">Chiedilo a SquEat
                    <?php
                        $conn = connect();
                        $res = $conn -> query("select * from Risposte, Richieste where IDRichiesta = ID and Risposte.stato = 0");
                        if($res -> num_rows > 0) echo '(' . $res -> num_rows . ')';
                    ?>
                </a></li>
                <li><a href="index.php?action=logout">Logout</a></li>
                <?php } else { ?>
                    <li><a rel="popover" data-content='
                                <form method="post" action="index.php">
                                    <div class="form-group">
                                        <label>Mail:</label>
                                        <input type="email" name="loginemail" class="form-control" required/>
                                    </div>
                                    <div class="form-group">
                                        <label>Password:</label>
                                        <input type="password" name="loginpsw" class="form-control" required/>
                                    </div>
                                    <div class="form-group text-center">
                                        <input type="submit" value="login" class="btn btn-danger">
                                    </div>
                                </form>'
                            data-placement="bottom" data-original-title="SquEat" >Login</a>
                    </li>
                    <li><a rel="popover" data-content='
                                <form method="post" action="index.php">
                                    <div class="form-group">
                                        <label>Nome:</label>
                                        <input type="text" name="registername"  class="form-control" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Cognome:</label>
                                        <input type="text" name="registersurname"  class="form-control" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Email:</label>
                                        <input type="email" name="registeremail"  class="form-control" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Cellulare:</label>
                                        <input type="phone" name="registerphone"  class="form-control" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Indirizzo (completo):</label>
                                        <input type="text" name="registeraddress"  class="form-control" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Password:</label>
                                        <input type="password" name="registerpassword"  class="form-control"/ required>
                                    </div>
                                    <div class="form-group text-center">
                                        <input type="submit" value="registrati" class="btn btn-danger">
                                    </div>
                                </form>'
                            data-placement="bottom" data-original-title="SquEat">Registrati</a>
                    </li>
            <?php } ?>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<br><br>
<img src="data/pizza.jpg.png" class="img-fluid" alt="Pizza" width=100%>

<?php
function sessionValid(){
    session_start();
    if(!empty($_SESSION["id"])){
        //controlli
        return true;
    }
    return false;
}
?>

<script>

function getCookie(name) {
    var match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    if (match) return match[2];
}
</script>

<!-- SCRIPT PER I POPOVER FORM  -->
<script>
$('a[rel=popover]').popover({
    html: 'true',
placement: 'bottom',
container: 'body'
})
$('a[rel=popover]').popover();

$('a[rel=popover]').on('click', function (e) {
    $('a[rel=popover]').not(this).popover('hide');
});
</script>