package org.codehaus.plexus.ldapserver.server.backend;

/*
The JavaLDAP Server
Copyright (C) 2000  Clayton Donley (donley@linc-dev.com) - All Rights Reserved

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/

/**
 * BackendJDBC is a Backend implementation that uses JDBC to manage information
 * in a particular set of tables within an RDBMS. Note that BackendDB is the one that
 * will eventually do LDAP-to-RDBMS mapping. This backend expects and/or creates a
 * particular set of tables that map easily to the LDAP protocol and information
 * model.
 *
 * @author <a href="mailto:clayton.donley@octetstring.com">Clayton Donley</a>
 */

import org.codehaus.plexus.ldapserver.ldapv3.Filter;
import org.codehaus.plexus.ldapserver.ldapv3.LDAPResultEnum;
import org.codehaus.plexus.ldapserver.ldapv3.ModifyRequestSeqOfSeqEnum;
import org.codehaus.plexus.ldapserver.ldapv3.SearchRequestEnum;
import org.codehaus.plexus.ldapserver.ldapv3.SubstringFilterSeqOfChoice;
import org.codehaus.plexus.ldapserver.server.Entry;
import org.codehaus.plexus.ldapserver.server.EntryChange;
import org.codehaus.plexus.ldapserver.server.EntrySet;
import org.codehaus.plexus.ldapserver.server.schema.SchemaChecker;
import org.codehaus.plexus.ldapserver.server.syntax.DirectoryString;
import org.codehaus.plexus.ldapserver.server.util.DirectoryException;
import org.codehaus.plexus.ldapserver.server.util.DirectorySchemaViolation;
import org.codehaus.plexus.ldapserver.server.util.Logger;
import org.codehaus.plexus.ldapserver.server.util.ServerConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;


public class BackendJDBC implements Backend
{

    Vector exactIndexes = null;
    long idCounter = 0;

    public BackendJDBC()
    {


        try
        {
            Class.forName( (String) ServerConfig.getInstance().get( ServerConfig.JAVALDAP_BACKENDJDBC_DBDRIVER ) ).newInstance();
        }
        catch ( Exception e )
        {
            Logger.getInstance().log( Logger.LOG_NORMAL, "Error: " + e.getMessage() );
        }

        // The indexes we will create. Hardcoded at the moment.
        this.exactIndexes = new Vector();
        this.exactIndexes.addElement( new DirectoryString( "cn" ) );
        this.exactIndexes.addElement( new DirectoryString( "sn" ) );
        this.exactIndexes.addElement( new DirectoryString( "l" ) );
        this.exactIndexes.addElement( new DirectoryString( "ou" ) );
        this.exactIndexes.addElement( new DirectoryString( "o" ) );
        this.exactIndexes.addElement( new DirectoryString( "objectclass" ) );
        this.exactIndexes.addElement( new DirectoryString( "description" ) );
        this.exactIndexes.addElement( new DirectoryString( "seealso" ) );

        /*
        Hashtable transTable = new Hashtable();

        String[] sn = {"person","first_name"};
        transTable.put("sn",sn);

        String[] givenName = {"person","last_name"};
        transTable.put("givenName",givenName);

        String[] employeeNumber = {"person","employee_id"};
        transTable.put("employeeNumber",employeeNumber);

        String[] postalAddress = {"address","address"};
        transTable.put("postalAddress", postalAddress);

        String[] l = {"address","city"};
        transTable.put("l",l);

        String[] st = {"address","state"};
        transTable.put("st",st);

        String[] zip = {"address","zip"};
        transTable.put("zip",zip);
        */

    }

    public LDAPResultEnum add( Entry entry )
    {

        Statement s = null;
        ResultSet rs = null;

        Connection dbcon = null;
        try
        {
            dbcon = (Connection) BackendJDBCConnPool.getInstance().checkOut();
            s = dbcon.createStatement();

            PreparedStatement tmpPS = dbcon.prepareStatement( "SELECT entryid FROM entry where dn = ?" );
            tmpPS.setString( 1, entry.getName().toString() );
            rs = tmpPS.executeQuery();

            if ( rs.next() )
            {
                // Entry with this name already exists
                BackendJDBCConnPool.getInstance().checkIn( dbcon );
                return new LDAPResultEnum( 68 );
            }
        }
        catch ( SQLException se )
        {
            se.printStackTrace();
            initialize();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            if ( dbcon != null )
            {
                BackendJDBCConnPool.getInstance().checkIn( dbcon );
                return new LDAPResultEnum( 53 );
            }
        }

        try
        {

            rs = s.executeQuery( "SELECT value FROM keyTable WHERE keyValue = 'entryCount'" );
            rs.next();
            idCounter = new Long( rs.getString( 1 ) ).longValue();
            idCounter++;
            entry.setID( idCounter );
            s.execute( "UPDATE keyTable SET value = '" + idCounter + "' WHERE keyValue = 'entryCount'" );

            byte[] byteEntry = entry.getAsByteArray();


            PreparedStatement ps = dbcon.prepareStatement( "INSERT into entry VALUES(?,?,?,?,?,?,?,?)" );
            ps.setLong( 1, idCounter );
            ps.setString( 2, entry.getName().toString() );
            ps.setString( 3, entry.getBase().toString() );
            ps.setBytes( 4, byteEntry );
            ps.setString( 5, "cn=Root" );
            ps.setString( 6, "cn=Root" );
            ps.setString( 7, "timestampHere" );
            ps.setString( 8, "ModifyTimestampHere" );
            ps.execute();


            for ( Enumeration indexEnum = exactIndexes.elements(); indexEnum.hasMoreElements(); )
            {
                DirectoryString attr = (DirectoryString) indexEnum.nextElement();
                if ( entry.containsKey( attr ) )
                {
                    for ( Enumeration valsEnum = ( (Vector) entry.get( attr ) ).elements(); valsEnum.hasMoreElements(); )
                    {
                        ps = dbcon.prepareStatement( "INSERT INTO " + attr + " VALUES (?,?)" );
                        ps.setLong( 1, idCounter );
                        ps.setString( 2, ( (DirectoryString) valsEnum.nextElement() ).normalize() );
                        ps.execute();
                    }
                }
            }
            dbcon.commit();
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
        }
        catch ( SQLException se )
        {
            se.printStackTrace();
            try
            {
                dbcon.rollback();
            }
            catch ( SQLException sqle )
            {
                sqle.printStackTrace();
            }
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
            return new LDAPResultEnum( 53 );
        }
        return new LDAPResultEnum( 0 );
    }

    private String constructFilter( Filter currentFilter )
    {


        switch ( currentFilter.choiceId )
        {
            case Filter.EQUALITYMATCH_CID:
                DirectoryString matchType = new DirectoryString( currentFilter.equalityMatch.attributeDesc );
                DirectoryString matchVal = new DirectoryString( currentFilter.equalityMatch.assertionValue );
                if ( exactIndexes.contains( matchType ) )
                {
                    //	return new String("SELECT entryid FROM " + matchType + " WHERE UPPER(value) = UPPER('" + matchVal + "')");
                    return new String( "SELECT " + matchType + ".entryid FROM " + matchType + " WHERE " + matchType + ".value = '" + matchVal.normalize() + "'" );
                }
                break;

            case Filter.PRESENT_CID:
                matchType = new DirectoryString( currentFilter.present );
                if ( exactIndexes.contains( matchType ) )
                {
                    return new String( "SELECT " + matchType + ".entryid FROM " + matchType );
                }
                break;

            case Filter.SUBSTRINGS_CID:
                matchType = new DirectoryString( currentFilter.substrings.type );
                String subfilter = new String();
                for ( Enumeration substrEnum = currentFilter.substrings.substrings.elements(); substrEnum.hasMoreElements(); )
                {
                    SubstringFilterSeqOfChoice oneSubFilter = (SubstringFilterSeqOfChoice) substrEnum.nextElement();
                    if ( oneSubFilter.choiceId == oneSubFilter.INITIAL_CID )
                    {
                        subfilter = subfilter.concat( new String( oneSubFilter.initial ) + "%" );
                    }
                    else if ( oneSubFilter.choiceId == oneSubFilter.ANY_CID )
                    {
                        if ( subfilter.length() == 0 )
                        {
                            subfilter = subfilter.concat( "%" );
                        }
                        subfilter = subfilter.concat( new String( oneSubFilter.any ) + "%" );
                    }
                    else if ( oneSubFilter.choiceId == oneSubFilter.FINAL1_CID )
                    {
                        if ( subfilter.length() == 0 )
                        {
                            subfilter = subfilter.concat( "%" );
                        }
                        subfilter = subfilter.concat( new String( oneSubFilter.final1 ) );
                    }
                }
                if ( exactIndexes.contains( matchType ) )
                {
                    // return new String("SELECT entryid FROM " + matchType + " WHERE UPPER(value) LIKE UPPER('" + subfilter + "')");
                    return new String( "SELECT " + matchType + ".entryid FROM " + matchType + " WHERE " + matchType + ".value LIKE '" + new DirectoryString( subfilter ).normalize() + "'" );

                }
                break;
            case Filter.AND_CID:
                String strFilt = new String();

                for ( Enumeration andEnum = currentFilter.and.elements(); andEnum.hasMoreElements(); )
                {

                    //strFilt = strFilt.concat("(" + constructFilter((Filter)andEnum.nextElement()) + ")");
                    strFilt = strFilt.concat( constructFilter( (Filter) andEnum.nextElement() ) );
                    if ( andEnum.hasMoreElements() )
                    {
                        strFilt = strFilt.concat( " INTERSECT " );
                    }
                }
                return strFilt;

            case Filter.OR_CID:
                strFilt = new String();

                for ( Enumeration orEnum = currentFilter.or.elements(); orEnum.hasMoreElements(); )
                {

                    //strFilt = strFilt.concat("(" + constructFilter((Filter)orEnum.nextElement()) + ")");
                    strFilt = strFilt.concat( constructFilter( (Filter) orEnum.nextElement() ) );
                    if ( orEnum.hasMoreElements() )
                    {
                        strFilt = strFilt.concat( " UNION " );
                    }
                }
                return strFilt;

            case Filter.NOT_CID:
                // Need to fix this...Not a correct implementation
                //Vector matched = evaluateFilter(currentFilter.not,base,scope);
                //matchThisFilter.removeAll(matched);

                break;


        }
        return new String();
    }

    public LDAPResultEnum delete( DirectoryString name )
    {
        Connection dbcon = null;
        try
        {
            dbcon = (Connection) BackendJDBCConnPool.getInstance().checkOut();
            Statement s = dbcon.createStatement();
            ResultSet rs = s.executeQuery( "SELECT entryid FROM ENTRY WHERE dn = '" + name + "'" );
            if ( rs.next() )
            {
                long entryid = rs.getLong( 1 );
                s.execute( "DELETE FROM entry WHERE entryid = " + entryid + "" );
                Enumeration indexEnum = exactIndexes.elements();
                while ( indexEnum.hasMoreElements() )
                {
                    s.execute( "DELETE FROM " + indexEnum.nextElement() + " WHERE entryid = " + entryid );
                }
            }
            dbcon.commit();
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            try
            {
                dbcon.rollback();
            }
            catch ( SQLException sqle )
            {
                sqle.printStackTrace();
            }
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
        }
        return new LDAPResultEnum( 0 );
    }

    private EntrySet evaluateFilter( Filter currentFilter, DirectoryString base, int scope )
    {
        EntrySet results = null;
        String filtStr = constructFilter( currentFilter );
        String scopeQuery = null;

        if ( scope == SearchRequestEnum.WHOLESUBTREE )
        {
            scopeQuery = "entry.base LIKE '%" + base + "'";
        }
        else if ( scope == SearchRequestEnum.SINGLELEVEL )
        {
            scopeQuery = "entry.base = '" + base + "'";
        }
        else
        {
            scopeQuery = "entry.dn = '" + base + "'";
        }

        //String fullQuery = new String("SELECT entry.entrydata FROM entry WHERE entry.entryid IN (SELECT DISTINCT entry.entryid FROM entry WHERE " + scopeQuery +
        //	" AND entry.entryid IN (" + filtStr + "))");


        //String fullQuery = new String("SELECT entry.entrydata FROM entry WHERE entry.entryid IN (" +
        //	filtStr + ") AND " + scopeQuery);
        String fullQuery = new String( "SELECT entry.entryid FROM entry WHERE entry.entryid in (" + filtStr + ") AND " + scopeQuery );

        Connection dbcon = null;
        ResultSet rs = null;
        Statement s = null;
        try
        {
            dbcon = (Connection) BackendJDBCConnPool.getInstance().checkOut();
            s = dbcon.createStatement();
            rs = s.executeQuery( fullQuery );
            //results = new JDBCEntrySet(this,rs,dbcon);
            Vector entryids = new Vector();
            while ( rs.next() )
            {
                entryids.addElement( new Long( rs.getLong( 1 ) ) );
            }
            results = new GenericEntrySet( this, entryids );
            rs.close();
            s.close();
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            try
            {
                s.close();
                rs.close();
            }
            catch ( SQLException se )
            {
            }
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
            results = new GenericEntrySet();
        }
        return results;
    }

    public EntrySet get( DirectoryString base, int scope, Filter filter,
                         boolean typesOnly, Vector attributes )
    {

        return (EntrySet) evaluateFilter( filter, base, scope );
    }

    public Entry getByDN( DirectoryString dn ) throws DirectoryException
    {
        Entry entry = null;
        Connection dbcon = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try
        {
            dbcon = (Connection) BackendJDBCConnPool.getInstance().checkOut();
            ps = dbcon.prepareStatement( "SELECT entryData from entry where dn = ?" );
            ps.setString( 1, dn.toString() );

            rs = ps.executeQuery();
            rs.next();
            byte[] entryBytes = rs.getBytes( 1 );
            entry = new Entry( entryBytes );
            rs = null;
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
        }
        catch ( Exception ioe )
        {
            ioe.printStackTrace();
            rs = null;
            ps = null;
            if ( dbcon != null )
            {
                BackendJDBCConnPool.getInstance().checkIn( dbcon );
            }
        }
        return entry;
    }

    public Entry getByID( Long id )
    {
        Entry entry = null;
        Connection dbcon = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            dbcon = (Connection) BackendJDBCConnPool.getInstance().checkOut();
            ps = dbcon.prepareStatement( "SELECT entrydata FROM entry WHERE entryid = ?" );
            ps.setLong( 1, id.longValue() );
            rs = ps.executeQuery();

            rs.next();

            byte[] entryBytes = rs.getBytes( 1 );
            entry = new Entry( entryBytes );
            rs.close();
            ps.close();
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
        }
        catch ( Exception ioe )
        {
            ioe.printStackTrace();
            rs = null;
            ps = null;
            if ( dbcon != null )
            {
                BackendJDBCConnPool.getInstance().checkIn( dbcon );
            }
            entry = new Entry();
        }
        return entry;
    }

    void initialize()
    {


        String entrytable = ServerConfig.getInstance().get( ServerConfig.JAVALDAP_BACKENDJDBC_CREATETABLE ) +
            " entry (entryid INT, dn VARCHAR(255), base VARCHAR(255), entrydata " +
            ServerConfig.getInstance().get( ServerConfig.JAVALDAP_BACKENDJDBC_LONGVARCHAR ) +
            ", creator VARCHAR(255), modifier VARCHAR(255), createstamp VARCHAR(32), modifystamp VARCHAR(32))";

        Connection dbcon = null;
        try
        {
            dbcon = (Connection) BackendJDBCConnPool.getInstance().checkOut();
            Statement s = dbcon.createStatement();
            s.execute( entrytable );
            s.execute( "CREATE INDEX entryindex ON entry (entryid, dn)" );

            s.execute( ServerConfig.getInstance().get( ServerConfig.JAVALDAP_BACKENDJDBC_CREATETABLE ) +
                       " keytable (keyvalue VARCHAR(64), value VARCHAR(128))" );
            s.execute( "INSERT INTO keytable VALUES ('entryCount','0')" );

            for ( Enumeration indexenum = exactIndexes.elements(); indexenum.hasMoreElements(); )
            {
                DirectoryString indexName = (DirectoryString) indexenum.nextElement();
                String indexcreate = ServerConfig.getInstance().get( ServerConfig.JAVALDAP_BACKENDJDBC_CREATETABLE ) +
                    " " + indexName + " (entryid int, value varchar(255))";
                s.execute( indexcreate );
                s.execute( "CREATE INDEX " + indexName + "ix ON " + indexName + " (entryid, value)" );
            }
            dbcon.commit();
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
        }
        catch ( java.sql.SQLException se )
        {
            se.printStackTrace();
            try
            {
                dbcon.rollback();
            }
            catch ( SQLException sqle )
            {
                sqle.printStackTrace();
            }
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            BackendJDBCConnPool.getInstance().checkIn( dbcon );
        }
    }

    public void modify( DirectoryString name, Vector changeEntries ) throws DirectorySchemaViolation
    {
        Entry entry = null;
        try
        {
            entry = getByDN( name );
        }
        catch ( DirectoryException de )
        {
            de.printStackTrace();
        }
        if ( entry != null )
        {
            Entry current = (Entry) entry.clone();
            Enumeration changeEnum = changeEntries.elements();
            while ( changeEnum.hasMoreElements() )
            {
                oneChange( current, (EntryChange) changeEnum.nextElement() );
            }
            SchemaChecker.getInstance().checkEntry( current );
            Connection dbcon = null;
            try
            {
                dbcon = (Connection) BackendJDBCConnPool.getInstance().checkOut();
                Statement s = dbcon.createStatement();

                // Get the current entry as a byte array
                byte[] byteEntry = current.getAsByteArray();

                // Update the entrydata for this entry
                PreparedStatement ps = dbcon.prepareStatement( "UPDATE entry SET entryData = ? WHERE entryid = ?" );
                ps.setBytes( 1, byteEntry );
                ps.setLong( 2, current.getID() );
                System.out.println( "Changing Entry: " + current.getID() );
                ps.execute();

                // Change indexes
                changeEnum = changeEntries.elements();
                while ( changeEnum.hasMoreElements() )
                {
                    EntryChange change = (EntryChange) changeEnum.nextElement();
                    int changeType = change.getModType();
                    DirectoryString attr = change.getAttr();
                    Vector vals = change.getValues();
                    if ( exactIndexes.contains( attr ) )
                    {
                        if ( changeType == ModifyRequestSeqOfSeqEnum.ADD )
                        {
                            Vector oldvals = null;
                            for ( Enumeration enumVals = vals.elements(); enumVals.hasMoreElements(); )
                            {
                                DirectoryString oneVal = (DirectoryString) enumVals.nextElement();
                                ps = dbcon.prepareStatement( "INSERT INTO " + attr + " VALUES (?,?)" );
                                ps.setLong( 1, current.getID() );
                                ps.setString( 2, oneVal.normalize() );
                                ps.execute();
                            }
                        }
                        if ( changeType == ModifyRequestSeqOfSeqEnum.DELETE )
                        {
                            for ( Enumeration enumVals = vals.elements(); enumVals.hasMoreElements(); )
                            {
                                DirectoryString oneVal = (DirectoryString) enumVals.nextElement();
                                ps = dbcon.prepareStatement( "DELETE FROM " + attr + " WHERE entryid = ? AND value = ?" );
                                ps.setLong( 1, current.getID() );
                                ps.setString( 2, oneVal.normalize() );
                                ps.execute();
                            }
                        }
                        if ( changeType == ModifyRequestSeqOfSeqEnum.REPLACE )
                        {
                            if ( entry.containsKey( attr ) )
                            {
                                ps = dbcon.prepareStatement( "DELETE FROM " + attr + " WHERE entryid = ?" );
                                ps.setLong( 1, current.getID() );
                                ps.execute();
                                for ( Enumeration valsEnum = ( (Vector) current.get( attr ) ).elements(); valsEnum.hasMoreElements(); )
                                {
                                    DirectoryString valString = (DirectoryString) valsEnum.nextElement();
                                    ps = dbcon.prepareStatement( "INSERT INTO " + attr + " VALUES (?,?)" );
                                    ps.setLong( 1, current.getID() );
                                    ps.setString( 2, valString.normalize() );
                                    ps.execute();
                                }
                            }
                        }
                    }
                }
                dbcon.commit();
                BackendJDBCConnPool.getInstance().checkIn( dbcon );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                try
                {
                    dbcon.rollback();
                }
                catch ( SQLException sqle )
                {
                    sqle.printStackTrace();
                }
                BackendJDBCConnPool.getInstance().checkIn( dbcon );
            }
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
        // Need to implement
        /* Entry entry = (Entry)this.datastore.get(oldname);
        try {
            entry.setName(newname);
        } catch (InvalidDNException ide) {
            return new LDAPResultEnum(ide.getLDAPErrorCode());
        }
        this.datastore.put(entry.getName(),entry);
        this.datastore.remove(oldname); */
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

            Entry current = null;
            try
            {
                current = getByDN( base );
            }
            catch ( DirectoryException de )
            {
                de.printStackTrace();
            }

            if ( current != null )
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
            return results;
        }

        if ( exactIndexes.contains( cisAttr ) )
        {
            Connection dbcon = null;
            try
            {
                dbcon = (Connection) BackendJDBCConnPool.getInstance().checkOut();
                Statement s = dbcon.createStatement();
                ResultSet rs = null;
                String scopeQuery = null;
                if ( scope == SearchRequestEnum.WHOLESUBTREE )
                {
                    scopeQuery = "entry.base LIKE '%" + base + "'";
                }
                else
                {
                    scopeQuery = "entry.base = '" + base + "'";
                }
                if ( matchValue == true )
                {
                    rs = s.executeQuery( "SELECT DISTINCT entry.entryid from entry," + cisAttr +
                                         " where entry.entryid = " + cisAttr + ".entryid and UPPER(value) = UPPER('" + cisValue +
                                         "') AND " + scopeQuery );
                }
                else
                {
                    rs = s.executeQuery( "SELECT DISTINCT entry.entryid from entry," + cisAttr +
                                         " where entry.entryid = " + cisAttr + ".entryid AND " + scopeQuery );
                }
                while ( rs.next() )
                {
                    Entry current = null;
                    long entryid = rs.getLong( 1 );
                    results.addElement( new Long( entryid ) );
                }
                BackendJDBCConnPool.getInstance().checkIn( dbcon );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                if ( dbcon != null )
                {
                    BackendJDBCConnPool.getInstance().checkIn( dbcon );
                }
            }
            return results;
        }

        return results;
    }

    private Vector searchSubstring( DirectoryString base, int scope, String attr, String value )
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

            Entry current = null;
            try
            {
                current = getByDN( base );
            }
            catch ( DirectoryException de )
            {
                de.printStackTrace();
            }

            if ( current != null )
            {
                if ( current.containsKey( cisAttr ) )
                {
                    Vector values = (Vector) current.get( cisAttr );
                    if ( values.contains( cisValue ) )
                    {
                        results.addElement( new Long( current.getID() ) );
                    }
                }
            }
            return results;
        }

        if ( exactIndexes.contains( cisAttr ) )
        {
            Connection dbcon = null;
            try
            {
                dbcon = (Connection) BackendJDBCConnPool.getInstance().checkOut();
                Statement s = dbcon.createStatement();
                ResultSet rs = null;
                String scopeQuery = null;
                if ( scope == SearchRequestEnum.WHOLESUBTREE )
                {
                    scopeQuery = "entry.base LIKE '%" + base + "'";
                }
                else
                {
                    scopeQuery = "entry.base = '" + base + "'";
                }
                rs = s.executeQuery( "SELECT DISTINCT entry.entryid FROM entry," + cisAttr +
                                     " WHERE entry.entryid = " + cisAttr + ".entryid AND UPPER(value) LIKE UPPER('" + cisValue + "')" +
                                     " AND " + scopeQuery );

                while ( rs.next() )
                {
                    Entry current = null;
                    long entryid = rs.getLong( 1 );
                    results.addElement( new Long( entryid ) );
                }
                BackendJDBCConnPool.getInstance().checkIn( dbcon );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                if ( dbcon != null )
                {
                    BackendJDBCConnPool.getInstance().checkIn( dbcon );
                }
            }
            return results;
        }


        return results;
    }
}
