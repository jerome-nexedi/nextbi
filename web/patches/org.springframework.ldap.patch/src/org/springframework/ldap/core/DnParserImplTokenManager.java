// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 2012/5/8 6:47:50
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DnParserImplTokenManager.java

package org.springframework.ldap.core;

import java.io.IOException;
import java.io.PrintStream;

// Referenced classes of package org.springframework.ldap.core:
//            TokenMgrError, DnParserImplConstants, SimpleCharStream, Token

public class DnParserImplTokenManager
    implements DnParserImplConstants
{

    public void setDebugStream(PrintStream ds)
    {
        debugStream = ds;
    }

    private int jjMoveStringLiteralDfa0_1()
    {
        return jjMoveNfa_1(0, 0);
    }

    private int jjMoveNfa_1(int startState, int curPos)
    {
        int startsAt = 0;
        jjnewStateCnt = 22;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        do
        {
            if(++jjround == 0x7fffffff)
                ReInitRounds();
            if(curChar < '@')
            {
                long l = 1L << curChar;
                do
                    switch(jjstateSet[--i])
                    {
                    case 0: // '\0'
                        if((0x87ffe7f3ffffffffL & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        else
                        if(curChar == '#')
                            jjCheckNAdd(8);
                        else
                        if(curChar == '"')
                            jjCheckNAddTwoStates(1, 2);
                        if((0x87ffe7f2ffffffffL & l) != 0L && kind > 18)
                            kind = 18;
                        break;

                    case 1: // '\001'
                        if((0xfffffffbffffffffL & l) != 0L)
                            jjCheckNAddStates(3, 5);
                        break;

                    case 3: // '\003'
                        if((0x7800180c00002000L & l) != 0L)
                            jjCheckNAddStates(3, 5);
                        break;

                    case 4: // '\004'
                        if(curChar == '"' && kind > 18)
                            kind = 18;
                        break;

                    case 5: // '\005'
                        if((0x3ff000000000000L & l) != 0L)
                            jjstateSet[jjnewStateCnt++] = 6;
                        break;

                    case 6: // '\006'
                        if((0x3ff000000000000L & l) != 0L)
                            jjCheckNAddStates(3, 5);
                        break;

                    case 7: // '\007'
                        if(curChar == '#')
                            jjCheckNAdd(8);
                        break;

                    case 8: // '\b'
                        if((0x3ff000000000000L & l) != 0L)
                            jjstateSet[jjnewStateCnt++] = 9;
                        break;

                    case 9: // '\t'
                        if((0x3ff000000000000L & l) != 0L)
                        {
                            if(kind > 18)
                                kind = 18;
                            jjCheckNAdd(8);
                        }
                        break;

                    case 10: // '\n'
                        if((0x87ffe7f3ffffffffL & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;

                    case 11: // '\013'
                        if((0x87ffe7f2ffffffffL & l) != 0L && kind > 18)
                            kind = 18;
                        break;

                    case 13: // '\r'
                        if((0x7800180c00002000L & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;

                    case 14: // '\016'
                        if((0x3ff000000000000L & l) != 0L)
                            jjstateSet[jjnewStateCnt++] = 15;
                        break;

                    case 15: // '\017'
                        if((0x3ff000000000000L & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;

                    case 16: // '\020'
                        if((0x7800180c00002000L & l) != 0L && kind > 18)
                            kind = 18;
                        break;

                    case 17: // '\021'
                        if((0x3ff000000000000L & l) != 0L)
                            jjstateSet[jjnewStateCnt++] = 18;
                        break;

                    case 18: // '\022'
                        if((0x3ff000000000000L & l) != 0L && kind > 18)
                            kind = 18;
                        break;

                    case 19: // '\023'
                        if(curChar == ' ' && kind > 18)
                            kind = 18;
                        break;

                    case 21: // '\025'
                        if((0x900000000L & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;
                    }
                while(i != startsAt);
            } else
            if(curChar < '\200')
            {
                long l = 1L << (curChar & 0x3f);
                do
                    switch(jjstateSet[--i])
                    {
                    case 0: // '\0'
                        if((0xffffffffefffffffL & l) != 0L)
                        {
                            if(kind > 18)
                                kind = 18;
                        } else
                        if(curChar == '\\')
                            jjCheckNAddStates(6, 11);
                        if((0xffffffffefffffffL & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;

                    case 1: // '\001'
                        if((0xffffffffefffffffL & l) != 0L)
                            jjCheckNAddStates(3, 5);
                        break;

                    case 2: // '\002'
                        if(curChar == '\\')
                            jjAddStates(12, 13);
                        break;

                    case 3: // '\003'
                        if(curChar == '\\')
                            jjCheckNAddStates(3, 5);
                        break;

                    case 5: // '\005'
                        if((0x7e0000007eL & l) != 0L)
                            jjstateSet[jjnewStateCnt++] = 6;
                        break;

                    case 6: // '\006'
                        if((0x7e0000007eL & l) != 0L)
                            jjCheckNAddStates(3, 5);
                        break;

                    case 8: // '\b'
                        if((0x7e0000007eL & l) != 0L)
                            jjstateSet[jjnewStateCnt++] = 9;
                        break;

                    case 9: // '\t'
                        if((0x7e0000007eL & l) != 0L)
                        {
                            if(kind > 18)
                                kind = 18;
                            jjstateSet[jjnewStateCnt++] = 8;
                        }
                        break;

                    case 10: // '\n'
                        if((0xffffffffefffffffL & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;

                    case 11: // '\013'
                        if((0xffffffffefffffffL & l) != 0L && kind > 18)
                            kind = 18;
                        break;

                    case 12: // '\f'
                        if(curChar == '\\')
                            jjCheckNAddStates(14, 18);
                        break;

                    case 13: // '\r'
                        if(curChar == '\\')
                            jjCheckNAddStates(0, 2);
                        break;

                    case 14: // '\016'
                        if((0x7e0000007eL & l) != 0L)
                            jjstateSet[jjnewStateCnt++] = 15;
                        break;

                    case 15: // '\017'
                        if((0x7e0000007eL & l) != 0L)
                            jjCheckNAddStates(0, 2);
                        break;

                    case 16: // '\020'
                        if(curChar == '\\' && kind > 18)
                            kind = 18;
                        break;

                    case 17: // '\021'
                        if((0x7e0000007eL & l) != 0L)
                            jjstateSet[jjnewStateCnt++] = 18;
                        break;

                    case 18: // '\022'
                        if((0x7e0000007eL & l) != 0L && kind > 18)
                            kind = 18;
                        break;

                    case 20: // '\024'
                        if(curChar == '\\')
                            jjCheckNAddStates(6, 11);
                        break;
                    }
                while(i != startsAt);
            } else
            {
                int hiByte = curChar >> 8;
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 0x3f);
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 0x3f);
                do
                    switch(jjstateSet[--i])
                    {
                    case 0: // '\0'
                        if(jjCanMove_0(hiByte, i1, i2, l1, l2))
                            jjCheckNAddStates(0, 2);
                        if(jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 18)
                            kind = 18;
                        break;

                    case 1: // '\001'
                        if(jjCanMove_0(hiByte, i1, i2, l1, l2))
                            jjAddStates(3, 5);
                        break;

                    case 10: // '\n'
                        if(jjCanMove_0(hiByte, i1, i2, l1, l2))
                            jjCheckNAddStates(0, 2);
                        break;

                    case 11: // '\013'
                        if(jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 18)
                            kind = 18;
                        break;
                    }
                while(i != startsAt);
            }
            if(kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            curPos++;
            if((i = jjnewStateCnt) == (startsAt = 22 - (jjnewStateCnt = startsAt)))
                return curPos;
            try
            {
                curChar = input_stream.readChar();
            }
            catch(IOException e)
            {
                return curPos;
            }
        } while(true);
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0)
    {
        switch(pos)
        {
        default:
            return -1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0)
    {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private int jjStopAtPos(int pos, int kind)
    {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private int jjMoveStringLiteralDfa0_0()
    {
        switch(curChar)
        {
        case 32: // ' '
            return jjStopAtPos(0, 17);

        case 34: // '"'
            return jjStopAtPos(0, 13);

        case 35: // '#'
            return jjStopAtPos(0, 14);

        case 43: // '+'
            return jjStopAtPos(0, 22);

        case 44: // ','
            return jjStopAtPos(0, 20);

        case 59: // ';'
            return jjStopAtPos(0, 21);
        }
        return jjMoveNfa_0(0, 0);
    }

    private int jjMoveNfa_0(int startState, int curPos)
    {
        int startsAt = 0;
        jjnewStateCnt = 5;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        do
        {
            if(++jjround == 0x7fffffff)
                ReInitRounds();
            if(curChar < '@')
            {
                long l = 1L << curChar;
                do
                    switch(jjstateSet[--i])
                    {
                    case 0: // '\0'
                    case 2: // '\002'
                        if((0x3ff000000000000L & l) != 0L)
                        {
                            if(kind > 16)
                                kind = 16;
                            jjCheckNAddTwoStates(2, 3);
                        }
                        break;

                    case 1: // '\001'
                        if((0x3ff200000000000L & l) != 0L)
                        {
                            if(kind > 15)
                                kind = 15;
                            jjstateSet[jjnewStateCnt++] = 1;
                        }
                        break;

                    case 3: // '\003'
                        if(curChar == '.')
                            jjCheckNAdd(4);
                        break;

                    case 4: // '\004'
                        if((0x3ff000000000000L & l) != 0L)
                        {
                            if(kind > 16)
                                kind = 16;
                            jjCheckNAddTwoStates(3, 4);
                        }
                        break;
                    }
                while(i != startsAt);
            } else
            if(curChar < '\200')
            {
                long l = 1L << (curChar & 0x3f);
                do
                    switch(jjstateSet[--i])
                    {
                    case 0: // '\0'
                    case 1: // '\001'
                        if((0x7fffffe07fffffeL & l) != 0L)
                        {
                            if(kind > 15)
                                kind = 15;
                            jjCheckNAdd(1);
                        }
                        break;
                    }
                while(i != startsAt);
            } else
            {
                int hiByte = curChar >> 8;
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 0x3f);
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 0x3f);
                do
                    switch(jjstateSet[--i])
                    {
                    }
                while(i != startsAt);
            }
            if(kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            curPos++;
            if((i = jjnewStateCnt) == (startsAt = 5 - (jjnewStateCnt = startsAt)))
                return curPos;
            try
            {
                curChar = input_stream.readChar();
            }
            catch(IOException e)
            {
                return curPos;
            }
        } while(true);
    }

    private int jjMoveStringLiteralDfa0_2()
    {
        return jjMoveNfa_2(3, 0);
    }

    private int jjMoveNfa_2(int startState, int curPos)
    {
        int startsAt = 0;
        jjnewStateCnt = 3;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        do
        {
            if(++jjround == 0x7fffffff)
                ReInitRounds();
            if(curChar < '@')
            {
                long l = 1L << curChar;
                do
                    switch(jjstateSet[--i])
                    {
                    case 3: // '\003'
                        if(curChar == '=')
                        {
                            if(kind > 19)
                                kind = 19;
                            jjCheckNAdd(2);
                        } else
                        if(curChar == ' ')
                            jjCheckNAddTwoStates(0, 1);
                        break;

                    case 0: // '\0'
                        if(curChar == ' ')
                            jjCheckNAddTwoStates(0, 1);
                        break;

                    case 1: // '\001'
                        if(curChar == '=')
                        {
                            kind = 19;
                            jjCheckNAdd(2);
                        }
                        break;

                    case 2: // '\002'
                        if(curChar == ' ')
                        {
                            if(kind > 19)
                                kind = 19;
                            jjCheckNAdd(2);
                        }
                        break;
                    }
                while(i != startsAt);
            } else
            if(curChar < '\200')
            {
                long l = 1L << (curChar & 0x3f);
                do
                    switch(jjstateSet[--i])
                    {
                    }
                while(i != startsAt);
            } else
            {
                int hiByte = curChar >> 8;
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 0x3f);
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 0x3f);
                do
                    switch(jjstateSet[--i])
                    {
                    }
                while(i != startsAt);
            }
            if(kind != 0x7fffffff)
            {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            curPos++;
            if((i = jjnewStateCnt) == (startsAt = 3 - (jjnewStateCnt = startsAt)))
                return curPos;
            try
            {
                curChar = input_stream.readChar();
            }
            catch(IOException e)
            {
                return curPos;
            }
        } while(true);
    }

    private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
    {
        switch(hiByte)
        {
        case 0: // '\0'
            return (jjbitVec2[i2] & l2) != 0L;
        }
        return (jjbitVec0[i1] & l1) != 0L;
    }

    public DnParserImplTokenManager(SimpleCharStream stream)
    {
        debugStream = System.out;
        jjrounds = new int[22];
        jjstateSet = new int[44];
        curLexState = 0;
        defaultLexState = 0;
        input_stream = stream;
    }

    public DnParserImplTokenManager(SimpleCharStream stream, int lexState)
    {
        this(stream);
        SwitchTo(lexState);
    }

    public void ReInit(SimpleCharStream stream)
    {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private void ReInitRounds()
    {
        jjround = 0x80000001;
        for(int i = 22; i-- > 0;)
            jjrounds[i] = 0x80000000;

    }

    public void ReInit(SimpleCharStream stream, int lexState)
    {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState)
    {
        if(lexState >= 3 || lexState < 0)
        {
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
        } else
        {
            curLexState = lexState;
            return;
        }
    }

    protected Token jjFillToken()
    {
        String im = jjstrLiteralImages[jjmatchedKind];
        String curTokenImage = im != null ? im : input_stream.GetImage();
        int beginLine = input_stream.getBeginLine();
        int beginColumn = input_stream.getBeginColumn();
        int endLine = input_stream.getEndLine();
        int endColumn = input_stream.getEndColumn();
        Token t = Token.newToken(jjmatchedKind, curTokenImage);
        t.beginLine = beginLine;
        t.endLine = endLine;
        t.beginColumn = beginColumn;
        t.endColumn = endColumn;
        return t;
    }

    public Token getNextToken()
    {
        int curPos = 0;
        try
        {
            curChar = input_stream.BeginToken();
        }
        catch(IOException e)
        {
            jjmatchedKind = 0;
            Token matchedToken = jjFillToken();
            return matchedToken;
        }
        switch(curLexState)
        {
        case 0: // '\0'
            jjmatchedKind = 0x7fffffff;
            jjmatchedPos = 0;
            curPos = jjMoveStringLiteralDfa0_0();
            break;

        case 1: // '\001'
            jjmatchedKind = 0x7fffffff;
            jjmatchedPos = 0;
            curPos = jjMoveStringLiteralDfa0_1();
            break;

        case 2: // '\002'
            jjmatchedKind = 0x7fffffff;
            jjmatchedPos = 0;
            curPos = jjMoveStringLiteralDfa0_2();
            break;
        }
        if(jjmatchedKind != 0x7fffffff)
        {
            if(jjmatchedPos + 1 < curPos)
                input_stream.backup(curPos - jjmatchedPos - 1);
            Token matchedToken = jjFillToken();
            if(jjnewLexState[jjmatchedKind] != -1)
                curLexState = jjnewLexState[jjmatchedKind];
            return matchedToken;
        }
        int error_line = input_stream.getEndLine();
        int error_column = input_stream.getEndColumn();
        String error_after = null;
        boolean EOFSeen = false;
        try
        {
            input_stream.readChar();
            input_stream.backup(1);
        }
        catch(IOException e1)
        {
            EOFSeen = true;
            error_after = curPos > 1 ? input_stream.GetImage() : "";
            if(curChar == '\n' || curChar == '\r')
            {
                error_line++;
                error_column = 0;
            } else
            {
                error_column++;
            }
        }
        if(!EOFSeen)
        {
            input_stream.backup(1);
            error_after = curPos > 1 ? input_stream.GetImage() : "";
        }
        throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, 0);
    }

    private void jjCheckNAdd(int state)
    {
        if(jjrounds[state] != jjround)
        {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private void jjAddStates(int start, int end)
    {
        do
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        while(start++ != end);
    }

    private void jjCheckNAddTwoStates(int state1, int state2)
    {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private void jjCheckNAddStates(int start, int end)
    {
        do
            jjCheckNAdd(jjnextStates[start]);
        while(start++ != end);
    }

    public PrintStream debugStream;
    static final long jjbitVec0[] = {
        -2L, -1L, -1L, -1L
    };
    static final long jjbitVec2[] = {
        0L, 0L, -1L, -1L
    };
    static final int jjnextStates[] = {
        10, 11, 12, 1, 2, 4, 21, 13, 14, 16, 
        17, 19, 3, 5, 13, 14, 16, 17, 19
    };
    public static final String jjstrLiteralImages[] = {
        "", null, null, null, null, null, null, null, null, null, 
        null, null, null, "\"", "#", null, null, " ", null, null, 
        ",", ";", "+"
    };
    public static final String lexStateNames[] = {
        "DEFAULT", "ATTRVALUE_S", "SPACED_EQUALS_S"
    };
    public static final int jjnewLexState[] = {
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
        -1, -1, -1
    };
    protected SimpleCharStream input_stream;
    private final int jjrounds[];
    private final int jjstateSet[];
    protected char curChar;
    int curLexState;
    int defaultLexState;
    int jjnewStateCnt;
    int jjround;
    int jjmatchedPos;
    int jjmatchedKind;

}
