// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   X500Name.java

package com.ibm.util.x500name;

import java.util.Enumeration;
import java.util.NoSuchElementException;

// Referenced classes of package com.ibm.util.x500name:
//            RDNAttribute, X500Name, X500NameEnumerator2, RDNAttributeFactory

class X500NameEnumerator1
    implements Enumeration
{

    X500NameEnumerator1(X500Name x500name, RDNAttributeFactory rdnattributefactory)
    {
        name = x500name;
        factory = rdnattributefactory;
    }

    private int findNext()
    {
        for(; index < name.attributes.length; index++)
        {
            for(int i = 0; i < name.attributes[index].length; i++)
                if(name.attributes[index][i].factory() == factory)
                    return index;

        }

        return index;
    }

    public boolean hasMoreElements()
    {
        findNext();
        return index < name.attributes.length;
    }

    public Object nextElement()
    {
        if(factory != null)
            findNext();
        if(index >= name.attributes.length)
            throw new NoSuchElementException("No more sets of RDNAttribute objects");
        if(name.attributes[index].length == 1)
            return name.attributes[index++][0];
        else
            return new X500NameEnumerator2(name, index++);
    }

    X500Name name;
    int index;
    RDNAttributeFactory factory;
}
