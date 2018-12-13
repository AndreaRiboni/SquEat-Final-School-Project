package webtest;

import java.io.IOException;

/**
 *
 * @author Riboni Andrea
 */
public class WebTest {

    

    public static void main(String[] args) throws IOException {
        System.out.println(
                WebUtility.getHTMLTagValue( //Voglio ottenere il valore del TAG chiamato "nome"
                        WebUtility.stringToHTML( //Della pagina HTML contenuta nella stringa
                                WebUtility.getHTMLPage( //che ho scaricato dall'url
                                        WebUtility.getParametricURL(
                                                "localhost/HTTP4Java/index.php", //(questo url)
                                                "nome", //chiedendo come parametro il nome
                                                "andrea" //e passando come valore del parametro nome "andrea"
                                        )
                                )
                        ),
                        "nome"
                )
        );
    }

}
