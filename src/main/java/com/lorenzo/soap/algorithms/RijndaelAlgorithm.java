package com.lorenzo.soap.algorithms;

import java.io.InputStream;
import java.security.InvalidKeyException;
import java.util.Properties;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@SuppressWarnings("restriction")
public final class RijndaelAlgorithm {
    
    static final String NAME_CIFRADO = "Cifrado_Algorithm";

    public static final int STATE_SERVICES = 9;
    
    public static final int SERVICES_TPV = 11;
    
    static final boolean IN_CIFRADO = true, OUT_CIFRADO = false;

    static final int debuglevel_CIFRADO = 1;

    static final int KEYSIZE_CIFRADO = 32;

    static final String PATH = "PRUEBARIUCIFRADO";
          
    static final int BLOCK_SIZE_CIFRADO = 16; // default block size in bytes

    static final int[] alog_CIFRADO = new int[256];

    static final int[] log_CIFRADO = new int[256];

    static final byte[] S_CIFRADO = new byte[256];

    static final byte[] Si_CIFRADO = new byte[256];

    static final int[] T1_CIFRADO = new int[256];

    static final int[] T2_CIFRADO = new int[256];

    static final int[] T3_CIFRADO = new int[256];

    static final int[] T4_CIFRADO = new int[256];

    static final int[] T5_CIFRADO = new int[256];

    static final int[] T6_CIFRADO = new int[256];

    static final int[] T7_CIFRADO = new int[256];

    static final int[] T8_CIFRADO = new int[256];

    static final int[] U1_CIFRADO = new int[256];

    static final int[] U2_CIFRADO = new int[256];

    static final int[] U3_CIFRADO = new int[256];

    static final int[] U4_CIFRADO = new int[256];

    static final byte[] rcon_CIFRADO = new byte[30];

    static final int[][][] shifts_CIFRADO = new int[][][] {
            { { 0, 0 }, { 1, 3 }, { 2, 2 }, { 3, 1 } },
            { { 0, 0 }, { 1, 5 }, { 2, 4 }, { 3, 3 } },
            { { 0, 0 }, { 1, 7 }, { 3, 5 }, { 4, 4 } } };

    static {
        long time = System.currentTimeMillis();

        int ROOT = 0x11B;
        int i, j = 0;

        alog_CIFRADO[0] = 1;
        for (i = 1; i < 256; i++) {
            j = (alog_CIFRADO[i - 1] << 1) ^ alog_CIFRADO[i - 1];
            if ((j & 0x100) != 0)
                j ^= ROOT;
            alog_CIFRADO[i] = j;
        }
        for (i = 1; i < 255; i++)
            log_CIFRADO[alog_CIFRADO[i]] = i;
        byte[][] A = new byte[][] { { 1, 1, 1, 1, 1, 0, 0, 0 },
                { 0, 1, 1, 1, 1, 1, 0, 0 }, { 0, 0, 1, 1, 1, 1, 1, 0 },
                { 0, 0, 0, 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 1, 1, 1, 1 },
                { 1, 1, 0, 0, 0, 1, 1, 1 }, { 1, 1, 1, 0, 0, 0, 1, 1 },
                { 1, 1, 1, 1, 0, 0, 0, 1 } };
        byte[] B = new byte[] { 0, 1, 1, 0, 0, 0, 1, 1 };

        //
        // substitution box based on F^{-1}(x)
        //
        int t;
        byte[][] box = new byte[256][8];
        box[1][7] = 1;
        for (i = 2; i < 256; i++) {
            j = alog_CIFRADO[255 - log_CIFRADO[i]];
            for (t = 0; t < 8; t++)
                box[i][t] = (byte) ((j >>> (7 - t)) & 0x01);
        }
        //
        // affine transform: box[i] <- B + A*box[i]
        //
        byte[][] cox = new byte[256][8];
        for (i = 0; i < 256; i++)
            for (t = 0; t < 8; t++) {
                cox[i][t] = B[t];
                for (j = 0; j < 8; j++)
                    cox[i][t] ^= A[t][j] * box[i][j];
            }
        //
        // S-boxes and inverse S-boxes
        //
        for (i = 0; i < 256; i++) {
            S_CIFRADO[i] = (byte) (cox[i][0] << 7);
            for (t = 1; t < 8; t++)
                S_CIFRADO[i] ^= cox[i][t] << (7 - t);
            Si_CIFRADO[S_CIFRADO[i] & 0xFF] = (byte) i;
        }
        //
        // T-boxes
        //
        byte[][] G = new byte[][] { { 2, 1, 1, 3 }, { 3, 2, 1, 1 },
                { 1, 3, 2, 1 }, { 1, 1, 3, 2 } };
        byte[][] AA = new byte[4][8];
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++)
                AA[i][j] = G[i][j];
            AA[i][i + 4] = 1;
        }
        byte pivot, tmp;
        byte[][] iG = new byte[4][4];
        for (i = 0; i < 4; i++) {
            pivot = AA[i][i];
            if (pivot == 0) {
                t = i + 1;
                while ((AA[t][i] == 0) && (t < 4))
                    t++;
                if (t == 4)
                    throw new RuntimeException("G matrix is not invertible");
                else {
                    for (j = 0; j < 8; j++) {
                        tmp = AA[i][j];
                        AA[i][j] = AA[t][j];
                        AA[t][j] = (byte) tmp;
                    }
                    pivot = AA[i][i];
                }
            }
            for (j = 0; j < 8; j++)
                if (AA[i][j] != 0)
                    AA[i][j] = (byte) alog_CIFRADO[(255 + log_CIFRADO[AA[i][j] & 0xFF] - log_CIFRADO[pivot & 0xFF]) % 255];
            for (t = 0; t < 4; t++)
                if (i != t) {
                    for (j = i + 1; j < 8; j++)
                        AA[t][j] ^= mul_CIFRADO(AA[i][j], AA[t][i]);
                    AA[t][i] = 0;
                }
        }
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++)
                iG[i][j] = AA[i][j + 4];

        int s;
        for (t = 0; t < 256; t++) {
            s = S_CIFRADO[t];
            T1_CIFRADO[t] = mul4_CIFRADO(s, G[0]);
            T2_CIFRADO[t] = mul4_CIFRADO(s, G[1]);
            T3_CIFRADO[t] = mul4_CIFRADO(s, G[2]);
            T4_CIFRADO[t] = mul4_CIFRADO(s, G[3]);

            s = Si_CIFRADO[t];
            T5_CIFRADO[t] = mul4_CIFRADO(s, iG[0]);
            T6_CIFRADO[t] = mul4_CIFRADO(s, iG[1]);
            T7_CIFRADO[t] = mul4_CIFRADO(s, iG[2]);
            T8_CIFRADO[t] = mul4_CIFRADO(s, iG[3]);

            U1_CIFRADO[t] = mul4_CIFRADO(t, iG[0]);
            U2_CIFRADO[t] = mul4_CIFRADO(t, iG[1]);
            U3_CIFRADO[t] = mul4_CIFRADO(t, iG[2]);
            U4_CIFRADO[t] = mul4_CIFRADO(t, iG[3]);
        }
        //
        // round constants
        //
        rcon_CIFRADO[0] = 1;
        int r = 1;
        for (t = 1; t < 30;)
            rcon_CIFRADO[t++] = (byte) (r = mul_CIFRADO(2, r));

        time = System.currentTimeMillis() - time;
    }

    // multiply two elements of GF(2^m)
    static final int mul_CIFRADO(int a, int b) {
        return (a != 0 && b != 0) ? alog_CIFRADO[(log_CIFRADO[a & 0xFF] + log_CIFRADO[b & 0xFF]) % 255]
                : 0;
    }

    // convenience method used in generating Transposition boxes
    static final int mul4_CIFRADO(int a, byte[] b) {
        if (a == 0)
            return 0;
        a = log_CIFRADO[a & 0xFF];
        int a0 = (b[0] != 0) ? alog_CIFRADO[(a + log_CIFRADO[b[0] & 0xFF]) % 255] & 0xFF
                : 0;
        int a1 = (b[1] != 0) ? alog_CIFRADO[(a + log_CIFRADO[b[1] & 0xFF]) % 255] & 0xFF
                : 0;
        int a2 = (b[2] != 0) ? alog_CIFRADO[(a + log_CIFRADO[b[2] & 0xFF]) % 255] & 0xFF
                : 0;
        int a3 = (b[3] != 0) ? alog_CIFRADO[(a + log_CIFRADO[b[3] & 0xFF]) % 255] & 0xFF
                : 0;
        return a0 << 24 | a1 << 16 | a2 << 8 | a3;
    }

    public static Object makeKey_CIFRADO(byte[] k) throws InvalidKeyException {
        return makeKey_CIFRADO(k, BLOCK_SIZE_CIFRADO);
    }

    public static byte[] blockEncrypt_CIFRADO(byte[] in, int inOffset,
            Object sessionKey) {
        
        int[][] Ke = (int[][]) ((Object[]) sessionKey)[0]; // extract encryption
        // round keys
        int ROUNDS = Ke.length - 1;
        int[] Ker = Ke[0];

        // plaintext to ints + key
        int t0 = ((in[inOffset++] & 0xFF) << 24 | (in[inOffset++] & 0xFF) << 16
                | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF))
                ^ Ker[0];
        int t1 = ((in[inOffset++] & 0xFF) << 24 | (in[inOffset++] & 0xFF) << 16
                | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF))
                ^ Ker[1];
        int t2 = ((in[inOffset++] & 0xFF) << 24 | (in[inOffset++] & 0xFF) << 16
                | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF))
                ^ Ker[2];
        int t3 = ((in[inOffset++] & 0xFF) << 24 | (in[inOffset++] & 0xFF) << 16
                | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF))
                ^ Ker[3];

        int a0, a1, a2, a3;
        for (int r = 1; r < ROUNDS; r++) { // apply round transforms
            Ker = Ke[r];
            a0 = (T1_CIFRADO[(t0 >>> 24) & 0xFF]
                    ^ T2_CIFRADO[(t1 >>> 16) & 0xFF]
                    ^ T3_CIFRADO[(t2 >>> 8) & 0xFF] ^ T4_CIFRADO[t3 & 0xFF])
                    ^ Ker[0];
            a1 = (T1_CIFRADO[(t1 >>> 24) & 0xFF]
                    ^ T2_CIFRADO[(t2 >>> 16) & 0xFF]
                    ^ T3_CIFRADO[(t3 >>> 8) & 0xFF] ^ T4_CIFRADO[t0 & 0xFF])
                    ^ Ker[1];
            a2 = (T1_CIFRADO[(t2 >>> 24) & 0xFF]
                    ^ T2_CIFRADO[(t3 >>> 16) & 0xFF]
                    ^ T3_CIFRADO[(t0 >>> 8) & 0xFF] ^ T4_CIFRADO[t1 & 0xFF])
                    ^ Ker[2];
            a3 = (T1_CIFRADO[(t3 >>> 24) & 0xFF]
                    ^ T2_CIFRADO[(t0 >>> 16) & 0xFF]
                    ^ T3_CIFRADO[(t1 >>> 8) & 0xFF] ^ T4_CIFRADO[t2 & 0xFF])
                    ^ Ker[3];
            t0 = a0;
            t1 = a1;
            t2 = a2;
            t3 = a3;
        }

        // last round is special
        byte[] result = new byte[BLOCK_SIZE_CIFRADO]; // the resulting
        // ciphertext
        Ker = Ke[ROUNDS];
        int tt = Ker[0];
        result[0] = (byte) (S_CIFRADO[(t0 >>> 24) & 0xFF] ^ (tt >>> 24));
        result[1] = (byte) (S_CIFRADO[(t1 >>> 16) & 0xFF] ^ (tt >>> 16));
        result[2] = (byte) (S_CIFRADO[(t2 >>> 8) & 0xFF] ^ (tt >>> 8));
        result[3] = (byte) (S_CIFRADO[t3 & 0xFF] ^ tt);
        tt = Ker[1];
        result[4] = (byte) (S_CIFRADO[(t1 >>> 24) & 0xFF] ^ (tt >>> 24));
        result[5] = (byte) (S_CIFRADO[(t2 >>> 16) & 0xFF] ^ (tt >>> 16));
        result[6] = (byte) (S_CIFRADO[(t3 >>> 8) & 0xFF] ^ (tt >>> 8));
        result[7] = (byte) (S_CIFRADO[t0 & 0xFF] ^ tt);
        tt = Ker[2];
        result[8] = (byte) (S_CIFRADO[(t2 >>> 24) & 0xFF] ^ (tt >>> 24));
        result[9] = (byte) (S_CIFRADO[(t3 >>> 16) & 0xFF] ^ (tt >>> 16));
        result[10] = (byte) (S_CIFRADO[(t0 >>> 8) & 0xFF] ^ (tt >>> 8));
        result[11] = (byte) (S_CIFRADO[t1 & 0xFF] ^ tt);
        tt = Ker[3];
        result[12] = (byte) (S_CIFRADO[(t3 >>> 24) & 0xFF] ^ (tt >>> 24));
        result[13] = (byte) (S_CIFRADO[(t0 >>> 16) & 0xFF] ^ (tt >>> 16));
        result[14] = (byte) (S_CIFRADO[(t1 >>> 8) & 0xFF] ^ (tt >>> 8));
        result[15] = (byte) (S_CIFRADO[t2 & 0xFF] ^ tt);
        
        return result;
    }

    public static byte[] blockDecrypt_CIFRADO(byte[] in, int inOffset,
            Object sessionKey) {
        
    	int[][] Kd = (int[][]) ((Object[]) sessionKey)[1]; // extract decryption
        // round keys
        int ROUNDS = Kd.length - 1;
        int[] Kdr = Kd[0];

        // ciphertext to ints + key
        int t0 = ((in[inOffset++] & 0xFF) << 24 | (in[inOffset++] & 0xFF) << 16
                | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF))
                ^ Kdr[0];
        int t1 = ((in[inOffset++] & 0xFF) << 24 | (in[inOffset++] & 0xFF) << 16
                | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF))
                ^ Kdr[1];
        int t2 = ((in[inOffset++] & 0xFF) << 24 | (in[inOffset++] & 0xFF) << 16
                | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF))
                ^ Kdr[2];
        int t3 = ((in[inOffset++] & 0xFF) << 24 | (in[inOffset++] & 0xFF) << 16
                | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF))
                ^ Kdr[3];

        int a0, a1, a2, a3;
        for (int r = 1; r < ROUNDS; r++) { // apply round transforms
            Kdr = Kd[r];
            a0 = (T5_CIFRADO[(t0 >>> 24) & 0xFF]
                    ^ T6_CIFRADO[(t3 >>> 16) & 0xFF]
                    ^ T7_CIFRADO[(t2 >>> 8) & 0xFF] ^ T8_CIFRADO[t1 & 0xFF])
                    ^ Kdr[0];
            a1 = (T5_CIFRADO[(t1 >>> 24) & 0xFF]
                    ^ T6_CIFRADO[(t0 >>> 16) & 0xFF]
                    ^ T7_CIFRADO[(t3 >>> 8) & 0xFF] ^ T8_CIFRADO[t2 & 0xFF])
                    ^ Kdr[1];
            a2 = (T5_CIFRADO[(t2 >>> 24) & 0xFF]
                    ^ T6_CIFRADO[(t1 >>> 16) & 0xFF]
                    ^ T7_CIFRADO[(t0 >>> 8) & 0xFF] ^ T8_CIFRADO[t3 & 0xFF])
                    ^ Kdr[2];
            a3 = (T5_CIFRADO[(t3 >>> 24) & 0xFF]
                    ^ T6_CIFRADO[(t2 >>> 16) & 0xFF]
                    ^ T7_CIFRADO[(t1 >>> 8) & 0xFF] ^ T8_CIFRADO[t0 & 0xFF])
                    ^ Kdr[3];
            t0 = a0;
            t1 = a1;
            t2 = a2;
            t3 = a3;
        }

        // last round is special
        byte[] result = new byte[16]; // the resulting plaintext
        Kdr = Kd[ROUNDS];
        int tt = Kdr[0];
        result[0] = (byte) (Si_CIFRADO[(t0 >>> 24) & 0xFF] ^ (tt >>> 24));
        result[1] = (byte) (Si_CIFRADO[(t3 >>> 16) & 0xFF] ^ (tt >>> 16));
        result[2] = (byte) (Si_CIFRADO[(t2 >>> 8) & 0xFF] ^ (tt >>> 8));
        result[3] = (byte) (Si_CIFRADO[t1 & 0xFF] ^ tt);
        tt = Kdr[1];
        result[4] = (byte) (Si_CIFRADO[(t1 >>> 24) & 0xFF] ^ (tt >>> 24));
        result[5] = (byte) (Si_CIFRADO[(t0 >>> 16) & 0xFF] ^ (tt >>> 16));
        result[6] = (byte) (Si_CIFRADO[(t3 >>> 8) & 0xFF] ^ (tt >>> 8));
        result[7] = (byte) (Si_CIFRADO[t2 & 0xFF] ^ tt);
        tt = Kdr[2];
        result[8] = (byte) (Si_CIFRADO[(t2 >>> 24) & 0xFF] ^ (tt >>> 24));
        result[9] = (byte) (Si_CIFRADO[(t1 >>> 16) & 0xFF] ^ (tt >>> 16));
        result[10] = (byte) (Si_CIFRADO[(t0 >>> 8) & 0xFF] ^ (tt >>> 8));
        result[11] = (byte) (Si_CIFRADO[t3 & 0xFF] ^ tt);
        tt = Kdr[3];
        result[12] = (byte) (Si_CIFRADO[(t3 >>> 24) & 0xFF] ^ (tt >>> 24));
        result[13] = (byte) (Si_CIFRADO[(t2 >>> 16) & 0xFF] ^ (tt >>> 16));
        result[14] = (byte) (Si_CIFRADO[(t1 >>> 8) & 0xFF] ^ (tt >>> 8));
        result[15] = (byte) (Si_CIFRADO[t0 & 0xFF] ^ tt);
        
        return result;
    }

    /** A basic symmetric encryption/decryption test. */
    public static boolean self_test_CIFRADO() {
        return self_test_CIFRADO(BLOCK_SIZE_CIFRADO);
    }

    public static int blockSize_CIFRADO() {
        return BLOCK_SIZE_CIFRADO;
    }

    public static synchronized Object makeKey_CIFRADO(byte[] k, int blockSize)
            throws InvalidKeyException {
        
        if (k == null)
            throw new InvalidKeyException("Llave vacia");
        if (!(k.length == 16 || k.length == 24 || k.length == 32 || k.length == 128))
            throw new InvalidKeyException("Llave de cifrado de tamaño invalido.");
        int ROUNDS = getRounds_CIFRADO(k.length, blockSize);
        int BC = blockSize / 4;
        int[][] Ke = new int[ROUNDS + 1][BC]; // encryption round keys
        int[][] Kd = new int[ROUNDS + 1][BC]; // decryption round keys
        int ROUND_KEY_COUNT = (ROUNDS + 1) * BC;
        int KC = k.length / 4;
        int[] tk = new int[KC];
        int i, j;

        // copy user material bytes into temporary ints
        for (i = 0, j = 0; i < KC;)
            tk[i++] = (k[j++] & 0xFF) << 24 | (k[j++] & 0xFF) << 16
                    | (k[j++] & 0xFF) << 8 | (k[j++] & 0xFF);
        // copy values into round key arrays
        int t = 0;
        for (j = 0; (j < KC) && (t < ROUND_KEY_COUNT); j++, t++) {
            Ke[t / BC][t % BC] = tk[j];
            Kd[ROUNDS - (t / BC)][t % BC] = tk[j];
        }
        int tt, rconpointer = 0;
        while (t < ROUND_KEY_COUNT) {
            // extrapolate using phi (the round key evolution function)
            tt = tk[KC - 1];
            tk[0] ^= (S_CIFRADO[(tt >>> 16) & 0xFF] & 0xFF) << 24
                    ^ (S_CIFRADO[(tt >>> 8) & 0xFF] & 0xFF) << 16
                    ^ (S_CIFRADO[tt & 0xFF] & 0xFF) << 8
                    ^ (S_CIFRADO[(tt >>> 24) & 0xFF] & 0xFF)
                    ^ (rcon_CIFRADO[rconpointer++] & 0xFF) << 24;
            if (KC != 8)
                for (i = 1, j = 0; i < KC;)
                    tk[i++] ^= tk[j++];
            else {
                for (i = 1, j = 0; i < KC / 2;)
                    tk[i++] ^= tk[j++];
                tt = tk[KC / 2 - 1];
                tk[KC / 2] ^= (S_CIFRADO[tt & 0xFF] & 0xFF)
                        ^ (S_CIFRADO[(tt >>> 8) & 0xFF] & 0xFF) << 8
                        ^ (S_CIFRADO[(tt >>> 16) & 0xFF] & 0xFF) << 16
                        ^ (S_CIFRADO[(tt >>> 24) & 0xFF] & 0xFF) << 24;
                for (j = KC / 2, i = j + 1; i < KC;)
                    tk[i++] ^= tk[j++];
            }
            // copy values into round key arrays
            for (j = 0; (j < KC) && (t < ROUND_KEY_COUNT); j++, t++) {
                Ke[t / BC][t % BC] = tk[j];
                Kd[ROUNDS - (t / BC)][t % BC] = tk[j];
            }
        }
        for (int r = 1; r < ROUNDS; r++)
            // inverse MixColumn where needed
            for (j = 0; j < BC; j++) {
                tt = Kd[r][j];
                Kd[r][j] = U1_CIFRADO[(tt >>> 24) & 0xFF]
                        ^ U2_CIFRADO[(tt >>> 16) & 0xFF]
                        ^ U3_CIFRADO[(tt >>> 8) & 0xFF] ^ U4_CIFRADO[tt & 0xFF];
            }
        Object[] sessionKey = new Object[] { Ke, Kd };
        
        return sessionKey;
    }

    public static byte[] blockEncrypt_CIFRADO(byte[] in, int inOffset,
            Object sessionKey, int blockSize) {
        if (blockSize == BLOCK_SIZE_CIFRADO)
            return blockEncrypt_CIFRADO(in, inOffset, sessionKey);
        
        Object[] sKey = (Object[]) sessionKey; // extract encryption round keys
        int[][] Ke = (int[][]) sKey[0];

        int BC = blockSize / 4;
        int ROUNDS = Ke.length - 1;
        int SC = BC == 4 ? 0 : (BC == 6 ? 1 : 2);
        int s1 = shifts_CIFRADO[SC][1][0];
        int s2 = shifts_CIFRADO[SC][2][0];
        int s3 = shifts_CIFRADO[SC][3][0];
        int[] a = new int[BC];
        int[] t = new int[BC]; // temporary work array
        int i;
        byte[] result = new byte[blockSize]; // the resulting ciphertext
        int j = 0, tt;

        for (i = 0; i < BC; i++)
            // plaintext to ints + key
            t[i] = ((in[inOffset++] & 0xFF) << 24
                    | (in[inOffset++] & 0xFF) << 16
                    | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF))
                    ^ Ke[0][i];
        for (int r = 1; r < ROUNDS; r++) { // apply round transforms
            for (i = 0; i < BC; i++)
                a[i] = (T1_CIFRADO[(t[i] >>> 24) & 0xFF]
                        ^ T2_CIFRADO[(t[(i + s1) % BC] >>> 16) & 0xFF]
                        ^ T3_CIFRADO[(t[(i + s2) % BC] >>> 8) & 0xFF] ^ T4_CIFRADO[t[(i + s3)
                        % BC] & 0xFF])
                        ^ Ke[r][i];
            System.arraycopy(a, 0, t, 0, BC);
        }
        for (i = 0; i < BC; i++) { // last round is special
            tt = Ke[ROUNDS][i];
            result[j++] = (byte) (S_CIFRADO[(t[i] >>> 24) & 0xFF] ^ (tt >>> 24));
            result[j++] = (byte) (S_CIFRADO[(t[(i + s1) % BC] >>> 16) & 0xFF] ^ (tt >>> 16));
            result[j++] = (byte) (S_CIFRADO[(t[(i + s2) % BC] >>> 8) & 0xFF] ^ (tt >>> 8));
            result[j++] = (byte) (S_CIFRADO[t[(i + s3) % BC] & 0xFF] ^ tt);
        }
        
        return result;
    }

    public static byte[] blockDecrypt_CIFRADO(byte[] in, int inOffset,
            Object sessionKey, int blockSize) {
        if (blockSize == BLOCK_SIZE_CIFRADO)
            return blockDecrypt_CIFRADO(in, inOffset, sessionKey);
        
        Object[] sKey = (Object[]) sessionKey; // extract decryption round keys
        int[][] Kd = (int[][]) sKey[1];

        int BC = blockSize / 4;
        int ROUNDS = Kd.length - 1;
        int SC = BC == 4 ? 0 : (BC == 6 ? 1 : 2);
        int s1 = shifts_CIFRADO[SC][1][1];
        int s2 = shifts_CIFRADO[SC][2][1];
        int s3 = shifts_CIFRADO[SC][3][1];
        int[] a = new int[BC];
        int[] t = new int[BC]; // temporary work array
        int i;
        byte[] result = new byte[blockSize]; // the resulting plaintext
        int j = 0, tt;

        for (i = 0; i < BC; i++)
            // ciphertext to ints + key
            t[i] = ((in[inOffset++] & 0xFF) << 24
                    | (in[inOffset++] & 0xFF) << 16
                    | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF))
                    ^ Kd[0][i];
        for (int r = 1; r < ROUNDS; r++) { // apply round transforms
            for (i = 0; i < BC; i++)
                a[i] = (T5_CIFRADO[(t[i] >>> 24) & 0xFF]
                        ^ T6_CIFRADO[(t[(i + s1) % BC] >>> 16) & 0xFF]
                        ^ T7_CIFRADO[(t[(i + s2) % BC] >>> 8) & 0xFF] ^ T8_CIFRADO[t[(i + s3)
                        % BC] & 0xFF])
                        ^ Kd[r][i];
            System.arraycopy(a, 0, t, 0, BC);
        }
        for (i = 0; i < BC; i++) { // last round is special
            tt = Kd[ROUNDS][i];
            result[j++] = (byte) (Si_CIFRADO[(t[i] >>> 24) & 0xFF] ^ (tt >>> 24));
            result[j++] = (byte) (Si_CIFRADO[(t[(i + s1) % BC] >>> 16) & 0xFF] ^ (tt >>> 16));
            result[j++] = (byte) (Si_CIFRADO[(t[(i + s2) % BC] >>> 8) & 0xFF] ^ (tt >>> 8));
            result[j++] = (byte) (Si_CIFRADO[t[(i + s3) % BC] & 0xFF] ^ tt);
        }
        return result;
    }

    /** A basic symmetric encryption/decryption test for a given key size. */
    private static boolean self_test_CIFRADO(int keysize) {
        
        boolean ok = false;
        try {
            byte[] kb = new byte[keysize];
            byte[] pt = new byte[BLOCK_SIZE_CIFRADO];
            int i;

            for (i = 0; i < keysize; i++)
                kb[i] = (byte) i;
            for (i = 0; i < BLOCK_SIZE_CIFRADO; i++)
                pt[i] = (byte) i;

            Object key = makeKey_CIFRADO(kb, BLOCK_SIZE_CIFRADO);

            byte[] ct = blockEncrypt_CIFRADO(pt, 0, key, BLOCK_SIZE_CIFRADO);

            byte[] cpt = blockDecrypt_CIFRADO(ct, 0, key, BLOCK_SIZE_CIFRADO);

            ok = areEqual_CIFRADO(pt, cpt);
            if (!ok)
                throw new RuntimeException("Symmetric operation failed");
        } catch (Exception x) {
                x.printStackTrace();

        }
        
        return ok;
    }

    public static int getRounds_CIFRADO(int keySize, int blockSize) {
        switch (keySize) {
        case 16:
            return blockSize == 16 ? 10 : (blockSize == 24 ? 12 : 14);
        case 24:
            return blockSize != 32 ? 12 : 14;
        default: // 32 bytes = 256 bits
            return 14;
        }
    }

    private static boolean areEqual_CIFRADO(byte[] a, byte[] b) {
        int aLength = a.length;
        if (aLength != b.length)
            return false;
        for (int i = 0; i < aLength; i++)
            if (a[i] != b[i])
                return false;
        return true;
    }

    private static int getNumBlocks_CIFRADO(int sizeString, int lengthBlock) {
        int numBloques = sizeString / lengthBlock;
        if (sizeString % lengthBlock > 0) {
            numBloques++;
        }
        return numBloques;
    }

    private static String[] setBlocks_CIFRADO(int numBlocks, String cadena,
            int lengthBlock) {
        String arrayBlocks[] = new String[numBlocks];
        for (int i = 0; i < numBlocks; i++) {
            if (i == numBlocks - 1) {
                arrayBlocks[i] = cadena.substring(i * lengthBlock, cadena
                        .length());
                int t = lengthBlock - arrayBlocks[i].length();
                for (int j = 0; j < t; j++) {
                    arrayBlocks[i] += " ";
                }
            } else {
                arrayBlocks[i] = cadena.substring(i * lengthBlock, lengthBlock
                        * (i + 1));
            }
        }
        return arrayBlocks;
    }
  
    private static byte[] encryptBlock_CIFRADO(String cadena, byte[] keycode) {
        byte[] ct;
        try {
            byte[] kb = keycode;
            byte[] pt = cadena.getBytes();
            Object key = makeKey_CIFRADO(kb, BLOCK_SIZE_CIFRADO);

            ct = blockEncrypt_CIFRADO(pt, 0, key, BLOCK_SIZE_CIFRADO);

        } catch (Exception x) {
            
                x.printStackTrace();
            throw new RuntimeException("Fallo en la operacion de encripcion: " + x.getMessage());
        }
        return ct;
    }
    
    
	private static String setEncrypt(String stringToEncrypt, byte[] keycode) {
        StringBuffer cadEncriptada = new StringBuffer();
        String[] blocksCadena;

        int numBloques = getNumBlocks_CIFRADO(stringToEncrypt.length(),
                BLOCK_SIZE_CIFRADO);

        blocksCadena = setBlocks_CIFRADO(numBloques, stringToEncrypt,
                BLOCK_SIZE_CIFRADO);

        for (int i = 0; i < numBloques; i++) {
            cadEncriptada.append(Base64.encode(encryptBlock_CIFRADO(blocksCadena[i], keycode)).trim());
        }
        return cadEncriptada.toString().trim();
    }
	
    public static String setEncrypt(String stringToEncrypt, String key) throws Exception {
		String cifrado = "";
		int keycode = 9; 
		if (stringToEncrypt == null || stringToEncrypt.equals(""))
			cifrado = stringToEncrypt;
		if ((keycode == STATE_SERVICES) || (keycode == SERVICES_TPV)) {

			byte[] kode = charArrayToByteArray( key.toCharArray() );
			
			cifrado = setEncrypt(stringToEncrypt, kode );
		}

		return cifrado;

	}
    
    private static byte[] charArrayToByteArray(char[] c_array) {
        byte[] b_array = new byte[c_array.length];
        for(int i= 0; i < c_array.length; i++) {
            b_array[i] = (byte)(0xFF & (int)c_array[i]);
        }
        return b_array;
    }



    public static String setEncrypt(String stringToEncrypt, int keycode)
            throws Exception {
    	String cifrado = "";
        if (stringToEncrypt == null || stringToEncrypt.equals(""))
        	cifrado = stringToEncrypt;
        if ((keycode == STATE_SERVICES) || (keycode==SERVICES_TPV)) {
            String key = llaveMultipagosWS(keycode);            
            cifrado = setEncrypt(stringToEncrypt, key);
        }
        
        return cifrado;

    }
    
    private static String llaveMultipagosWS(int keycode) throws Exception{
    	InputStream fis = RijndaelAlgorithm.class.getResourceAsStream("Rijndael.properties");
    	Properties prop = new Properties();
    	prop.load(fis);
    	fis.close();    
    	if (keycode ==STATE_SERVICES) { return prop.getProperty("KEY_WS"); }
    	if (keycode ==SERVICES_TPV) { return prop.getProperty("KEY_TPV"); }
    	
        return null;
    }

	public static String setDesEncrypt(String stringToDesencrypt) {
        StringBuffer cadDesencriptada = new StringBuffer();
        String[] blocksCadena;
        byte[] b;

        int numBloques = getNumBlocks_CIFRADO(stringToDesencrypt.length(),
                24);
        blocksCadena = setBlocks_CIFRADO(numBloques, stringToDesencrypt, 24);
        for (int i = 0; i < numBloques; i++) {
			b = Base64.decode(blocksCadena[i]);
			b = desEncryptBlock_CIFRADO(b);
			String ac = new String(b);
			cadDesencriptada.append(ac);
        }

        return cadDesencriptada.toString().trim();
    }

	public static String setDesEncrypt(String stringToDesencrypt, String keycode) {
        StringBuffer cadDesencriptada = new StringBuffer();
        String[] blocksCadena;

        int numBloques = getNumBlocks_CIFRADO(stringToDesencrypt.length(), 24);
        blocksCadena = setBlocks_CIFRADO(numBloques, stringToDesencrypt, 24);
        byte[] b;
        for (int i = 0; i < numBloques; i++) {
			b = Base64.decode(blocksCadena[i]);
			b = desEncryptBlock_CIFRADO(b, keycode);
			String ac = new String(b);
			cadDesencriptada.append(ac);
        }

        return cadDesencriptada.toString().trim();
    }

    
    public static String setDesEncrypt(String stringToDesencrypt, int keycode)
            throws Exception {
        if (stringToDesencrypt == null || stringToDesencrypt.equals(""))
            return stringToDesencrypt;
        if((keycode == STATE_SERVICES)|| (keycode == SERVICES_TPV)){
        	String key = llaveMultipagosWS(keycode);
        	return setDesEncrypt(stringToDesencrypt, key );
        }else{

        }
        
        return "";
    }


    private static byte[] desEncryptBlock_CIFRADO(byte[] ct) {
        byte[] cpt;
        try {
        	byte[] kb = {(byte)'R', (byte)'?', (byte)'`', (byte)':', (byte)'#' ,(byte)'*',
                    (byte)'-', (byte)'´', (byte)'Ö', (byte)'.', (byte)'Ú', (byte)'ð',
                    (byte)'µ', (byte)'©', (byte)'å', (byte)'s'};

            Object key = makeKey_CIFRADO(kb, BLOCK_SIZE_CIFRADO);
            cpt = blockDecrypt_CIFRADO(ct, 0, key, BLOCK_SIZE_CIFRADO);
        } catch (Exception x) {
            
            throw new RuntimeException("Fallo en la operacion de encripcion.");
        }
        return cpt;
    }


    private static byte[] desEncryptBlock_CIFRADO(byte[] ct, String keycode) {
        byte[] cpt;
        try {
            byte[] kb = keycode.getBytes();
            Object key = makeKey_CIFRADO(kb, BLOCK_SIZE_CIFRADO);
            cpt = blockDecrypt_CIFRADO(ct, 0, key, BLOCK_SIZE_CIFRADO);
        } catch (Exception x) {
            
            throw new RuntimeException("Fallo en la operacion de encripcion.");
        }
        return cpt;
    }
       
}