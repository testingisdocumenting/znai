package com.twosigma.documentation.codesnippets;

import java.util.List;
import java.util.Map;

/**
 * @author mykola
 */
public interface CodeTokenizer {
    List<Map<String, Object>> tokenize(String lang, String code);
}
