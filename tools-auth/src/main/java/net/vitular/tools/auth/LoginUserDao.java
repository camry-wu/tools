/*
 * -----------------------------------------------------------
 * file name  : LoginUserDao.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Fri 12 Jun 2015 12:17:59 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.auth;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import static org.hibernate.criterion.Restrictions.eq;
/*
import static org.hibernate.criterion.Restrictions.ne;
import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.or;
*/

import net.vitular.tools.common.dao.HibernateDao;

/**
 * login user dao.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class LoginUserDao extends HibernateDao implements ILoginUserDao {

    /**
     * get user by oid.
     *
     * @param oid user oid
     * @return LoginUser
    public LoginUser getUserByOid(final Long oid) {
        return (LoginUser) find(LoginUser.class, oid);
    }
     */
	
    /**
     * save user to db.
     *
     * @param user LoginUser
     * @return user oid
     */
    public Long saveUser(final LoginUser user) {
        if (user.getOid() == 0) {
            return (Long)insert(user);
        } else {
            return ((LoginUser)update(user)).getOid();
        }
    }

    /**
     * load user by username.
     *
     * @param username  user name
     * @return LoginUser
     */
    public LoginUser loadUserByUsername(final String username) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(LoginUser.class);
        criteria.add(eq("username", username));

        List<LoginUser> list = (List<LoginUser>) findByCriteria(criteria);
        if (list == null) {
            return null;
        }

        return list.get(0);
    }

    /*
    public List<LoginUser> findUserByUserID(String userID, Long excluedOid) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(LoginUser.class);
        criteria.add(eq(SystemConsts.FieldUser.UserID, userID));
        if (excluedOid != null && excluedOid > 0){
            criteria.add(ne(SystemConsts.FieldUser.Oid, excluedOid));
        }
		
        return (List<LoginUser>)findAllByCriteria(criteria);
    }*/

    /**
     * list user base on the query params.
     *
     * @param queryParams LoginUserQueryParams
     * @return SearchResult
    public SearchResult listUsers(LoginUserQueryParams queryParams) {
        return findPageByCriteriaRootOnly(createUserCriteria(queryParams), queryParams.getRowsPerPage(), queryParams.getStartIndex());
    }
     */

    /*
    protected DetachedCriteriaWithOrder createUserCriteria(LoginUserQueryParams queryParams) {
        DetachedCriteriaWithOrder criteria = null;
        criteria = CriteriaHelper.createCriteria(queryParams, LoginUser.class);
        DetachedCriteriaExt deCriteria = criteria.getDetachedCriteria();

        if (queryParams.getRegionOid() != null && !queryParams.getRegionOid().equals(Long.parseLong("0"))) {
        	deCriteria.add(eq(SystemConsts.FieldUser.RegionOid, queryParams.getRegionOid()));
        }

//        if (queryParams.getIsActive() != null) {
//        	deCriteria.add(eq(SystemConsts.FieldUser.IsActive, queryParams.getIsActive()));
//        }

        String keyword = queryParams.getKeyword();

        // set keyword to criteria
        if ( !StringUtils.isEmptyAfterTrim(keyword) ) {

            keyword = keyword.trim();

            deCriteria.add(or(like(SystemConsts.FieldUser.UserID, keyword, MatchMode.ANYWHERE),
  				  like(SystemConsts.FieldUser.UserName, keyword, MatchMode.ANYWHERE))
  			  );
        }

        Integer isActive = queryParams.getIsActive();
        if (null != isActive) {
            if (queryParams.getIsActive() == SystemConsts.NormalCodePubYesOrNo.YES) {
                deCriteria.add(Restrictions.eq(SystemConsts.FieldUser.IsActive, Boolean.TRUE));
            } else {
                deCriteria.add(Restrictions.eq(SystemConsts.FieldUser.IsActive, Boolean.FALSE));
            }
        }

        deCriteria.addOrder(Order.asc(SystemConsts.FieldUser.UserID));

        return criteria;
    }
    */
} // END: LoginUserDao
