package org.codehaus.plexus.ldapserver.server.backend;



/**
 * BackendMemory is a simple backend that does not persist data beyond the life
 * of the JVM.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.btree.BTree;
import org.codehaus.plexus.ldapserver.ldapv3.Filter;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum;
import org.codehaus.plexus.ldapserver.ldapv3.ModifyRequestSeqOfSeqEnum;
import org.codehaus.plexus.ldapserver.ldapv3.SearchRequestEnum;
import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.EntryChange;
import org.codehaus.plexus.ldapserver.server.EntrySet;
import org.codehaus.plexus.ldapserver.server.schema.SchemaChecker;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DirectoryException;
import org.codehaus.plexus.ldapserver.server.util.DirectorySchemaViolation;
import org.codehaus.plexus.ldapserver.server.util.InvalidDNException;
import org.codehaus.plexus.ldapserver.server.util.Logger;

import java.util.Enumeration;
import java.util.Vector;

public class BackendMemory implements Backend
{

    private BTree datastore = null;
    private BTree idtodnmap = null;
    private BTree indextable = null;
    private Vector equalsMatch; //= [ "cn","sn","objectclass","l","ou","o","telephonenumber","c","description","seealso"];
    private long idCounter = 0;

    public BackendMemory()
    {
        datastore = new BTree( 10 );
        idtodnmap = new BTree( 10 );
        indextable = new BTree( 10 );

        equalsMatch = new Vector();
        equalsMatch.addElement( new DirectoryString( "sn" ) );
        equalsMatch.addElement( new DirectoryString( "cn" ) );
        equalsMatch.addElement( new DirectoryString( "objectclass" ) );
        equalsMatch.addElement( new DirectoryString( "l" ) );
        equalsMatch.addElement( new DirectoryString( "o" ) );
        equalsMatch.addElement( new DirectoryString( "ou" ) );
        equalsMatch.addElement( new DirectoryString( "telephonenumber" ) );
        equalsMatch.addElement( new DirectoryString( "description" ) );
        equalsMatch.addElement( new DirectoryString( "seealso" ) );

        Enumeration equalsMatchEnum = equalsMatch.elements();
        while ( equalsMatchEnum.hasMoreElements() )
        {
            indextable.put( (DirectoryString) equalsMatchEnum.nextElement(), new BTree( 10 ) );
        }
    }

    public LDAPResultEnum add( Entry entry )
    {
        if ( this.datastore.get( entry.getName() ) != null )
        {
            return new LDAPResultEnum( 68 );
        }
        this.idCounter++;
        entry.setID( idCounter );
        this.idtodnmap.put( new Long( idCounter ), entry.getName() );
        this.datastore.put( entry.getName(), entry );
        index( entry );
        return new LDAPResultEnum( 0 );
    }

    public LDAPResultEnum delete( DirectoryString name )
    {
        this.datastore.remove( name );
        return new LDAPResultEnum( 0 );
    }

    private Vector evaluateFilter( Filter currentFilter, DirectoryString base, int scope )
    {

        Vector matchThisFilter = new Vector();

        switch ( currentFilter.choiceId )
        {
            case Filter.EQUALITYMATCH_CID:
                String matchType = new String( currentFilter.equalityMatch.attributeDesc );
                String matchVal = new String( currentFilter.equalityMatch.assertionValue );
                Logger.getInstance().log( Logger.LOG_DEBUG, "Filter: " + matchType + "=" + matchVal );
                Logger.getInstance().log( Logger.LOG_DEBUG, "Base: " + base );
                matchThisFilter = searchExact( base, scope, matchType, matchVal, true );
                break;

            case Filter.PRESENT_CID:
                matchType = new String( currentFilter.present );
                matchThisFilter = searchExact( base, scope, matchType, null, false );
                break;

            case Filter.AND_CID:
                boolean firstAnd = true;
                for ( Enumeration andEnum = currentFilter.and.elements(); andEnum.hasMoreElements(); )
                {
                    Vector matched = evaluateFilter( (Filter) andEnum.nextElement(), base, scope );
                    if ( firstAnd )
                    {
                        firstAnd = false;
                        for ( Enumeration matchEnum = matched.elements(); matchEnum.hasMoreElements(); )
                        {
                            matchThisFilter.addElement( matchEnum.nextElement() );
                        }
                    }
                    else
                    {
                        Vector inBoth = new Vector();
                        for ( Enumeration matchEnum = matched.elements(); matchEnum.hasMoreElements(); )
                        {
                            Object matchObj = matchEnum.nextElement();
                            if ( matchThisFilter.contains( matchObj ) )
                            {
                                inBoth.addElement( matchObj );
                            }
                        }
                        matchThisFilter = inBoth;
                    }
                }
                break;

            case Filter.OR_CID:
                for ( Enumeration orEnum = currentFilter.or.elements(); orEnum.hasMoreElements(); )
                {
                    Vector matched = evaluateFilter( (Filter) orEnum.nextElement(), base, scope );
                    for ( Enumeration matchEnum = matched.elements(); matchEnum.hasMoreElements(); )
                    {
                        matchThisFilter.addElement( matchEnum.nextElement() );
                    }
                }
                break;

            case Filter.NOT_CID:
                // Need to fix this...Not a correct implementation
                //Vector matched = evaluateFilter(currentFilter.not,base,scope);
                //matchThisFilter.removeAll(matched);

                break;


        }
        return matchThisFilter;
    }

    public EntrySet get( DirectoryString base, int scope, Filter filter,
                         boolean typesOnly, Vector attributes )
    {

        Vector matchingEntries = evaluateFilter( filter, base, scope );

        return (EntrySet) new GenericEntrySet( this, matchingEntries );
    }

    public Entry getByDN( DirectoryString dn ) throws DirectoryException
    {
        return (Entry) this.datastore.get( dn );
    }

    public Entry getByID( Long id )
    {
        DirectoryString dn = (DirectoryString) this.idtodnmap.get( id );
        return (Entry) this.datastore.get( dn );
    }

    private void index( Entry entry )
    {
        Long entryId = new Long( entry.getID() );
        Enumeration indexes = equalsMatch.elements();
        while ( indexes.hasMoreElements() )
        {
            DirectoryString indexKey = (DirectoryString) indexes.nextElement();
            if ( entry.get( indexKey ) != null )
            {
                BTree attrIndex = (BTree) indextable.get( indexKey );
                Vector attrValVec = (Vector) entry.get( indexKey );
                Enumeration attrValEnum = attrValVec.elements();
                while ( attrValEnum.hasMoreElements() )
                {
                    String oneValue = ( (DirectoryString) attrValEnum.nextElement() ).normalize();
                    Vector eids = (Vector) attrIndex.get( oneValue );
                    if ( eids != null )
                    {
                        //if (!eids.contains(entryId)) {
                        eids.addElement( entryId );
                        //}
                    }
                    else
                    {
                        eids = new Vector();
                        eids.addElement( entryId );
                        attrIndex.put( oneValue, eids );
                    }
                }
            }
        }
    }

    private void index( Entry entry, Vector changes )
    {
        Long entryId = new Long( entry.getID() );

        Enumeration changeEnum = changes.elements();
        while ( changeEnum.hasMoreElements() )
        {
            EntryChange change = (EntryChange) changeEnum.nextElement();
            int changeType = change.getModType();
            DirectoryString attr = change.getAttr();
            Vector vals = change.getValues();

            if ( equalsMatch.contains( attr ) )
            {
                BTree attrIndex = (BTree) indextable.get( attr );

                if ( changeType == ModifyRequestSeqOfSeqEnum.ADD )
                {

                    Vector oldvals = (Vector) entry.get( attr );
                    if ( oldvals == null )
                    {
                        oldvals = new Vector();
                    }


                    for ( Enumeration enumVals = vals.elements(); enumVals.hasMoreElements(); )
                    {
                        String oneVal = ( (DirectoryString) enumVals.nextElement() ).normalize();
                        if ( !oldvals.contains( oneVal ) )
                        {

                            Vector eids = (Vector) attrIndex.get( oneVal );
                            if ( eids != null )
                            {
                                if ( !eids.contains( entryId ) )
                                {
                                    eids.addElement( entryId );
                                }
                            }
                            else
                            {
                                eids = new Vector();
                                eids.addElement( entryId );
                                attrIndex.put( oneVal, eids );
                            }
                        }
                    }
                }


                if ( changeType == ModifyRequestSeqOfSeqEnum.DELETE )
                {
                    Vector oldvals = (Vector) entry.get( attr );
                    if ( oldvals == null )
                    {
                        oldvals = new Vector();
                    }

                    for ( Enumeration enumVals = vals.elements(); enumVals.hasMoreElements(); )
                    {
                        String oneVal = ( (DirectoryString) enumVals.nextElement() ).normalize();
                        if ( oldvals.contains( oneVal ) )
                        {
                            Vector eids = (Vector) attrIndex.get( oneVal );
                            if ( eids != null )
                            {
                                if ( eids.contains( entryId ) )
                                {
                                    eids.removeElement( entryId );
                                }
                            }
                        }
                    }
                }

                if ( changeType == ModifyRequestSeqOfSeqEnum.REPLACE )
                {
                    if ( entry.containsKey( attr ) )
                    {
                        Vector oldvals = (Vector) entry.get( attr );

                        for ( Enumeration enumVals = oldvals.elements(); enumVals.hasMoreElements(); )
                        {
                            String oneVal = ( (DirectoryString) enumVals.nextElement() ).normalize();
                            Vector eids = (Vector) attrIndex.get( oneVal );
                            if ( eids != null )
                            {
                                eids.removeElement( entryId );
                            }
                        }
                    }
                    for ( Enumeration enumVals = vals.elements(); enumVals.hasMoreElements(); )
                    {
                        String oneVal = ( (DirectoryString) enumVals.nextElement() ).normalize();
                        Vector eids = (Vector) attrIndex.get( oneVal );
                        if ( eids != null )
                        {
                            if ( !eids.contains( entryId ) )
                            {
                                eids.addElement( entryId );
                            }
                        }
                        else
                        {
                            eids = new Vector();
                            eids.addElement( entryId );
                            attrIndex.put( oneVal, eids );
                        }
                    }
                }
            }
        }
    }

    public void modify( DirectoryString name, Vector changeEntries ) throws DirectorySchemaViolation
    {
        Entry entry = (Entry) datastore.get( name );
        if ( entry != null )
        {
            Entry current = (Entry) entry.clone();
            Enumeration changeEnum = changeEntries.elements();
            while ( changeEnum.hasMoreElements() )
            {
                oneChange( current, (EntryChange) changeEnum.nextElement() );
            }
            SchemaChecker.getInstance().checkEntry( current );
            index( entry, changeEntries );
            datastore.put( name, current );
        }
    }

    public void oneChange( Entry current, EntryChange change )
    {

        int changeType = change.getModType();
        DirectoryString attr = change.getAttr();
        Vector vals = change.getValues();

        if ( changeType == ModifyRequestSeqOfSeqEnum.ADD )
        {
            Vector oldvals = null;
            if ( !current.containsKey( attr ) )
            {
                oldvals = new Vector();
            }
            else
            {
                oldvals = (Vector) current.get( attr );
            }
            for ( Enumeration enumVals = vals.elements(); enumVals.hasMoreElements(); )
            {
                Object oneVal = enumVals.nextElement();
                if ( !oldvals.contains( oneVal ) )
                {
                    oldvals.addElement( oneVal );
                }
            }
            current.put( attr, oldvals );
        }

        if ( changeType == ModifyRequestSeqOfSeqEnum.DELETE )
        {
            Vector oldvals = null;
            if ( !current.containsKey( attr ) )
            {
                oldvals = new Vector();
            }
            else
            {
                oldvals = (Vector) current.get( attr );
            }
            for ( Enumeration enumVals = vals.elements(); enumVals.hasMoreElements(); )
            {
                Object oneVal = enumVals.nextElement();
                if ( oldvals.contains( oneVal ) )
                {
                    oldvals.removeElement( oneVal );
                }
            }
            if ( oldvals.size() > 0 )
            {
                current.put( attr, oldvals );
            }
            else
            {
                current.remove( attr );
            }
        }

        if ( changeType == ModifyRequestSeqOfSeqEnum.REPLACE )
        {
            current.put( attr, vals );
        }
    }

    public LDAPResultEnum rename( DirectoryString oldname, DirectoryString newname )
    {
        Entry entry = (Entry) this.datastore.get( oldname );
        try
        {
            entry.setName( newname );
        }
        catch ( InvalidDNException ide )
        {
            return new LDAPResultEnum( ide.getLDAPErrorCode() );
        }
        this.datastore.put( entry.getName(), entry );
        this.datastore.remove( oldname );
        return new LDAPResultEnum( 0 );
    }

    private Vector searchExact( DirectoryString base, int scope, String attr, String value, boolean matchValue )
    {
        Vector results = new Vector();
        DirectoryString cisAttr = new DirectoryString( attr );
        DirectoryString cisValue = null;
        if ( value != null )
        {
            cisValue = new DirectoryString( value );
        }

        if ( scope == SearchRequestEnum.BASEOBJECT )
        {
            Entry current = (Entry) this.datastore.get( base );
            if ( current != null )
            {
                if ( current.containsKey( cisAttr ) )
                {
                    Vector values = (Vector) current.get( attr );
                    if ( matchValue == false || values.contains( cisValue ) )
                    {
                        results.addElement( new Long( current.getID() ) );
                    }
                }
            }
            return results;
        }

        if ( indextable.containsKey( cisAttr ) && matchValue )
        {
            BTree attrIndex = (BTree) this.indextable.get( cisAttr );
            if ( value == null || !attrIndex.containsKey( cisValue.normalize() ) )
            {
                return results;
            }
            Vector eids = (Vector) attrIndex.get( cisValue.normalize() );
            for ( Enumeration enumEntries = eids.elements(); enumEntries.hasMoreElements(); )
            {
                Long currentEid = (Long) enumEntries.nextElement();
                Entry current = getByID( currentEid );
                if ( ( current.getBase().endsWith( base ) || base.equals( new DirectoryString( "" ) ) ) && !current.getName().equals( base ) )
                {
                    if ( ( scope == SearchRequestEnum.WHOLESUBTREE || current.getBase().equals( base ) ) && !current.getName().equals( base ) )
                    {
                        results.addElement( currentEid );
                    }
                }
            }
            return results;
        }

        for ( Enumeration enumEntries = this.datastore.values(); enumEntries.hasMoreElements(); )
        {
            Entry current = (Entry) enumEntries.nextElement();
            if ( ( current.getBase().endsWith( base ) || base.equals( new DirectoryString( "" ) ) ) && !current.getName().equals( base ) )
            {
                if ( ( scope == SearchRequestEnum.WHOLESUBTREE || current.getBase().equals( base ) ) && !current.getName().equals( base ) )
                {
                    if ( current.containsKey( cisAttr ) )
                    {
                        Vector values = (Vector) current.get( cisAttr );
                        if ( matchValue == false || values.contains( cisValue ) )
                        {
                            results.addElement( new Long( current.getID() ) );
                        }
                    }
                }
            }
        }
        return results;
    }
}
