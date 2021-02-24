package org.dwm.dashboard;

import java.util.HashMap;

/**
 *
 * @author Daniel Mikesell
 */
public class CableCalculator {
    public static double[][] reels = {
        {12, 12, 5, 0.5, 0, 0},
        {16, 14, 8, 1, 0, 0},
        {18, 14, 8, 1, 0, 0},
        {21, 16, 10, 1, 0, 0},
        {24, 18, 10, 1.5, 0, 0},
        {27, 18, 12, 1.5, 0, 0},
        {30, 22, 16, 1.5, 0, 0},
        {32, 24, 16, 1.5, 0, 0},
        {36, 22, 18, 2, 0, 0},
        {36, 24, 14, 2, 1, 0},
        {40, 24, 17, 2, 0, 0},
        {45, 28, 21, 2, 1, 0},
        {44, 30, 24, 2, 0, 0},
        {48, 28, 24, 2, 0, 0},
        {50, 32, 24, 2, 1, 0},
        {54, 32, 28, 2, 0, 0},
        {58, 32, 28, 2, 1, 0},
        {60, 36, 24, 2.5, 0, 0},
        {72, 36, 36, 2.5, 0, 0} 
    };
    public CableCalculator() {}
    
    public static String getBWReelSize(String pn, int qty) {
        HashMap<String, int[]> bwHm = new HashMap<>();

        bwHm.put("000", new int[]{0, 0, 0, 0, 	0, 	0, 	0, 	0, 	0, 	903, 	1284, 	1687});
        bwHm.put("750", new int[]{0, 0, 0, 0, 	0, 	0, 	0, 	581, 	0, 	1244, 	1692, 	2460});
        bwHm.put("600", new int[]{0, 0, 0, 0, 	0, 	437, 	630, 	782, 	1215, 	1636, 	2075, 	3053});
        bwHm.put("500", new int[]{0, 0, 0, 0, 	0, 	607, 	867, 	1008, 	1561, 	2078, 	2581, 	3706});
        bwHm.put("400", new int[]{0, 0, 0, 0, 	0, 	835, 	1101, 	1309, 	1873, 	2564, 	3133, 	4411});
        bwHm.put("350", new int[]{0, 0, 0, 0, 	0, 	872, 	1188, 	1361, 	2213, 	2714, 	3525, 	5044});
        bwHm.put("300", new int[]{0, 0, 0, 0, 	757, 	1101, 	1410, 	1649, 	2498, 	3183, 	3845, 	5861});
        bwHm.put("250", new int[]{0, 0, 0, 0, 	905, 	1182, 	1749, 	1767, 	2976, 	3778, 	4397, 	6739});
        bwHm.put("410", new int[]{0, 0, 535, 877, 	1145, 	1486, 	1909, 	2160, 	3497, 	4624, 	5741, 	8005});
        bwHm.put("310", new int[]{0, 0, 682, 1096, 	1414, 	1874, 	2356, 	2664, 	4440, 	5445, 	6694, 	9741});
        bwHm.put("210", new int[]{0, 0, 876, 1339, 	1711, 	2251, 	2851, 	3142, 	5189, 	6451, 	8171, 	12048});
        bwHm.put("110", new int[]{484, 704, 	1093, 	1607, 	2036, 	2724, 	3676, 	4032, 	6327, 	8042, 	9969, 	14376});
        bwHm.put("001", new int[]{518, 754, 	1196, 	1899, 	2389, 	3242, 	4288, 	4712, 	7215, 	9257, 	11402, 	17153});
        bwHm.put("002", new int[]{706, 1100, 	1670, 	2613, 	3252, 	4413, 	5749, 	6231, 	10157, 	12734, 	15800, 	23157});
        bwHm.put("003", new int[]{875, 1313, 	2004, 	3043, 	3770, 	5150, 	6998, 	7586, 	11905, 	15297, 	18506, 	27261});
        bwHm.put("004", new int[]{1089, 1583, 	2369, 	3506, 	4580, 	5944, 	8058, 	9071, 	13986, 	18096, 	22431, 	32346});
        bwHm.put("006", new int[]{1679, 2498, 	3937, 	5795, 	7050, 	9929, 	12865, 	14294, 	22728, 	28651, 	36192, 	52317});
        bwHm.put("008", new int[]{2321, 3311, 	5390, 	7791, 	9802, 	13239, 	18278, 	19884, 	31178, 	39291, 	48957, 	71543});

        double[][] bwReels = {
            {16, 14, 8, 1, 0},
            {18, 14, 8, 1, 0},
            {21, 16, 10, 1, 0},
            {24, 18, 10, 1.5, 0},
            {27, 18, 12, 1.5, 0},
            {30, 22, 16, 1.5, 0},
            {32, 24, 16, 1.5, 0},
            {36, 22, 18, 2, 0},
            {40, 24, 17, 2, 0},
            {44, 30, 24, 2, 0},
            {48, 28, 24, 2, 0},
            {54, 32, 28, 2, 0},
            {60, 36, 24, 2.5, 0},
            {72, 36, 36, 2.5, 0}
        };

        String awg = "";
        int count = 0;
        if(pn.length() == 8) {
                awg = pn.substring(2, 5);
        } else {
                awg = pn.substring(5, 8);
        }

        if(bwHm.containsKey(awg)) {
            int[] arr = bwHm.get(awg);
            for(int i = 0; i < arr.length; i++) {
                int q = arr[i];
                if(qty < q) {
                    return Integer.toString((int) bwReels[count][0]);
                }
                    count++;
                }
            return "60";
        }

        return "99";
    }

    public static String getReelSize(double od, int qty, String type, String pn) {
        //int special = 1;
        double md = 0;
        double gap = 0;
        String r = "99";

        if(type.equals("Fiber")) {
            md = Math.ceil(20 * od); 
        } else if(type.equals("BW")) {
            return getBWReelSize(pn.trim(), qty);
        }

        if(qty < 26) { return "Box";  }

        for (double[] reel : reels) {
            if(reel[2] >= md) {
                if(od > 1) {
                    gap = Math.ceil(2 * od);
                } else {
                    gap = reel[3];
                }
                
                int capacity = calculateCapacity(reel[0], reel[1], reel[2], gap, od);
                
                if(qty < capacity) {							
                    if(reel[4] == 1) {
                        r = (Integer.toString((int) reel[0]) + "*");
                    } else {
                        r = Integer.toString((int) reel[0]);
                    }
                    break;
                }
            }
        }
        return r;
    }

    public static int calculateCapacity(double flange, double traverse, double drum, double gap, double c) {
        flange = (int) (flange - (gap * 2));			
        flange = flange * flange;
        drum = drum * drum;
        c = c * c;

        int t3 = (int) Math.ceil(((flange - drum) * traverse * .019949) / c);
        return (int) Math.ceil(t3 * 3.2808398 * 1);
    }
}


