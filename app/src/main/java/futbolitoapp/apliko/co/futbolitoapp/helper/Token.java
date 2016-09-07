package futbolitoapp.apliko.co.futbolitoapp.helper;

/**
 * Created by iosdeveloper on 25/08/16.
 */
public class Token {

    private int id;
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
