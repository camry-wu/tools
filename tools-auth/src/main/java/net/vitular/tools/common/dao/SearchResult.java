package net.vitular.tools.common.dao;

import java.io.Serializable;
import java.util.Collection;

public class SearchResult implements Serializable {

    private Collection  _items;
    // TODO: it should be long
    private long        _lTotal;

    public SearchResult() {
    }

    public SearchResult(Collection items, long total) {
        _items = items;
        _lTotal = total;
    }

    public long getSize() { return _items.size(); }

    // getter and setter
    public void setItems(Collection items) { _items = items; }
    public Collection getItems() { return _items; }

    public long getTotal() { return _lTotal; }
    public void setTotal(long total) { _lTotal = total; }
}
