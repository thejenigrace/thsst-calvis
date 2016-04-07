package configuration.model.engine;

import com.github.pfmiles.dropincc.Grule;
import com.github.pfmiles.dropincc.TokenDef;

/**
 * Created by Goodwin Chua on 17/03/2016.
 */
public class TokenBag {

    private TokenDef hex;
    private TokenDef dec;
    private Grule justLabel;
    private TokenDef floating;

    public TokenBag(TokenDef hex, TokenDef dec, Grule justLabel, TokenDef floating) {
        this.hex = hex;
        this.dec = dec;
        this.justLabel = justLabel;
        this.floating = floating;
    }

    public TokenDef hex() {
        return hex;
    }

    public TokenDef dec() {
        return dec;
    }

    public Grule justLabel() {
        return justLabel;
    }

    public TokenDef floating() {
        return floating;
    }
}
