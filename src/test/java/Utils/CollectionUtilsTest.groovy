package Utils

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import junit.framework.TestCase

import java.util.function.Function
import java.util.stream.Collectors

class CollectionUtilsTest extends TestCase {

    private List<Double> sortedList1 = List.of(1d, 4d, 7d)
    private Map<Double, Double> list1Map = sortedList1.stream().collect(Collectors.toMap(Function.identity(), Function.identity()))
    private List<Double> sortedList2 = List.of(2d, 3d, 8d)
    private Map<Double, Double> list2Map = sortedList2.stream().collect(Collectors.toMap(Function.identity(), Function.identity()))
    private List<Double> newList = Lists.newArrayList()
    private Map<Double, Double> newListMap = Maps.newHashMap()
    private List<Double> expectedList = List.of(1d, 2d, 3d, 4d, 7d, 8d);
    private Map<Double, Double> expectedMap = expectedList.stream().collect(Collectors.toMap(Function.identity(), Function.identity()))

    void testMergeTwoSortedArrays() {
        CollectionUtils.mergeTwoSortedArrays(sortedList1, list1Map, sortedList2, list2Map, newList, newListMap, CollectionUtils.ASK_COMPERATOR)
        for(int i = 0; i < newList.size(); i++) {
            assertEquals(newList.get(i), expectedList.get(i))
        }
        for(Double key : expectedMap.keySet()) {
            assertEquals(expectedMap.get(key), newListMap.get(key))
        }
        newList.clear()
        newListMap.clear()
        CollectionUtils.mergeTwoSortedArrays(sortedList1, list1Map, Collections.EMPTY_LIST, Collections.EMPTY_MAP, newList, newListMap, CollectionUtils.BID_COMPERATOR)
        for(int i = 0; i < newList.size(); i++) {
            assertEquals(newList.get(i), sortedList1.get(i))
        }
        for(Double key : list1Map.keySet()) {
            assertEquals(newListMap.get(key), list1Map.get(key))
        }
    }
}
