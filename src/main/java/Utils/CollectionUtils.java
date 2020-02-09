package Utils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CollectionUtils {

    public static final Comparator<Double> BID_COMPERATOR = (a, b) ->  Double.compare(b, a);
    public static final Comparator<Double> ASK_COMPERATOR = (a, b) ->  Double.compare(a, b);

    public static void mergeTwoSortedArrays(List<Double> sortedList1, Map<Double, Double> list1Map,
                                   List<Double> sortedList2, Map<Double, Double> list2Map,
                                   List<Double> allSortedPrices, Map<Double, Double> allPrices, Comparator<Double> comparator) {

        if(sortedList1.isEmpty() && sortedList2.isEmpty()) {
            return;
        }
        if(sortedList1.isEmpty()) {
            addRemaining(sortedList2, list2Map, allSortedPrices, allPrices, 0);
            return;
        }
        if(sortedList2.isEmpty()) {
            addRemaining(sortedList1, list1Map, allSortedPrices, allPrices, 0);
            return;
        }
        int i = 0;
        int j = 0;
        while(true) {
            if (i == sortedList1.size()) {
                addRemaining(sortedList2, list2Map, allSortedPrices, allPrices, j);
                break;
            }
            if (j == sortedList2.size()) {
                addRemaining(sortedList1, list1Map, allSortedPrices, allPrices, i);
                break;
            }
            double betterPrice = comparator.compare(sortedList1.get(i), sortedList2.get(j)) < 0 ? sortedList1.get(i) : sortedList2.get(j);
            allSortedPrices.add(betterPrice);
            allPrices.put(betterPrice, list1Map.getOrDefault(betterPrice, 0d) + list2Map.getOrDefault(betterPrice, 0d));
            i += list1Map.containsKey(betterPrice) ? 1 : 0;
            j += list2Map.containsKey(betterPrice) ? 1 : 0;
        }

    }

    private static void addRemaining(List<Double> list, Map<Double, Double> map,
                                     List<Double> newList, Map<Double, Double> newMap, int index) {
            list.subList(index, list.size()).
                    forEach(e -> {
                        newList.add(e);
                        newMap.put(e, map.get(e));
                    });
    }
}
