package net.vitular.tools.common.dao;

import java.util.List;

/**
 * Dao Support.
 * @author DL
 */
public interface IDaoSupport
{
    /**
     * Make query
     *
     * @param sQuery hive query
     *
     * @return list of query result
     */
    public List query(final String sQuery);

    /**
     * Make query
     *
     * @param sQuery hive query
     * @param listPar list of query parameters
     *
     * @return list of query result
     */
    public List query(final String sQuery, final List listPar);

    /**
     * Make query
     *
     * @param sQuery hive query
     * @param aobjPar array of query parameters
     *
     * @return list of query result
     */
    public List query(final String sQuery, final Object[] aobjPar);

    /**
     * Make query
     *
     * @param sQuery hive query
     * @param listPar list of query parameters
     * @param nLimit limitation of query result
     *
     * @return list of query result
     */
    public List query(final String sQuery, final List listPar, final int nLimit);

    /**
     * Make query
     *
     * @param sQuery hive query
     * @param aobjPar array of query parameters
     * @param nLimit limitation of query result
     *
     * @return list of query result
     */
    public List query(final String sQuery, final Object[] aobjPar, final int nLimit);

    /**
     * Make query by page
     *
     * @param sQuery hive query
     * @param listPar list of query parameters
     * @param iCurrentPage current page, begin from 1
     * @param iRowsPerPage rows per page
     *
     * @return SearchResult
     */
    public SearchResult queryByPage(final String sQuery, final List listPar, final int iCurrentPage, final int iRowsPerPage);

    /**
     * Make query by page
     *
     * @param sQuery hive query
     * @param listPar list of query parameters
     * @param iCurrentPage current page, begin from 1
     * @param iRowsPerPage rows per page
     * @param bCountAll If count all record
     *
     * @return SearchResult
     */
    public SearchResult queryByPage(final String sQuery, final List listPar, final int iCurrentPage, final int iRowsPerPage, final boolean bCountAll);

    /**
     * Make query by page
     *
     * @param sQuery hive query
     * @param aobjPar array of query parameters
     * @param iCurrentPage current page, begin from 1
     * @param iRowsPerPage rows per page
     *
     * @return SearchResult
     */
    public SearchResult queryByPage(final String sQuery, final Object[] aobjPar, final int iCurrentPage, final int iRowsPerPage);

    /**
     * Make query by page
     *
     * @param sQuery hive query
     * @param aobjPar array of query parameters
     * @param iCurrentPage current page, begin from 1
     * @param iRowsPerPage rows per page
     * @param bCountAll If count all record
     *
     * @return SearchResult
     */
    public SearchResult queryByPage(final String sQuery, final Object[] aobjPar, final int iCurrentPage, final int iRowsPerPage, final boolean bCountAll);

    /**
     * Count all records based on a query string which does not include count fragment
     *
     * @param sQuery query string
     * @param aobjPar parameters
     *
     * @return record number
     */
    public long countAll(final String sQuery, final Object[] aobjPar);

    /**
     * Count all records based on a query string which does not include count fragment
     *
     * @param sQuery query string
     * @param aobjPar parameters
     *
     * @return record number
     */
    public long countAll(final String sQuery, final List listPar);

    /**
     * Execute a query or an update
     *
     * @param sQuery hive query
     */
    public void execute(final String sQuery);

    /**
     * Execute a query or an update
     *
     * @param sQuery hive query
     * @param aobjPar array of query parameters
     */
    public void execute(final String sQuery, final Object[] aobjPar);

    /**
     * Execute a query or an update
     *
     * @param sQuery hive query
     * @param listPar list of query parameters
     */
    public void execute(final String sQuery, final List listPar);

    /**
     * Update or delete based on named parameters
     *
     * @param sHql HQL
     * @param sParName named parameter name
     * @param aobjArg parameters
     */
    public void execute(final String sHql, final String sParName, final Object[] aobjArg);

    /**
     * Delete all records based on parameters
     *
     * @param sHql Delete HQL
     * @param aobjArg parameters
     */
    public void deleteAll(final String sDeleteHql);

    /**
     * Delete all records based on parameters
     *
     * @param sHql Delete HQL
     * @param aobjArg parameters
     */
    public void deleteAll(final String sDeleteHql, final Object[] aobjArg);
}
