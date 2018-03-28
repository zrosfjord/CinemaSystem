package com.zrosfjord.cs.search;

import java.util.LinkedList;
import java.util.List;

public interface Searchable {

    List<Search.Result> search(String question) throws SearchTermException;

}
