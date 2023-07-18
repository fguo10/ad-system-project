package com.example.adsearch.search;

import com.example.adsearch.search.vo.*;

public interface SearchInterface {
    SearchResponse fetchAds(SearchRequest request);
}
