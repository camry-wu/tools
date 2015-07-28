/*
 * -----------------------------------------------------------
 * file name  : LoginUser.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Wed 17 Jun 2015 04:59:37 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.auth;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.util.ByteSource;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeConverter;
import javax.persistence.AttributeNode;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Cache;
import javax.persistence.Cacheable;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstraintMode;
import javax.persistence.ConstructorResult;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Converts;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityGraph;
import javax.persistence.EntityListeners;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityResult;
import javax.persistence.EntityTransaction;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.ExcludeDefaultListeners;
import javax.persistence.ExcludeSuperclassListeners;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.FlushModeType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.LockModeType;
import javax.persistence.LockTimeoutException;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.MapKeyJoinColumns;
import javax.persistence.MapKeyTemporal;
import javax.persistence.MappedSuperclass;
import javax.persistence.MapsId;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.NamedSubgraph;
import javax.persistence.NonUniqueResultException;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OptimisticLockException;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Parameter;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContexts;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceProperty;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.PersistenceUtil;
import javax.persistence.PessimisticLockException;
import javax.persistence.PessimisticLockScope;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Query;
import javax.persistence.QueryHint;
import javax.persistence.QueryTimeoutException;
import javax.persistence.RollbackException;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.SequenceGenerator;
import javax.persistence.SharedCacheMode;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.Subgraph;
import javax.persistence.SynchronizationType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TransactionRequiredException;
import javax.persistence.Transient;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.TypedQuery;
import javax.persistence.UniqueConstraint;
import javax.persistence.ValidationMode;
import javax.persistence.Version;

import net.vitular.tools.common.domain.BaseObj;

/**
 * LoginUser domain.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
@Entity
@Table(name="cooking.user")
@AttributeOverride(name="oid", column=@Column(name="oid"))
public class LoginUser extends BaseObj implements java.io.Serializable {

    /**
     * object id.
     */
    private long _lOid;

    /**
     * username.
     */
    private String _sUsername;

    /**
     * password.
     */
    private String _sPassword;

    /**
     * password salt.
     */
    private String _sSalt;

    /**
     * is locked.
     */
    private Boolean _bLocked = false;

    /**
     * verify email.
     */
    private String _sVerifyEmail;

    /**
     * verify cell phone number.
     */
    private String _sVerifyCellPhoneNo;

    // getter and setter
    public void setOid(final long oid) { _lOid = oid; }
    @Id
    @Column(name="oid")
    @TableGenerator(
        name="user-oid-generator",
        table="serial_number",
        pkColumnName="IdKey",
        pkColumnValue="userOid",
        valueColumnName="IdValue",
        allocationSize=1
    )
    @GeneratedValue(strategy=GenerationType.TABLE, generator="user-oid-generator")
    public long getOid() { return _lOid; }

    public void setUsername(final String username) { _sUsername = username; }
    public String getUsername() { return _sUsername; }

    public void setPassword(final String password) { _sPassword = password; }
    public String getPassword() { return _sPassword; }

    public void setSalt(final String salt) { _sSalt = salt; }
    public String getSalt() { return _sSalt; }

    public void setLocked(final Boolean locked) { _bLocked = locked; }
    public Boolean getLocked() { return _bLocked; }

    public void setVerifyEmail(final String verifyEmail) { _sVerifyEmail = verifyEmail; }
    public String getVerifyEmail() { return _sVerifyEmail; }

    public void setVerifyCellPhoneNo(final String verifyCellPhoneNo) { _sVerifyCellPhoneNo = verifyCellPhoneNo; }
    public String getVerifyCellPhoneNo() { return _sVerifyCellPhoneNo; }

    /**
     * salt = username + salt.
     */
    @Transient
    public String getCredentialsSalt() { return _sUsername + _sSalt; }

    /**
     * default constructor.
     */
    public LoginUser() {
        super();
    }

    /**
     * constructor.
     * @param user  username
     * @param pass  password
     */
    public LoginUser(final String user, final String pass) {
        super();
        _sUsername = user;
        _sPassword = pass;
    }

    /**
     * to string.
     *
     * @return string
     */
    public String toString() {
        String base = super.toString();

        StringBuilder sb = new StringBuilder();
        sb.append("username=\t\t\t").append(_sUsername).append("\n");
        sb.append("password=\t\t\t").append(_sPassword).append("\n");
        sb.append("salt=\t\t\t").append(_sSalt).append("\n");
        sb.append("locked=\t\t\t").append(_bLocked).append("\n");
        sb.append("verifyEmail=\t\t\t").append(_sVerifyEmail).append("\n");
        sb.append("verifyCellPhoneNo=\t\t\t").append(_sVerifyCellPhoneNo).append("\n");
        sb.append(base);

        return sb.toString();
    }
} // END: LoginUser

/**
 * password helper.
 *
 * @author camry
 * @version
 */
class PasswordHelper {
    private final static String algorithmName = "md5";
    private final static int hashIterations = 2;
    private final static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    public static void encryptPassword(final LoginUser user) {
        //user.setSalt(randomNumberGenerator.nextBytes().toHex());
        user.setSalt("123");

        String newPassword = new SimpleHash(
            algorithmName,
            user.getPassword(),
            ByteSource.Util.bytes(user.getCredentialsSalt()),
            hashIterations).toHex();

        user.setPassword(newPassword);
    }
}
///:~
