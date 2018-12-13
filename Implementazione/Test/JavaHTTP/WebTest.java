package webtest;

import java.io.IOException;

/**
 *
 * @author Riboni Andrea
 */
public class WebTest {

    

    public static void main(String[] args) throws IOException {
        System.out.println(
                WebUtility.getHTMLTagValue(
                        WebUtility.stringToHTML(
                                WebUtility.getHTMLPage(
                                        WebUtility.getParametricURL(
                                                "localhost/HTTP4Java/index.php",
                                                "nome",
                                                "andrea"
                                        )
                                )
                        ),
                        "nome"
                )
        );
    }

}
