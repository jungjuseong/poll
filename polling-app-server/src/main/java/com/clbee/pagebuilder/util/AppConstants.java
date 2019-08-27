package com.clbee.pagebuilder.util;

import java.util.ArrayList;

public interface AppConstants {
    String DEFAULT_PAGE_NUMBER = "0";
    String DEFAULT_PAGE_SIZE = "30";

    int MAX_PAGE_SIZE = 50;

    String[] SORTKEYS = {"name", "updatedAt", "createdAt"};
    String[] SORT_DIRECTIONS = {"desc", "asc"};
    String DEFAULT_SORTKEY = "updatedAt";
    String DEFAULT_DIRECTION = "desc";
}
