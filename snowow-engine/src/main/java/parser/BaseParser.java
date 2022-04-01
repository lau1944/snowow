package parser;

import com.google.gson.Gson;

/**
 * @author liuquan
 */
public abstract class BaseParser implements Parser {

    final Gson gson;

    public BaseParser() {
       this.gson = new Gson();
    }
}
