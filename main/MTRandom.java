package eean_games.main;

import java.util.Date;

public class MTRandom 
{
	 /* Period parameters */
    private final static int MT_N = 624;
    private final static int MT_M = 397;
    private final static long MATRIX_A = 0x9908b0dfL;   /* constant vector a */
    private final static long UPPER_MASK = 0x80000000L; /* most significant w-r bits */
    private final static long LOWER_MASK = 0x7fffffffL; /* least significant r bits */

    static long[] mt = new long[MT_N]; /* the array for the state vector  */
    static int mti = MT_N + 1; /* mti==MT_N+1 means mt[MT_N] is not initialized */


    public static void randInit()
    {
        long s = new Date().getTime();

        mt[0] = s & 0xffffffffL;
        for (mti = 1; mti < MT_N; mti++)
        {
            mt[mti] =
                (1812433253L * (mt[mti - 1] ^ (mt[mti - 1] >> 30)) + (long)mti);

            mt[mti] &= 0xffffffffL;
            /* for >32 bit machines */
        }
    }

    public static int getRand(int a, int b)
    {
        long y;
        long[] mag01 = new long[]{ 0x0L, MATRIX_A };
        /* mag01[x] = x * MATRIX_A  for x=0,1 */

        if (mti >= MT_N)
        { /* generate N words at one time */
            int kk;

            if (mti == MT_N + 1)   /* if randInit() has not been called, */
                randInit();

            for (kk = 0; kk < MT_N - MT_M; kk++)
            {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + MT_M] ^ (y >> 1) ^ mag01[(int)(y & 0x1L)];
            }
            for (; kk < MT_N - 1; kk++)
            {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (MT_M - MT_N)] ^ (y >> 1) ^ mag01[(int)(y & 0x1L)];
            }
            y = (mt[MT_N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[MT_N - 1] = mt[MT_M - 1] ^ (y >> 1) ^ mag01[(int)(y & 0x1L)];

            mti = 0;
        }

        y = mt[mti++];

        /* Tempering */
        y ^= (y >> 11);
        y ^= (y << 7) & 0x9d2c5680L;
        y ^= (y << 15) & 0xefc60000L;
        y ^= (y >> 18);

        return (int)(y % (int)(b - (a - 1))) + a;
    }
}
