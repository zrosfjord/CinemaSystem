package com.zrosfjord.cs.search;

public class SearchTermException extends Exception {

    public SearchTermException(String term) {
        super(term + " isn't a search term!");
    }

}
