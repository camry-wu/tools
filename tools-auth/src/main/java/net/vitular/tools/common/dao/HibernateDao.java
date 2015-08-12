package net.vitular.tools.common.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

/**
 * Hibernate Dao
 * @author DL
 */
public class HibernateDao extends HibernateDaoSupport implements IDaoSupport {
    protected Log logger = LogFactory.getLog(getClass());

    private String SELECT_COUNT_START = "select count(*) ";

    /**
     * Make query
     *
     * @param sQuery hive query
     *
     * @return list of query result
     */
    public List query(final String sQuery)
    {
        return getHibernateTemplate().find(sQuery);
    }

    /**
     * Make query
     *
     * @param sQuery hive query
     * @param listPar list of query parameters
     *
     * @return list of query result
     */
    public List query(final String sQuery, final List listPar)
    {
        return query(sQuery, listPar.toArray());
    }

    /**
     * Make query
     *
     * @param sQuery hive query
     * @param aobjPar array of query parameters
     *
     * @return list of query result
     */
    public List query(final String sQuery, final Object[] aobjPar)
    {
        return getHibernateTemplate().find(sQuery, aobjPar);
    }

    /**
     * Make query
     *
     * @param sQuery hive query
     * @param listPar list of query parameters
     * @param nLimit limitation of query result, only valid when it is larger than 0
     *
     * @return list of query result
     */
    public List query(final String sQuery, final List listPar, final int nLimit)
    {
        return query(sQuery, listPar.toArray(), nLimit);
    }

    /**
     * Make query
     *
     * @param sQuery hive query
     * @param aobjPar array of query parameters
     * @param nLimit limitation of query result, only valid when it is larger than 0
     *
     * @return list of query result
     */
    public List query(final String sQuery, final Object[] aobjPar, final int nLimit)
    {
        return (List) getHibernateTemplate().execute(new HibernateCallback()
        {
            public Object doInHibernate(Session session) throws HibernateException
            {
                Query query = session.createQuery(sQuery);
                addParameters(query, aobjPar);

                if( nLimit > 0 )
                    query.setMaxResults(nLimit);

                return query.list();
            }
        });
    }

    /**
     * Query by pure SQL
     *
     * @param sSQL Pure SQL string
     * @param aobjPar parameters
     * @param limitSize Limited Size, only valid when it is larger than 0.
     *
     * @return List of result
     */
    public List querySQL(final String sSQL, final Object[] aobjPar, final int nLimit)
    {
        return (List) getHibernateTemplate().execute(new HibernateCallback()
        {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createSQLQuery(sSQL);

                addParameters(query, aobjPar);

                if (nLimit > 0)
                    query.setMaxResults(nLimit);

                return query.list();
            }
        });
    }

    /**
     * Make query by page and do not count all records
     *
     * @param sQuery hive query
     * @param listPar list of query parameters
     * @param iCurrentPage current page, begin from 1
     * @param iRowsPerPage rows per page
     *
     * @return SearchResult
     */
    public SearchResult queryByPage(final String sQuery, final List listPar, final int iCurrentPage, final int iRowsPerPage)
    {
        return queryByPage(sQuery, listPar.toArray(), iCurrentPage, iRowsPerPage, false/* bCountAll */);
    }

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
    public SearchResult queryByPage(final String sQuery, final List listPar, final int iCurrentPage, final int iRowsPerPage, final boolean bCountAll)
    {
        return queryByPage(sQuery, listPar.toArray(), iCurrentPage, iRowsPerPage, bCountAll);
    }

    /**
     * Make query by page and do not count all records
     *
     * @param sQuery hive query
     * @param aobjPar array of query parameters
     * @param iCurrentPage current page, begin from 1
     * @param iRowsPerPage rows per page
     *
     * @return SearchResult
     */
    public SearchResult queryByPage(final String sQuery, final Object[] aobjPar, final int iCurrentPage, final int iRowsPerPage)
    {
        return queryByPage(sQuery, aobjPar, iCurrentPage, iRowsPerPage, false/* bCountAll */);
    }

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
    public SearchResult queryByPage(final String sQuery, final Object[] aobjPar, final int iCurrentPage, final int iRowsPerPage, final boolean bCountAll)
    {
        return (SearchResult) getHibernateTemplate().execute(
                new HibernateCallback()
                {
                    public Object doInHibernate(Session session) throws HibernateException {
                        Query query = session.createQuery(sQuery);

                        addParameters(query, aobjPar);

                        if (iCurrentPage > 0 && iRowsPerPage > 0) {
                            query.setFirstResult((iCurrentPage - 1) * iRowsPerPage);
                            query.setMaxResults(iRowsPerPage);
                        }

                        List listRecord = query.list();

                        long iTotal;
                        if (bCountAll)
                            iTotal = countAll(sQuery, aobjPar);
                        else
                            iTotal = makeFooTotal(listRecord.size(), iCurrentPage, iRowsPerPage);

                        return new SearchResult(listRecord, iTotal);
                    }
                });
    }

    protected long makeFooTotal(long nCurPageRec, int iCurrentPage, int iRowsPerPage)
    {
        if( iRowsPerPage <= 0 || iCurrentPage <= 0 )
        {
            return nCurPageRec;
        }
        else
        {
            if( nCurPageRec == iRowsPerPage )
                return iCurrentPage * iRowsPerPage + 1;
            else
                return (iCurrentPage - 1) * iRowsPerPage + nCurPageRec;
        }
    }

    /**
     * Count all records based on a query string which does not include count fragment
     *
     * @param sQuery query string
     * @param aobjPar parameters
     *
     * @return record number
     */
    public long countAll(final String sQuery, final List listPar)
    {
        return countAll(sQuery, listPar.toArray());
    }

    /**
     * Count all records based on a query string which does not include count fragment
     *
     * @param sQuery query string
     * @param aobjPar parameters
     *
     * @return record number
     */
    public long countAll(final String sQuery, final Object[] aobjPar)
    {
        String sCount = makeCountQuery(sQuery);

        List listRet = query(sCount, aobjPar);

        return (Long)listRet.get(0);
    }

    @Override
    public void execute(final String sQuery)
    {
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                return session.createQuery(sQuery).executeUpdate();
            }
        });
    }

    @Override
    public void execute(String sQuery, List listPar)
    {
        execute(sQuery, listPar.toArray());
    }

    @Override
    public void execute(final String sQuery, final Object[] aobjPar)
    {
        getHibernateTemplate().execute(new HibernateCallback()
        {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(sQuery);

                addParameters(query, aobjPar);

                return query.executeUpdate();
            }
        });
    }

    /**
     * Update or delete based on named parameters
     *
     * @param sHql HQL
     * @param sParName named parameter name
     * @param aobjArg parameters
     */
    @Override
    public void execute(final String sHql, final String sParName, final Object[] aobjArg)
    {
        getHibernateTemplate().execute(new HibernateCallback()
        {
            public Object doInHibernate(Session session) throws HibernateException {
                Query queryObject = session.createQuery(sHql);
                if (aobjArg != null) {
                    queryObject.setParameter(sParName, aobjArg);
                }
                queryObject.executeUpdate();
                return null;
            }
        });
    }

    public Object insert(Object obj)
    {
        return getHibernateTemplate().save(obj);
    }

    public Object update(Object obj)
    {
        getHibernateTemplate().update(obj);
        return obj;
    }

    public Object merge(Object obj)
    {
        return getHibernateTemplate().merge(obj);
    }

    public Object saveOrupdate(Object obj)
    {
        getHibernateTemplate().saveOrUpdate(obj);
        return obj;
    }

    public void delete(Object obj)
    {
        getHibernateTemplate().delete(obj);
    }

    /**
     * Delete all records based on parameters
     *
     * @param sHql Delete HQL
     * @param aobjArg parameters
     */
    public void deleteAll(final String sDeleteHql)
    {
        getHibernateTemplate().execute(new HibernateCallback()
        {
            public Object doInHibernate(Session session) throws HibernateException {
                Query queryObject = session.createQuery(sDeleteHql);
                queryObject.executeUpdate();
                return null;
            }
        });
    }

    /**
     * Delete all records based on parameters
     *
     * @param sHql Delete HQL
     * @param aobjArg parameters
     */
    public void deleteAll(final String sDeleteHql, final Object[] aobjArg)
    {
        getHibernateTemplate().execute(new HibernateCallback()
        {
            public Object doInHibernate(Session session) throws HibernateException {
                Query queryObject = session.createQuery(sDeleteHql);
                if (aobjArg != null) {
                    for (int i = 0; i < aobjArg.length; i++) {
                        queryObject.setParameter(i, aobjArg[i]);
                    }
                }
                queryObject.executeUpdate();
                return null;
            }
        });
    }

    /**
     * Evict Object From Session
     *
     * @param obj Object to be evicted
     */
    public void evict(Object obj)
    {
        getHibernateTemplate().evict(obj);
    }

    /*============================================================================*
     *                              Protected Methods                             *
     =============================================================================*/

    /**
     * Make count query based on the given query, and count
     *
     * @param sQuery the original hive query
     *
     * @return count result
     */
    protected String makeCountQuery(String sQuery)
    {
        String sCountQL;

        int iFromIdx = sQuery.indexOf("from");
        int iOrderIdx = sQuery.lastIndexOf("order by");

        if( iFromIdx >= 0 )
        {
            if( iOrderIdx >= 0 )
            {
                sCountQL = SELECT_COUNT_START + sQuery.substring(iFromIdx, iOrderIdx);
            }
            else
            {
                sCountQL = SELECT_COUNT_START + sQuery.substring(iFromIdx);
            }
        }
        else
        {
            sCountQL = sQuery;
        }

        return sCountQL;
    }

    protected void addParameters(Query query, Object[] aobjPar)
    {
        if( aobjPar != null )
        {
            for( int i = 0; i < aobjPar.length; i++ )
            {
                query.setParameter(i, aobjPar[i]);
            }
        }
    }

    protected List findByCriteria(DetachedCriteria criteria) {
        return getHibernateTemplate().findByCriteria(criteria);
    }

    protected List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) {
        return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
    }
}
