// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   X500Name.java

package com.ibm.util.x500name;

import java.util.Enumeration;
import java.util.NoSuchElementException;

// Referenced classes of package com.ibm.util.x500name:
//            X500Name, X500NameEnumerator1

class X500NameEnumerator2
    implements Enumeration
{

    X500NameEnumerator2(X500Name x500name, int i)
    {
        name = x500name;
        nthset = i;
    }

    public boolean hasMoreElements()
    {
        return index < name.attributes[nthset].length;
    }

    public Object nextElement()
    {
        if(index >= name.attributes[nthset].length)
            throw new NoSuchElementException("No more RDNAttribute objects");
        else
            return name.attributes[nthset][index++];
    }

    X500Name name;
    int nthset;
    int index;
}
