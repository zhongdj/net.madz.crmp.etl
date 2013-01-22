package net.madz.db.metadata.comparators;

import java.util.Comparator;

import net.madz.db.metadata.Table;

public class TableComparator implements Comparator<Table> {

	@Override
	public int compare(Table t1, Table t2) {
		String t1Name = t1.getName();
		String t2Name = t2.getName();
		int length = t1Name.length() < t2Name.length() ? t1Name.length() : t2Name.length();
		for (int i = 0; i < length; i++) {
			if (t1Name.charAt(i) - t2Name.charAt(i) == 0) {
				continue;
			} else {
				return t1Name.charAt(i) - t2Name.charAt(i);
			}
		}
		return 0;
	}

}
