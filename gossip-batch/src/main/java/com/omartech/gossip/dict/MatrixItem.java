package com.omartech.gossip.dict;


import java.util.ArrayList;
import java.util.List;

public class MatrixItem {

    int position;

    double value;

    public MatrixItem(int pos, double val) {
        this.position = pos;
        this.value = val;
    }

    public static List<MatrixItem> parseFromLines(String line) {
        String[] group = line.split(";");
        List<MatrixItem> items = new ArrayList<>();
        for (String pair : group) {
            String[] kv = pair.split(":");
            int pos = Integer.parseInt(kv[0]);
            double val = Double.parseDouble(kv[1]);
            items.add(new MatrixItem(pos, val));
        }
        return items;
    }

    public static double dot(List<MatrixItem> itemA, List<MatrixItem> itemB) {
        double total = 0;
        int index = 0;
        for (MatrixItem item : itemA) {
            for (int i = index; i < itemB.size(); i++) {
                MatrixItem current = itemB.get(i);
                if (item.position == current.position) {
                    total += (item.value * current.value);
                } else if (current.position > item.position) {
                    index = i;
                    break;
                }
            }
        }
        return total;
    }

    public String toString() {
        return position + ":" + value;
    }

    public static void debug(List<MatrixItem> m1) {
        for (MatrixItem mi : m1) {
            System.out.println(mi);
        }
    }


}
