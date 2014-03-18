package edu.udel.cis.vsl.abc.preproc.common;

// $ANTLR 3.4 OmpLexer.g 2014-03-17 17:27:33

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class OmpLexer extends Lexer {
    public static final int EOF=-1;
    public static final int ABSTRACT=4;
    public static final int ALIGNAS=5;
    public static final int ALIGNOF=6;
    public static final int AMPERSAND=7;
    public static final int AND=8;
    public static final int ARROW=9;
    public static final int ASSIGN=10;
    public static final int ASSUME=11;
    public static final int AT=12;
    public static final int ATOMIC=13;
    public static final int AUTO=14;
    public static final int BIG_O=15;
    public static final int BITANDEQ=16;
    public static final int BITOR=17;
    public static final int BITOREQ=18;
    public static final int BITXOR=19;
    public static final int BITXOREQ=20;
    public static final int BOOL=21;
    public static final int BREAK=22;
    public static final int BinaryExponentPart=23;
    public static final int CASE=24;
    public static final int CChar=25;
    public static final int CHAR=26;
    public static final int CHARACTER_CONSTANT=27;
    public static final int CHOOSE=28;
    public static final int CIVLATOM=29;
    public static final int CIVLATOMIC=30;
    public static final int COLLECTIVE=31;
    public static final int COLON=32;
    public static final int COMMA=33;
    public static final int COMMENT=34;
    public static final int COMPLEX=35;
    public static final int CONST=36;
    public static final int CONTIN=37;
    public static final int CONTINUE=38;
    public static final int DEFAULT=39;
    public static final int DEFINED=40;
    public static final int DERIV=41;
    public static final int DIV=42;
    public static final int DIVEQ=43;
    public static final int DO=44;
    public static final int DOT=45;
    public static final int DOTDOT=46;
    public static final int DOUBLE=47;
    public static final int DecimalConstant=48;
    public static final int DecimalFloatingConstant=49;
    public static final int Digit=50;
    public static final int ELLIPSIS=51;
    public static final int ELSE=52;
    public static final int ENSURES=53;
    public static final int ENUM=54;
    public static final int EQUALS=55;
    public static final int EXISTS=56;
    public static final int EXTERN=57;
    public static final int EscapeSequence=58;
    public static final int ExponentPart=59;
    public static final int FALSE=60;
    public static final int FLOAT=61;
    public static final int FLOATING_CONSTANT=62;
    public static final int FOR=63;
    public static final int FORALL=64;
    public static final int FloatingSuffix=65;
    public static final int FractionalConstant=66;
    public static final int GENERIC=67;
    public static final int GOTO=68;
    public static final int GT=69;
    public static final int GTE=70;
    public static final int HASH=71;
    public static final int HASHHASH=72;
    public static final int HEADER_NAME=73;
    public static final int HERE=74;
    public static final int HexEscape=75;
    public static final int HexFractionalConstant=76;
    public static final int HexPrefix=77;
    public static final int HexQuad=78;
    public static final int HexadecimalConstant=79;
    public static final int HexadecimalDigit=80;
    public static final int HexadecimalFloatingConstant=81;
    public static final int IDENTIFIER=82;
    public static final int IF=83;
    public static final int IMAGINARY=84;
    public static final int IMPLIES=85;
    public static final int INLINE=86;
    public static final int INPUT=87;
    public static final int INT=88;
    public static final int INTEGER_CONSTANT=89;
    public static final int INVARIANT=90;
    public static final int IdentifierNonDigit=91;
    public static final int IntegerSuffix=92;
    public static final int LCURLY=93;
    public static final int LONG=94;
    public static final int LPAREN=95;
    public static final int LSLIST=96;
    public static final int LSQUARE=97;
    public static final int LT=98;
    public static final int LTE=99;
    public static final int LongLongSuffix=100;
    public static final int LongSuffix=101;
    public static final int MINUSMINUS=102;
    public static final int MOD=103;
    public static final int MODEQ=104;
    public static final int NEQ=105;
    public static final int NEWLINE=106;
    public static final int NORETURN=107;
    public static final int NOT=108;
    public static final int NewLine=109;
    public static final int NonDigit=110;
    public static final int NonZeroDigit=111;
    public static final int NotLineStart=112;
    public static final int OR=113;
    public static final int OTHER=114;
    public static final int OUTPUT=115;
    public static final int OctalConstant=116;
    public static final int OctalDigit=117;
    public static final int OctalEscape=118;
    public static final int PDEFINE=119;
    public static final int PELIF=120;
    public static final int PELSE=121;
    public static final int PENDIF=122;
    public static final int PERROR=123;
    public static final int PIF=124;
    public static final int PIFDEF=125;
    public static final int PIFNDEF=126;
    public static final int PINCLUDE=127;
    public static final int PLINE=128;
    public static final int PLUS=129;
    public static final int PLUSEQ=130;
    public static final int PLUSPLUS=131;
    public static final int PP_NUMBER=132;
    public static final int PRAGMA=133;
    public static final int PUNDEF=134;
    public static final int QMARK=135;
    public static final int RCURLY=136;
    public static final int REAL=137;
    public static final int REGISTER=138;
    public static final int REQUIRES=139;
    public static final int RESTRICT=140;
    public static final int RESULT=141;
    public static final int RETURN=142;
    public static final int ROOT=143;
    public static final int RPAREN=144;
    public static final int RSLIST=145;
    public static final int RSQUARE=146;
    public static final int SCOPE=147;
    public static final int SCOPEOF=148;
    public static final int SChar=149;
    public static final int SELF=150;
    public static final int SEMI=151;
    public static final int SHIFTLEFT=152;
    public static final int SHIFTLEFTEQ=153;
    public static final int SHIFTRIGHT=154;
    public static final int SHIFTRIGHTEQ=155;
    public static final int SHORT=156;
    public static final int SIGNED=157;
    public static final int SIZEOF=158;
    public static final int SPAWN=159;
    public static final int STAR=160;
    public static final int STAREQ=161;
    public static final int STATIC=162;
    public static final int STATICASSERT=163;
    public static final int STRING_LITERAL=164;
    public static final int STRUCT=165;
    public static final int SUB=166;
    public static final int SUBEQ=167;
    public static final int SWITCH=168;
    public static final int THREADLOCAL=169;
    public static final int TILDE=170;
    public static final int TRUE=171;
    public static final int TYPEDEF=172;
    public static final int UNIFORM=173;
    public static final int UNION=174;
    public static final int UNSIGNED=175;
    public static final int UniversalCharacterName=176;
    public static final int UnsignedSuffix=177;
    public static final int VOID=178;
    public static final int VOLATILE=179;
    public static final int WHEN=180;
    public static final int WHILE=181;
    public static final int WS=182;
    public static final int Zero=183;
    public static final int BARRIER=195;
    public static final int COLLAPSE=212;
    public static final int COPYIN=221;
    public static final int CRITICAL=222;
    public static final int DYNAMIC=232;
    public static final int FLUSH=248;
    public static final int FST_PRIVATE=251;
    public static final int GUIDED=258;
    public static final int LST_PRIVATE=286;
    public static final int MASTER=291;
    public static final int NONE=297;
    public static final int NOWAIT=300;
    public static final int NUM_THREADS=301;
    public static final int OMP=306;
    public static final int ORDERED=308;
    public static final int PARALLEL=314;
    public static final int PRIVATE=330;
    public static final int REDUCTION=335;
    public static final int RUNTIME=345;
    public static final int SCHEDULE=346;
    public static final int SECTION=350;
    public static final int SECTIONS=351;
    public static final int SHARED=354;
    public static final int THD_PRIVATE=372;
    public static final int Tokens=377;
    public static final int COPYPRIVATE=413;

    // delegates
    public OmpLexer_PreprocessorLexer gPreprocessorLexer;
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {gPreprocessorLexer};
    }

    public OmpLexer() {} 
    public OmpLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public OmpLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
        gPreprocessorLexer = new OmpLexer_PreprocessorLexer(input, state, this);
    }
    public String getGrammarFileName() { return "OmpLexer.g"; }

    // $ANTLR start "BARRIER"
    public final void mBARRIER() throws RecognitionException {
        try {
            int _type = BARRIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:11:10: ( 'barrier' NotLineStart )
            // OmpLexer.g:11:12: 'barrier' NotLineStart
            {
            match("barrier"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BARRIER"

    // $ANTLR start "COLLAPSE"
    public final void mCOLLAPSE() throws RecognitionException {
        try {
            int _type = COLLAPSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:12:10: ( 'collapse' NotLineStart )
            // OmpLexer.g:12:12: 'collapse' NotLineStart
            {
            match("collapse"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COLLAPSE"

    // $ANTLR start "COPYIN"
    public final void mCOPYIN() throws RecognitionException {
        try {
            int _type = COPYIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:13:9: ( 'copyin' NotLineStart )
            // OmpLexer.g:13:11: 'copyin' NotLineStart
            {
            match("copyin"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COPYIN"

    // $ANTLR start "COPYPRIVATE"
    public final void mCOPYPRIVATE() throws RecognitionException {
        try {
            int _type = COPYPRIVATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:14:13: ( 'copyprivate' NotLineStart )
            // OmpLexer.g:14:15: 'copyprivate' NotLineStart
            {
            match("copyprivate"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COPYPRIVATE"

    // $ANTLR start "CRITICAL"
    public final void mCRITICAL() throws RecognitionException {
        try {
            int _type = CRITICAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:15:10: ( 'critical' NotLineStart )
            // OmpLexer.g:15:12: 'critical' NotLineStart
            {
            match("critical"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CRITICAL"

    // $ANTLR start "DEFAULT"
    public final void mDEFAULT() throws RecognitionException {
        try {
            int _type = DEFAULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:16:10: ( 'default' NotLineStart )
            // OmpLexer.g:16:12: 'default' NotLineStart
            {
            match("default"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DEFAULT"

    // $ANTLR start "DYNAMIC"
    public final void mDYNAMIC() throws RecognitionException {
        try {
            int _type = DYNAMIC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:17:10: ( 'dynamic' NotLineStart )
            // OmpLexer.g:17:12: 'dynamic' NotLineStart
            {
            match("dynamic"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DYNAMIC"

    // $ANTLR start "FST_PRIVATE"
    public final void mFST_PRIVATE() throws RecognitionException {
        try {
            int _type = FST_PRIVATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:18:13: ( 'firstprivate' NotLineStart )
            // OmpLexer.g:18:15: 'firstprivate' NotLineStart
            {
            match("firstprivate"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FST_PRIVATE"

    // $ANTLR start "FLUSH"
    public final void mFLUSH() throws RecognitionException {
        try {
            int _type = FLUSH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:19:8: ( 'flush' NotLineStart )
            // OmpLexer.g:19:10: 'flush' NotLineStart
            {
            match("flush"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FLUSH"

    // $ANTLR start "GUIDED"
    public final void mGUIDED() throws RecognitionException {
        try {
            int _type = GUIDED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:20:9: ( 'guided' NotLineStart )
            // OmpLexer.g:20:11: 'guided' NotLineStart
            {
            match("guided"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "GUIDED"

    // $ANTLR start "LST_PRIVATE"
    public final void mLST_PRIVATE() throws RecognitionException {
        try {
            int _type = LST_PRIVATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:21:13: ( 'lastprivate' NotLineStart )
            // OmpLexer.g:21:15: 'lastprivate' NotLineStart
            {
            match("lastprivate"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LST_PRIVATE"

    // $ANTLR start "MASTER"
    public final void mMASTER() throws RecognitionException {
        try {
            int _type = MASTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:22:9: ( 'master' NotLineStart )
            // OmpLexer.g:22:11: 'master' NotLineStart
            {
            match("master"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MASTER"

    // $ANTLR start "NONE"
    public final void mNONE() throws RecognitionException {
        try {
            int _type = NONE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:23:7: ( 'none' NotLineStart )
            // OmpLexer.g:23:9: 'none' NotLineStart
            {
            match("none"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NONE"

    // $ANTLR start "NOWAIT"
    public final void mNOWAIT() throws RecognitionException {
        try {
            int _type = NOWAIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:24:9: ( 'nowait' NotLineStart )
            // OmpLexer.g:24:11: 'nowait' NotLineStart
            {
            match("nowait"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NOWAIT"

    // $ANTLR start "NUM_THREADS"
    public final void mNUM_THREADS() throws RecognitionException {
        try {
            int _type = NUM_THREADS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:25:13: ( 'num_threads' NotLineStart )
            // OmpLexer.g:25:15: 'num_threads' NotLineStart
            {
            match("num_threads"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NUM_THREADS"

    // $ANTLR start "ORDERED"
    public final void mORDERED() throws RecognitionException {
        try {
            int _type = ORDERED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:26:10: ( 'ordered' NotLineStart )
            // OmpLexer.g:26:12: 'ordered' NotLineStart
            {
            match("ordered"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ORDERED"

    // $ANTLR start "PARALLEL"
    public final void mPARALLEL() throws RecognitionException {
        try {
            int _type = PARALLEL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:27:10: ( 'parallel' NotLineStart )
            // OmpLexer.g:27:12: 'parallel' NotLineStart
            {
            match("parallel"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PARALLEL"

    // $ANTLR start "PRIVATE"
    public final void mPRIVATE() throws RecognitionException {
        try {
            int _type = PRIVATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:28:10: ( 'private' NotLineStart )
            // OmpLexer.g:28:12: 'private' NotLineStart
            {
            match("private"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PRIVATE"

    // $ANTLR start "REDUCTION"
    public final void mREDUCTION() throws RecognitionException {
        try {
            int _type = REDUCTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:29:11: ( 'reduction' NotLineStart )
            // OmpLexer.g:29:13: 'reduction' NotLineStart
            {
            match("reduction"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "REDUCTION"

    // $ANTLR start "RUNTIME"
    public final void mRUNTIME() throws RecognitionException {
        try {
            int _type = RUNTIME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:30:10: ( 'runtime' NotLineStart )
            // OmpLexer.g:30:12: 'runtime' NotLineStart
            {
            match("runtime"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RUNTIME"

    // $ANTLR start "SCHEDULE"
    public final void mSCHEDULE() throws RecognitionException {
        try {
            int _type = SCHEDULE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:31:10: ( 'schedule' NotLineStart )
            // OmpLexer.g:31:12: 'schedule' NotLineStart
            {
            match("schedule"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SCHEDULE"

    // $ANTLR start "SECTIONS"
    public final void mSECTIONS() throws RecognitionException {
        try {
            int _type = SECTIONS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:32:10: ( 'sections' NotLineStart )
            // OmpLexer.g:32:12: 'sections' NotLineStart
            {
            match("sections"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SECTIONS"

    // $ANTLR start "SECTION"
    public final void mSECTION() throws RecognitionException {
        try {
            int _type = SECTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:33:10: ( 'section' NotLineStart )
            // OmpLexer.g:33:12: 'section' NotLineStart
            {
            match("section"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SECTION"

    // $ANTLR start "SHARED"
    public final void mSHARED() throws RecognitionException {
        try {
            int _type = SHARED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:34:9: ( 'shared' NotLineStart )
            // OmpLexer.g:34:11: 'shared' NotLineStart
            {
            match("shared"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SHARED"

    // $ANTLR start "STATIC"
    public final void mSTATIC() throws RecognitionException {
        try {
            int _type = STATIC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:35:9: ( 'static' NotLineStart )
            // OmpLexer.g:35:11: 'static' NotLineStart
            {
            match("static"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STATIC"

    // $ANTLR start "THD_PRIVATE"
    public final void mTHD_PRIVATE() throws RecognitionException {
        try {
            int _type = THD_PRIVATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // OmpLexer.g:36:13: ( 'threadprivate' NotLineStart )
            // OmpLexer.g:36:15: 'threadprivate' NotLineStart
            {
            match("threadprivate"); 



            gPreprocessorLexer.mNotLineStart(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "THD_PRIVATE"

    public void mTokens() throws RecognitionException {
        // OmpLexer.g:1:8: ( BARRIER | COLLAPSE | COPYIN | COPYPRIVATE | CRITICAL | DEFAULT | DYNAMIC | FST_PRIVATE | FLUSH | GUIDED | LST_PRIVATE | MASTER | NONE | NOWAIT | NUM_THREADS | ORDERED | PARALLEL | PRIVATE | REDUCTION | RUNTIME | SCHEDULE | SECTIONS | SECTION | SHARED | STATIC | THD_PRIVATE | PreprocessorLexer. Tokens )
        int alt1=27;
        alt1 = dfa1.predict(input);
        switch (alt1) {
            case 1 :
                // OmpLexer.g:1:10: BARRIER
                {
                mBARRIER(); 


                }
                break;
            case 2 :
                // OmpLexer.g:1:18: COLLAPSE
                {
                mCOLLAPSE(); 


                }
                break;
            case 3 :
                // OmpLexer.g:1:27: COPYIN
                {
                mCOPYIN(); 


                }
                break;
            case 4 :
                // OmpLexer.g:1:34: COPYPRIVATE
                {
                mCOPYPRIVATE(); 


                }
                break;
            case 5 :
                // OmpLexer.g:1:46: CRITICAL
                {
                mCRITICAL(); 


                }
                break;
            case 6 :
                // OmpLexer.g:1:55: DEFAULT
                {
                mDEFAULT(); 


                }
                break;
            case 7 :
                // OmpLexer.g:1:63: DYNAMIC
                {
                mDYNAMIC(); 


                }
                break;
            case 8 :
                // OmpLexer.g:1:71: FST_PRIVATE
                {
                mFST_PRIVATE(); 


                }
                break;
            case 9 :
                // OmpLexer.g:1:83: FLUSH
                {
                mFLUSH(); 


                }
                break;
            case 10 :
                // OmpLexer.g:1:89: GUIDED
                {
                mGUIDED(); 


                }
                break;
            case 11 :
                // OmpLexer.g:1:96: LST_PRIVATE
                {
                mLST_PRIVATE(); 


                }
                break;
            case 12 :
                // OmpLexer.g:1:108: MASTER
                {
                mMASTER(); 


                }
                break;
            case 13 :
                // OmpLexer.g:1:115: NONE
                {
                mNONE(); 


                }
                break;
            case 14 :
                // OmpLexer.g:1:120: NOWAIT
                {
                mNOWAIT(); 


                }
                break;
            case 15 :
                // OmpLexer.g:1:127: NUM_THREADS
                {
                mNUM_THREADS(); 


                }
                break;
            case 16 :
                // OmpLexer.g:1:139: ORDERED
                {
                mORDERED(); 


                }
                break;
            case 17 :
                // OmpLexer.g:1:147: PARALLEL
                {
                mPARALLEL(); 


                }
                break;
            case 18 :
                // OmpLexer.g:1:156: PRIVATE
                {
                mPRIVATE(); 


                }
                break;
            case 19 :
                // OmpLexer.g:1:164: REDUCTION
                {
                mREDUCTION(); 


                }
                break;
            case 20 :
                // OmpLexer.g:1:174: RUNTIME
                {
                mRUNTIME(); 


                }
                break;
            case 21 :
                // OmpLexer.g:1:182: SCHEDULE
                {
                mSCHEDULE(); 


                }
                break;
            case 22 :
                // OmpLexer.g:1:191: SECTIONS
                {
                mSECTIONS(); 


                }
                break;
            case 23 :
                // OmpLexer.g:1:200: SECTION
                {
                mSECTION(); 


                }
                break;
            case 24 :
                // OmpLexer.g:1:208: SHARED
                {
                mSHARED(); 


                }
                break;
            case 25 :
                // OmpLexer.g:1:215: STATIC
                {
                mSTATIC(); 


                }
                break;
            case 26 :
                // OmpLexer.g:1:222: THD_PRIVATE
                {
                mTHD_PRIVATE(); 


                }
                break;
            case 27 :
                // OmpLexer.g:1:234: PreprocessorLexer. Tokens
                {
                gPreprocessorLexer.mTokens(); 


                }
                break;

        }

    }


    protected DFA1 dfa1 = new DFA1(this);
    static final String DFA1_eotS =
        "\16\16\1\uffff\71\16\1\141\24\16\1\166\3\16\1\uffff\16\16\1\u0088"+
        "\5\16\1\uffff\1\u008e\1\16\1\u0090\1\u0091\10\16\1\u009a\1\u009b"+
        "\1\16\1\u009d\1\16\1\uffff\2\16\1\u00a1\1\u00a2\1\16\1\uffff\1\16"+
        "\2\uffff\1\16\1\u00a6\1\16\1\u00a8\1\16\1\u00aa\1\16\1\u00ad\2\uffff"+
        "\1\16\1\uffff\1\u00af\1\16\1\u00b1\2\uffff\3\16\1\uffff\1\u00b5"+
        "\1\uffff\1\16\1\uffff\1\u00b7\1\u00b8\1\uffff\1\16\1\uffff\1\16"+
        "\1\uffff\3\16\1\uffff\1\u00be\2\uffff\5\16\1\uffff\1\16\1\u00c5"+
        "\1\16\1\u00c7\1\u00c8\1\16\1\uffff\1\u00ca\2\uffff\1\16\1\uffff"+
        "\1\u00cc\1\uffff";
    static final String DFA1_eofS =
        "\u00cd\uffff";
    static final String DFA1_minS =
        "\1\142\1\141\1\157\1\145\1\151\1\165\2\141\1\157\1\162\1\141\1\145"+
        "\1\143\1\150\1\uffff\1\162\1\154\1\151\1\146\1\156\1\162\1\165\1"+
        "\151\2\163\1\156\1\155\1\144\1\162\1\151\1\144\1\156\1\150\1\143"+
        "\2\141\2\162\1\154\1\171\1\164\2\141\2\163\1\144\2\164\1\145\1\141"+
        "\1\137\1\145\1\141\1\166\1\165\1\164\1\145\1\164\1\162\1\164\1\145"+
        "\1\151\1\141\2\151\1\165\1\155\1\164\1\150\1\145\1\160\1\145\1\44"+
        "\1\151\1\164\1\162\1\154\1\141\1\143\1\151\1\144\1\151\1\145\1\151"+
        "\1\141\1\145\1\160\1\156\1\162\1\143\1\154\1\151\1\160\1\44\1\144"+
        "\2\162\1\uffff\1\164\1\150\1\145\1\154\2\164\1\155\1\165\1\157\1"+
        "\144\1\143\1\144\1\162\1\163\1\44\1\151\1\141\1\164\1\143\1\162"+
        "\1\uffff\1\44\1\151\2\44\1\162\1\144\2\145\1\151\1\145\1\154\1\156"+
        "\2\44\1\160\1\44\1\145\1\uffff\1\166\1\154\2\44\1\151\1\uffff\1"+
        "\166\2\uffff\1\145\1\44\1\154\1\44\1\157\1\44\1\145\1\44\2\uffff"+
        "\1\162\1\uffff\1\44\1\141\1\44\2\uffff\1\166\2\141\1\uffff\1\44"+
        "\1\uffff\1\156\1\uffff\2\44\1\uffff\1\151\1\uffff\1\164\1\uffff"+
        "\1\141\1\164\1\144\1\uffff\1\44\2\uffff\1\166\1\145\1\164\1\145"+
        "\1\163\1\uffff\1\141\1\44\1\145\2\44\1\164\1\uffff\1\44\2\uffff"+
        "\1\145\1\uffff\1\44\1\uffff";
    static final String DFA1_maxS =
        "\1\164\1\141\1\162\1\171\1\154\1\165\2\141\1\165\2\162\1\165\1\164"+
        "\1\150\1\uffff\1\162\1\160\1\151\1\146\1\156\1\162\1\165\1\151\2"+
        "\163\1\167\1\155\1\144\1\162\1\151\1\144\1\156\1\150\1\143\2\141"+
        "\2\162\1\154\1\171\1\164\2\141\2\163\1\144\2\164\1\145\1\141\1\137"+
        "\1\145\1\141\1\166\1\165\1\164\1\145\1\164\1\162\1\164\1\145\1\151"+
        "\1\141\1\160\1\151\1\165\1\155\1\164\1\150\1\145\1\160\1\145\1\172"+
        "\1\151\1\164\1\162\1\154\1\141\1\143\1\151\1\144\1\151\1\145\1\151"+
        "\1\141\1\145\1\160\1\156\1\162\1\143\1\154\1\151\1\160\1\172\1\144"+
        "\2\162\1\uffff\1\164\1\150\1\145\1\154\2\164\1\155\1\165\1\157\1"+
        "\144\1\143\1\144\1\162\1\163\1\172\1\151\1\141\1\164\1\143\1\162"+
        "\1\uffff\1\172\1\151\2\172\1\162\1\144\2\145\1\151\1\145\1\154\1"+
        "\156\2\172\1\160\1\172\1\145\1\uffff\1\166\1\154\2\172\1\151\1\uffff"+
        "\1\166\2\uffff\1\145\1\172\1\154\1\172\1\157\1\172\1\145\1\172\2"+
        "\uffff\1\162\1\uffff\1\172\1\141\1\172\2\uffff\1\166\2\141\1\uffff"+
        "\1\172\1\uffff\1\156\1\uffff\2\172\1\uffff\1\151\1\uffff\1\164\1"+
        "\uffff\1\141\1\164\1\144\1\uffff\1\172\2\uffff\1\166\1\145\1\164"+
        "\1\145\1\163\1\uffff\1\141\1\172\1\145\2\172\1\164\1\uffff\1\172"+
        "\2\uffff\1\145\1\uffff\1\172\1\uffff";
    static final String DFA1_acceptS =
        "\16\uffff\1\33\122\uffff\1\15\24\uffff\1\11\21\uffff\1\3\5\uffff"+
        "\1\12\1\uffff\1\14\1\16\10\uffff\1\30\1\31\1\uffff\1\1\3\uffff\1"+
        "\6\1\7\3\uffff\1\20\1\uffff\1\22\1\uffff\1\24\2\uffff\1\27\1\uffff"+
        "\1\2\1\uffff\1\5\3\uffff\1\21\1\uffff\1\25\1\26\5\uffff\1\23\6\uffff"+
        "\1\4\1\uffff\1\13\1\17\1\uffff\1\10\1\uffff\1\32";
    static final String DFA1_specialS =
        "\u00cd\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\1\1\2\1\3\1\uffff\1\4\1\5\4\uffff\1\6\1\7\1\10\1\11\1\12"+
            "\1\uffff\1\13\1\14\1\15",
            "\1\17",
            "\1\20\2\uffff\1\21",
            "\1\22\23\uffff\1\23",
            "\1\24\2\uffff\1\25",
            "\1\26",
            "\1\27",
            "\1\30",
            "\1\31\5\uffff\1\32",
            "\1\33",
            "\1\34\20\uffff\1\35",
            "\1\36\17\uffff\1\37",
            "\1\40\1\uffff\1\41\2\uffff\1\42\13\uffff\1\43",
            "\1\44",
            "",
            "\1\45",
            "\1\46\3\uffff\1\47",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\60\10\uffff\1\61",
            "\1\62",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "\1\123",
            "\1\124",
            "\1\125",
            "\1\126",
            "\1\127\6\uffff\1\130",
            "\1\131",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\142",
            "\1\143",
            "\1\144",
            "\1\145",
            "\1\146",
            "\1\147",
            "\1\150",
            "\1\151",
            "\1\152",
            "\1\153",
            "\1\154",
            "\1\155",
            "\1\156",
            "\1\157",
            "\1\160",
            "\1\161",
            "\1\162",
            "\1\163",
            "\1\164",
            "\1\165",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\167",
            "\1\170",
            "\1\171",
            "",
            "\1\172",
            "\1\173",
            "\1\174",
            "\1\175",
            "\1\176",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "\1\u0083",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u0089",
            "\1\u008a",
            "\1\u008b",
            "\1\u008c",
            "\1\u008d",
            "",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u008f",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u0092",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u009c",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u009e",
            "",
            "\1\u009f",
            "\1\u00a0",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u00a3",
            "",
            "\1\u00a4",
            "",
            "",
            "\1\u00a5",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u00a7",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u00a9",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u00ab",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\22\16\1\u00ac\7\16",
            "",
            "",
            "\1\u00ae",
            "",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u00b0",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "",
            "",
            "\1\u00b2",
            "\1\u00b3",
            "\1\u00b4",
            "",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "",
            "\1\u00b6",
            "",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "",
            "\1\u00b9",
            "",
            "\1\u00ba",
            "",
            "\1\u00bb",
            "\1\u00bc",
            "\1\u00bd",
            "",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "",
            "",
            "\1\u00bf",
            "\1\u00c0",
            "\1\u00c1",
            "\1\u00c2",
            "\1\u00c3",
            "",
            "\1\u00c4",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u00c6",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "\1\u00c9",
            "",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            "",
            "",
            "\1\u00cb",
            "",
            "\1\16\13\uffff\12\16\7\uffff\32\16\1\uffff\1\16\2\uffff\1\16"+
            "\1\uffff\32\16",
            ""
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( BARRIER | COLLAPSE | COPYIN | COPYPRIVATE | CRITICAL | DEFAULT | DYNAMIC | FST_PRIVATE | FLUSH | GUIDED | LST_PRIVATE | MASTER | NONE | NOWAIT | NUM_THREADS | ORDERED | PARALLEL | PRIVATE | REDUCTION | RUNTIME | SCHEDULE | SECTIONS | SECTION | SHARED | STATIC | THD_PRIVATE | PreprocessorLexer. Tokens );";
        }
    }
 

}