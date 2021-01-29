package id.co.bni.otf.bootcamp.support;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {
    public String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
