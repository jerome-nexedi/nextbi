// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 2012/5/8 6:49:36
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DnParserImpl.java

package org.springframework.ldap.core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package org.springframework.ldap.core:
//            DistinguishedName, ParseException, LdapRdn, LdapRdnComponent, 
//            SimpleCharStream, DnParserImplTokenManager, Token, DnParser, 
//            DnParserImplConstants

public class DnParserImpl
    implements DnParser, DnParserImplConstants
{

    public final void input()
        throws ParseException
    {
        dn();
    }

    public final DistinguishedName dn()
        throws ParseException
    {
        DistinguishedName dn = new DistinguishedName();
        LdapRdn rdn = rdn();
        dn.add(0, rdn);
label0:
        do
            switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
            {
            default:
                jj_la1[0] = jj_gen;
                break label0;

            case 20: // '\024'
            case 21: // '\025'
                switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
                {
                case 20: // '\024'
                    jj_consume_token(20);
                    break;

                case 21: // '\025'
                    jj_consume_token(21);
                    break;

                default:
                    jj_la1[1] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
                }
                rdn = rdn();
                dn.add(0, rdn);
                break;
            }
        while(true);
        return dn;
    }

    public final LdapRdn rdn()
        throws ParseException
    {
        LdapRdn rdn = new LdapRdn();
        LdapRdnComponent rdnComponent = attributeTypeAndValue();
        rdn.addComponent(rdnComponent);
label0:
        do
            switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
            {
            default:
                jj_la1[2] = jj_gen;
                break label0;

            case 22: // '\026'
                jj_consume_token(22);
                rdnComponent = attributeTypeAndValue();
                rdn.addComponent(rdnComponent);
                break;
            }
        while(true);
        return rdn;
    }

    public final LdapRdnComponent attributeTypeAndValue()
        throws ParseException
    {
label0:
        do
            switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
            {
            default:
                jj_la1[3] = jj_gen;
                break label0;

            case 17: // '\021'
                jj_consume_token(17);
                break;
            }
        while(true);
        String attributeType = AttributeType();
        SpacedEquals();
        String value = AttributeValue();
label1:
        do
            switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
            {
            default:
                jj_la1[4] = jj_gen;
                break label1;

            case 17: // '\021'
                jj_consume_token(17);
                break;
            }
        while(true);
        return new LdapRdnComponent(attributeType, value, true);
    }

    public final void SpacedEquals()
        throws ParseException
    {
        token_source.SwitchTo(2);
        jj_consume_token(19);
    }

    public final String AttributeType()
        throws ParseException
    {
        Token t;
        switch(jj_ntk != -1 ? jj_ntk : jj_ntk())
        {
        case 16: // '\020'
            t = jj_consume_token(16);
            break;

        case 15: // '\017'
            t = jj_consume_token(15);
            break;

        default:
            jj_la1[5] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        return t.image.toString();
    }

    public final String AttributeValue()
        throws ParseException
    {
        token_source.SwitchTo(1);
        Token t = jj_consume_token(18);
        token_source.SwitchTo(0);
        return t.image.toString();
    }

    private static void jj_la1_init_0()
    {
        jj_la1_0 = (new int[] {
            0x300000, 0x300000, 0x400000, 0x20000, 0x20000, 0x18000
        });
    }

    public DnParserImpl(InputStream stream)
    {
        this(stream, null);
    }

    public DnParserImpl(InputStream stream, String encoding)
    {
        jj_la1 = new int[6];
        jj_expentries = new ArrayList();
        jj_kind = -1;
        try
        {
            jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        token_source = new DnParserImplTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 6; i++)
            jj_la1[i] = -1;

    }

    public void ReInit(InputStream stream)
    {
        ReInit(stream, null);
    }

    public void ReInit(InputStream stream, String encoding)
    {
        try
        {
            jj_input_stream.ReInit(stream, encoding, 1, 1);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 6; i++)
            jj_la1[i] = -1;

    }

    public DnParserImpl(Reader stream)
    {
        jj_la1 = new int[6];
        jj_expentries = new ArrayList();
        jj_kind = -1;
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new DnParserImplTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 6; i++)
            jj_la1[i] = -1;

    }

    public void ReInit(Reader stream)
    {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 6; i++)
            jj_la1[i] = -1;

    }

    public DnParserImpl(DnParserImplTokenManager tm)
    {
        jj_la1 = new int[6];
        jj_expentries = new ArrayList();
        jj_kind = -1;
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 6; i++)
            jj_la1[i] = -1;

    }

    public void ReInit(DnParserImplTokenManager tm)
    {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for(int i = 0; i < 6; i++)
            jj_la1[i] = -1;

    }

    private Token jj_consume_token(int kind)
        throws ParseException
    {
        Token oldToken;
        if((oldToken = token).next != null)
            token = token.next;
        else
            token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        if(token.kind == kind)
        {
            jj_gen++;
            return token;
        } else
        {
            token = oldToken;
            jj_kind = kind;
            throw generateParseException();
        }
    }

    public final Token getNextToken()
    {
        if(token.next != null)
            token = token.next;
        else
            token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    public final Token getToken(int index)
    {
        Token t = token;
        for(int i = 0; i < index; i++)
            if(t.next != null)
                t = t.next;
            else
                t = t.next = token_source.getNextToken();

        return t;
    }

    private int jj_ntk()
    {
        if((jj_nt = token.next) == null)
            return jj_ntk = (token.next = token_source.getNextToken()).kind;
        else
            return jj_ntk = jj_nt.kind;
    }

    public ParseException generateParseException()
    {
        jj_expentries.clear();
        boolean la1tokens[] = new boolean[23];
        if(jj_kind >= 0)
        {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for(int i = 0; i < 6; i++)
        {
            if(jj_la1[i] != jj_gen)
                continue;
            for(int j = 0; j < 32; j++)
                if((jj_la1_0[i] & 1 << j) != 0)
                    la1tokens[j] = true;

        }

        for(int i = 0; i < 23; i++)
            if(la1tokens[i])
            {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.add(jj_expentry);
            }

        int exptokseq[][] = new int[jj_expentries.size()][];
        for(int i = 0; i < jj_expentries.size(); i++)
            exptokseq[i] = (int[])(int[])jj_expentries.get(i);

        return new ParseException(token, exptokseq, tokenImage);
    }

    public final void enable_tracing()
    {
    }

    public final void disable_tracing()
    {
    }

    public DnParserImplTokenManager token_source;
    SimpleCharStream jj_input_stream;
    public Token token;
    public Token jj_nt;
    private int jj_ntk;
    private int jj_gen;
    private final int jj_la1[];
    private static int jj_la1_0[];
    private List jj_expentries;
    private int jj_expentry[];
    private int jj_kind;

    static 
    {
        jj_la1_init_0();
    }
}
