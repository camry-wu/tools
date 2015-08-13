/*
 * -----------------------------------------------------------
 * file name  : BaseObj.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Mon 27 Jul 2015 11:42:39 AM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.common.domain;

import java.util.Date;

// import javax.persistence.Access;
// import javax.persistence.AccessType;
// import javax.persistence.AssociationOverride;
// import javax.persistence.AssociationOverrides;
// import javax.persistence.AttributeConverter;
// import javax.persistence.AttributeNode;
// import javax.persistence.AttributeOverride;
// import javax.persistence.AttributeOverrides;
// import javax.persistence.Basic;
// import javax.persistence.Cache;
// import javax.persistence.Cacheable;
// import javax.persistence.CacheRetrieveMode;
// import javax.persistence.CacheStoreMode;
// import javax.persistence.CascadeType;
// import javax.persistence.CollectionTable;
import javax.persistence.Column;
// import javax.persistence.ColumnResult;
// import javax.persistence.ConstraintMode;
// import javax.persistence.ConstructorResult;
// import javax.persistence.Convert;
// import javax.persistence.Converter;
// import javax.persistence.Converts;
// import javax.persistence.DiscriminatorColumn;
// import javax.persistence.DiscriminatorType;
// import javax.persistence.DiscriminatorValue;
// import javax.persistence.ElementCollection;
// import javax.persistence.Embeddable;
// import javax.persistence.Embedded;
// import javax.persistence.EmbeddedId;
// import javax.persistence.Entity;
// import javax.persistence.EntityExistsException;
// import javax.persistence.EntityGraph;
// import javax.persistence.EntityListeners;
// import javax.persistence.EntityManager;
// import javax.persistence.EntityManagerFactory;
// import javax.persistence.EntityNotFoundException;
// import javax.persistence.EntityResult;
// import javax.persistence.EntityTransaction;
// import javax.persistence.Enumerated;
// import javax.persistence.EnumType;
// import javax.persistence.ExcludeDefaultListeners;
// import javax.persistence.ExcludeSuperclassListeners;
// import javax.persistence.FetchType;
// import javax.persistence.FieldResult;
// import javax.persistence.FlushModeType;
// import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
// import javax.persistence.IdClass;
// import javax.persistence.Index;
// import javax.persistence.Inheritance;
// import javax.persistence.InheritanceType;
// import javax.persistence.JoinColumn;
// import javax.persistence.JoinColumns;
// import javax.persistence.JoinTable;
// import javax.persistence.Lob;
// import javax.persistence.LockModeType;
// import javax.persistence.LockTimeoutException;
// import javax.persistence.ManyToMany;
// import javax.persistence.ManyToOne;
// import javax.persistence.MapKey;
// import javax.persistence.MapKeyClass;
// import javax.persistence.MapKeyColumn;
// import javax.persistence.MapKeyEnumerated;
// import javax.persistence.MapKeyJoinColumn;
// import javax.persistence.MapKeyJoinColumns;
// import javax.persistence.MapKeyTemporal;
import javax.persistence.MappedSuperclass;
// import javax.persistence.MapsId;
// import javax.persistence.NamedAttributeNode;
// import javax.persistence.NamedEntityGraph;
// import javax.persistence.NamedEntityGraphs;
// import javax.persistence.NamedNativeQueries;
// import javax.persistence.NamedNativeQuery;
// import javax.persistence.NamedQueries;
// import javax.persistence.NamedQuery;
// import javax.persistence.NamedStoredProcedureQueries;
// import javax.persistence.NamedStoredProcedureQuery;
// import javax.persistence.NamedSubgraph;
// import javax.persistence.NonUniqueResultException;
// import javax.persistence.NoResultException;
// import javax.persistence.OneToMany;
// import javax.persistence.OneToOne;
// import javax.persistence.OptimisticLockException;
// import javax.persistence.OrderBy;
// import javax.persistence.OrderColumn;
// import javax.persistence.Parameter;
// import javax.persistence.ParameterMode;
// import javax.persistence.Persistence;
// import javax.persistence.PersistenceContext;
// import javax.persistence.PersistenceContexts;
// import javax.persistence.PersistenceContextType;
// import javax.persistence.PersistenceException;
// import javax.persistence.PersistenceProperty;
// import javax.persistence.PersistenceUnit;
// import javax.persistence.PersistenceUnits;
// import javax.persistence.PersistenceUnitUtil;
// import javax.persistence.PersistenceUtil;
// import javax.persistence.PessimisticLockException;
// import javax.persistence.PessimisticLockScope;
// import javax.persistence.PostLoad;
// import javax.persistence.PostPersist;
// import javax.persistence.PostRemove;
// import javax.persistence.PostUpdate;
// import javax.persistence.PrePersist;
// import javax.persistence.PreRemove;
// import javax.persistence.PreUpdate;
// import javax.persistence.PrimaryKeyJoinColumn;
// import javax.persistence.PrimaryKeyJoinColumns;
// import javax.persistence.Query;
// import javax.persistence.QueryHint;
// import javax.persistence.QueryTimeoutException;
// import javax.persistence.RollbackException;
// import javax.persistence.SecondaryTable;
// import javax.persistence.SecondaryTables;
// import javax.persistence.SequenceGenerator;
// import javax.persistence.SharedCacheMode;
// import javax.persistence.SqlResultSetMapping;
// import javax.persistence.SqlResultSetMappings;
// import javax.persistence.StoredProcedureParameter;
// import javax.persistence.StoredProcedureQuery;
// import javax.persistence.Subgraph;
// import javax.persistence.SynchronizationType;
// import javax.persistence.Table;
import javax.persistence.TableGenerator;
// import javax.persistence.Temporal;
// import javax.persistence.TemporalType;
// import javax.persistence.TransactionRequiredException;
import javax.persistence.Transient;
// import javax.persistence.Tuple;
// import javax.persistence.TupleElement;
// import javax.persistence.TypedQuery;
// import javax.persistence.UniqueConstraint;
// import javax.persistence.ValidationMode;
import javax.persistence.Version;

/**
 * Base Obj.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
@MappedSuperclass
public abstract class BaseObj {

    /**
     * version, use for optimize lock.
     */
    private int _iVersion;

    /**
     * last update time.
     */
    private Date _lastUpdate;

    /**
     * last update user.
     */
    private String _sLastUpdater;

    /**
     * if true is active.
     */
    private boolean _bIsActive = true;

    // getter and setter
    public abstract void setOid(final long oid);
    @Transient
    public abstract long getOid();

    public void setVersion(final int version) { _iVersion = version; }
    @Version
    public int getVersion() { return _iVersion; }

    public void setLastUpdate(final Date lastUpdate) { _lastUpdate = lastUpdate; }
    public Date getLastUpdate() { return _lastUpdate; }

    public void setLastUpdater(final String lastUpdater) { _sLastUpdater = lastUpdater; }
    @Column(name="LastUpdater", length=255)
    public String getLastUpdater() { return _sLastUpdater; }

    public void setIsActive(final boolean isActive) { _bIsActive = isActive; }
    public boolean getIsActive() { return _bIsActive; }

    /**
     * default constructor.
     */
    public BaseObj() {
        super();
    }

    /**
     * to string.
     *
     * @return string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("oid=\t").append(getOid()).append("\n");
        sb.append("version=\t").append(_iVersion).append("\n");
        sb.append("lastUpdate=\t").append(_lastUpdate).append("\n");
        sb.append("lastUpdater=\t").append(_sLastUpdater).append("\n");
        sb.append("isActive=\t").append(_bIsActive).append("\n");

        return sb.toString();
    }

    /**
     * base equals: the same class and oid.
     *
     * @param obj the other object
     * @return
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        String thisClzName = getClass().getName();
        String otherClzName = obj.getClass().getName();

        if (thisClzName.equals(otherClzName)) {
            long ooid = ((BaseObj) obj).getOid();
            return getOid() == ooid;
        }

        return false;
    }
} // END: BaseObj
///:~
