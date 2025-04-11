package org.example.learn.spring.boot.web.hello.util;

import org.junit.Test;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

/**
 * Reserved Characters
 *
 * !	#	$	&	'	(	)	*	+	,	/	:	;	=	?	@	[	]
 */
public class UriReservedCharactersTest {

    @Test
    public void test11() {
        String queryValue = " ";
        String encodedValue = UriUtils.encode(queryValue, StandardCharsets.UTF_8);
        System.out.println("encodedValue = " + encodedValue);
        String decodedQueryValue = UriUtils.decode(encodedValue, StandardCharsets.UTF_8);
        System.out.println("decodedQueryValue = " + decodedQueryValue);
    }

    @Test
    public void test12() {
        String queryValue = "+";
        String encodedValue = UriUtils.encode(queryValue, StandardCharsets.UTF_8);
        System.out.println("encodedValue = " + encodedValue);
        String decodedQueryValue = UriUtils.decode(encodedValue, StandardCharsets.UTF_8);
        System.out.println("decodedQueryValue = " + decodedQueryValue);
    }
}
